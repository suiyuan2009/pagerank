package worker;

import java.io.FileReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import share.Graph;
import share.MasterFunc;
import share.WorkerFunc;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Worker {
    public static WorkerPr wpr;
    public static boolean sendMsgFlag;
    public static String[] workerUrls;
    public static int[] workerIds;
    public static boolean calcPrFlag;
    public static int round;
    public static int masterRound;
    public static boolean setRoundFlag;
    public static int countMsg;
    public static int chunkSize;
    public static boolean gameOverFlag;
    
    public synchronized int sendPrMsg() throws Exception {
        ArrayList MsgPrs = new ArrayList();
        ArrayList MsgIds = new ArrayList();
        for (int i = 0; i < Worker.wpr.WorkerNum; i++) {
            ArrayList Prs = new ArrayList();
            ArrayList Ids = new ArrayList();
            MsgPrs.add(Prs);
            MsgIds.add(Ids);
        }

        for (int j = 0; j < Worker.wpr.ids.size(); j++) {
            int nodex = (int) Worker.wpr.ids.get(j);
            double pr = ((double) Worker.wpr.Pr.get(j)) / (int) ((ArrayList<Integer>) Worker.wpr.edges.get(j)).size();
            for (int k = 0; k < ((ArrayList<Integer>) Worker.wpr.edges.get(j)).size(); k++) {
                int nodey = (int) ((ArrayList<Integer>) Worker.wpr.edges.get(j)).get(k);
                int workerid = nodey % Worker.wpr.WorkerNum;
                ((ArrayList<Double>) MsgPrs.get(workerid)).add(pr);
                ((ArrayList<Integer>) MsgIds.get(workerid)).add(nodey);
            }
        }

        for (int i = 0; i < workerIds.length; i++) {
            int id = workerIds[i];
            String url = workerUrls[i];
            System.out.println(
                    "worker " + wpr.WorkerId + " sendPrMsg to " + " worker " + id + ", worker's url is " + url);
            if (id == Worker.wpr.WorkerId) {
                Worker.wpr.addMsg((ArrayList) MsgPrs.get(id), (ArrayList) MsgIds.get(id));
            } else {
                WorkerFunc send = (WorkerFunc) Naming.lookup(url);
                ArrayList<Double> MsgPr = (ArrayList<Double>) MsgPrs.get(id);
                ArrayList<Integer> MsgId = (ArrayList<Integer>) MsgIds.get(id);

                ArrayList<Integer> ids = new ArrayList<Integer>();
                ArrayList<Double> prs = new ArrayList<Double>();
                int cnt = 0;
                int limit = chunkSize;
                for (int i1 = 0; i1 < MsgPr.size(); i1++) {
                    prs.add((double) MsgPr.get(i1));
                    ids.add((int) MsgId.get(i1));
                    cnt++;
                    if (cnt == limit) {
                        send.receivePrMsg(prs, ids);
                        cnt = 0;
                        prs.clear();
                        ids.clear();
                        System.out.println("send " + (i1 * 100.0 / MsgPr.size()) + "%");
                    }
                }
                if (cnt > 0) {
                    send.receivePrMsg(prs, ids);
                    cnt = 0;
                    prs.clear();
                    ids.clear();
                    System.out.println("send 100.00%");
                }
            }
        }

        System.out.println("worker " + Worker.wpr.WorkerId + " finished sending");
        return 0;
    }

    public static void main(String[] args) throws Exception {
        
        //String ipurl = SharedFunc.getLocalHostIP();
        //System.out.println(ipurl);
       	 
	Options options = new Options();
	options.addOption(Option.builder()
			.longOpt("configure")
			.desc("xxxxx")
			.hasArg()
			.argName("configure")
			.build());
	CommandLineParser parser = new DefaultParser();
	CommandLine cmd = parser.parse(options, args);
	String configureFilePath = cmd.getOptionValue("configure");
	if(configureFilePath == null){
	System.out.println("no configure file path provided!");
	System.exit(0);
	}else{
	System.out.println("configure file path is " + configureFilePath);
	}


	String workerUrl = "";
	String checkpointDir = "";
	String dataDir = "";
	String masterUrl = "";

  	try {
	       JSONParser parserJson = new JSONParser();	
            Object obj = parserJson.parse(new FileReader(configureFilePath));
 
            JSONObject jsonObject = (JSONObject) obj;
 
            workerUrl  = (String)jsonObject.get("worker_ip:port");
            masterUrl  = (String)jsonObject.get("master_ip:port");
	    dataDir = (String)jsonObject.get("data_dir");
	    checkpointDir = (String)jsonObject.get("checkpoint_dir");
            System.out.println("data dir: " + dataDir);
            System.out.println("worker ip:port: " + workerUrl);
            System.out.println("master ip:port: " + masterUrl);
            System.out.println("checkpoint dir: " + checkpointDir);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        // String url = SharedFunc.GetIP("10.2.5.185", portList);
        String url = "//"+ workerUrl + "/FUNCTION";

        String serverUrl = "//" + masterUrl + "/SAMPLE-SERVER";
        MasterFunc master = (MasterFunc) Naming.lookup(serverUrl);
        System.out.println("master url is" + masterUrl);

        String filename = dataDir + master.GetFileName();
	System.out.println("data file: "+filename);
        Graph g = new Graph(filename);
        int workerNum = master.GetWorkerNum();
        chunkSize = master.GetChunkSize();

        try {
	    String[] parts = workerUrl.split(":");
            LocateRegistry.createRegistry(Integer.parseInt(parts[1]));
            WorkerFuncImp func = new WorkerFuncImp();
            Naming.rebind(url, func);
        } catch (RemoteException re) {
            System.out.println("Exception in FibonacciImpl.main: " + re);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException " + e);
        }

        int id = master.AddWorker(url);
        g.setWorkNo(id);
        System.out.println("this worker id is " + id);

        String checkpointPath = checkpointDir;
        Worker worker = new Worker();
        worker.wpr = new WorkerPr(g, workerNum, checkpointPath);
        worker.wpr.saveCheckPoint();

        System.out.println("worker " + wpr.WorkerId + " ready");
        System.out.println("worker " + wpr.WorkerId + " page rank begins");

        
        //worker.wpr.print(round);
        round = 1;
        
        while (true) {
            Thread.sleep(100);
            if (worker.setRoundFlag == true) {
                System.out.println("\nround "+masterRound);
                worker.wpr.setRound();
                worker.setRoundFlag = false;
                System.out.println("worker " + wpr.WorkerId + " say to master set round finished");
                master.Completed(id, MasterFunc.SET_COMPLETED);
            } else if (worker.sendMsgFlag == true) {
                try {
                    //worker.wpr.setRound();
                    worker.sendPrMsg();
                } catch (Exception e) {
                    System.out.println("sendPrMsg failed: " + e);
                    e.printStackTrace();
                }
                worker.sendMsgFlag = false;
                System.out.println("worker " + wpr.WorkerId + " say to master send msg finished");
                master.Completed(id, MasterFunc.SENT_COMPLETED);
            } else if (worker.calcPrFlag == true) {
                System.out.println("worker " + wpr.WorkerId + " receive " + worker.countMsg + " msg");
                worker.wpr.calcPr();
                worker.wpr.saveCheckPoint();
                worker.calcPrFlag = false;
                System.out.println("worker " + wpr.WorkerId + " say to master calc Pr finished");
                worker.countMsg = 0;
                master.Completed(id, MasterFunc.SAVE_COMPLETED);
                //worker.wpr.print(round);
                round++;
            }else if (worker.gameOverFlag == true){
                System.out.println("worker " + wpr.WorkerId + " exits");
                System.exit(0);
            }
        }

    }
}
