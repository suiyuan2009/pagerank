package master;

import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import share.WorkerFunc;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Master {
    static int WorkNum = 1;
    static final int TMax = 10;
    static final int CHUNK_SIZE = 10000;
    static String FILE_NAME = "p2p-Gnutella08.txt";
    static int SetCompleted = 0;
    static int SendCompleted = 0;
    static int SaveCompleted = 0;
    static HashSet<String> Recovered = new HashSet<String>();
    static MasterFuncImp func;
    static ArrayList<String> WorkerList = new ArrayList<String>();

    public static void main(String[] args) throws Exception {

	Options options = new Options();
	options.addOption(Option.builder()
			.longOpt("configure")
			.desc("xxxxx")
			.hasArg()
			.argName("configure")
			.build());
	options.addOption(Option.builder()
			.longOpt("data")
			.desc("xxxxx")
			.hasArg()
			.argName("data")
			.build());
	options.addOption(Option.builder()
			.longOpt("workernum")
			.desc("xxxxx")
			.hasArg()
			.argName("workernum")
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

	String dataFileName = cmd.getOptionValue("data");
	if(dataFileName == null){
	System.out.println("no data file provided!");
	System.exit(0);
	}else{
	FILE_NAME = dataFileName;
	System.out.println("data file name is " + dataFileName);
	}
	
	String workNum = cmd.getOptionValue("workernum");
	if(workNum == null){
	System.out.println("no work num provided!");
	System.exit(0);
	}else{
	WorkNum = Integer.parseInt(workNum);
	System.out.println("work num is " + workNum);
	}

	String masterUrl = "";
	String resultDir = "";
  	try {
	       JSONParser parserJson = new JSONParser();	
            Object obj = parserJson.parse(new FileReader(configureFilePath));
 
            JSONObject jsonObject = (JSONObject)obj;
 
            masterUrl  = (String)jsonObject.get("master_ip:port");
	    resultDir = (String)jsonObject.get("result_dir");

            System.out.println("master ip:port: " + masterUrl);
            System.out.println("result dir: " + resultDir);
 
        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
		String[] parts = masterUrl.split(":");
            LocateRegistry.createRegistry(Integer.parseInt(parts[1]));
            func = new MasterFuncImp();
           
            Naming.rebind("//" + masterUrl + "/SAMPLE-SERVER", func);
            System.out.println("master server ready");
        } catch (RemoteException re) {
            System.out.println("Exception in FibonacciImpl.main: " + re);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException " + e);
        }
        Master master = new Master();
        master.WaitWorker();
        WorkerFunc[] funcs = new WorkerFunc[WorkNum];
        for (int i = 0; i < WorkNum; i++) {
            System.out.println("look up " + WorkerList.get(i));
            funcs[i] = (WorkerFunc) Naming.lookup(WorkerList.get(i));
        }
        // String[] workerUrls = ;
        int[] workerIds = new int[WorkNum];
        for (int i = 0; i < WorkNum; i++)
            workerIds[i] = i;
        String[] workerUrls = new String[WorkNum];
        for (int i = 0; i < WorkNum; i++)
            workerUrls[i] = WorkerList.get(i);

        for (int o = 1; o <= TMax; o++) {
            for (String s : Recovered) {
                for (int i = 0; i < WorkerList.size(); i++)
                    if (s.equals(WorkerList.get(i))) {
                        System.out.println("look up " + WorkerList.get(i));
                        funcs[i] = (WorkerFunc) Naming
                                .lookup(WorkerList.get(i));
                    }
            }
            Recovered.clear();

            System.out.println("Round " + o);

            SetCompleted = 0;
            for (int i = 0; i < WorkNum; i++) {
                try {
                    funcs[i].setRound(o);
                } catch (Exception e) {
                    System.out.println("Exception @ setRound");
                    e.printStackTrace();
                }
                System.out.println("SetRound " + workerUrls[i]);
            }

            while (true) {
                Thread.sleep(100);
                if (SetCompleted + Recovered.size() >= WorkNum) {
                    System.out.println("ALL Set Completed");
                    break;
                }
            }

            if (Recovered.size() > 0) {
                o--;
                continue;
            }

            SendCompleted = 0;
            for (int i = 0; i < WorkNum; i++) {
                try {
                    funcs[i].sendPrMsg(workerUrls, workerIds, o);
                } catch (Exception e) {
                    System.out.println("Exception @ sendPrMsg");
                    e.printStackTrace();
                }
                System.out.println("SendPrMsg  " + workerUrls[i]);
            }

            while (true) {
                Thread.sleep(100);
                if (SendCompleted + Recovered.size() >= WorkNum) {
                    System.out.println("ALL Send Completed");
                    break;
                }
            }

            if (Recovered.size() > 0) {
                o--;
                continue;
            }

            SaveCompleted = 0;
            for (int i = 0; i < WorkNum; i++) {
                try {
                    funcs[i].calcPr();
                } catch (Exception e) {
                    System.out.println("Exception @ send Calc");
                }
                System.out.println("Calc  " + workerUrls[i]);
            }
            while (true) {
                Thread.sleep(100);
                if (SaveCompleted + Recovered.size() >= WorkNum) {
                    System.out.println("ALL Calc Completed");
                    break;
                }
            }

            if (Recovered.size() > 0) {
                o--;
                continue;
            }

        }
        
        
        ArrayList<Double> Pr = new ArrayList<Double>();
        ArrayList<Integer> ID = new ArrayList<Integer>();
        //Map map = new TreeMap();
        ArrayList A = new ArrayList();
        for (int i = 0; i < WorkNum; i++) {
            ID = funcs[i].getResultId();
            Pr = funcs[i].getResultPr();
            for (int j = 0; j < Pr.size(); j++) {
                //map.put(-((double)Pr.get(j)), (int)ID.get(j));
                A.add(new ForSort((double)Pr.get(j), (int)ID.get(j)));
            }
            System.out.println("Get result from " + WorkerList.get(i) + " size = " + Pr.size());
        }
        
        Collections.sort(A);
        
        //for (int i = 0; i < 10; i++)
        //    System.out.println(((ForSort)A.get(i)).getPr() + " " + ((ForSort)A.get(i)).getID());
        
        FileWriter fileWriter = null;  
        try { 
            fileWriter = new FileWriter(resultDir + "result.txt");
            for (int i = 0; i < A.size(); i++) 
                fileWriter.write(A.get(i).toString() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) fileWriter.close(); 
        }
        
        for (int i = 0; i < WorkNum; i++) {
            funcs[i].gameOver();
        }
        
        System.exit(0);
    }

    private void WaitWorker() throws Exception {
        System.out.println("Waiting worker.");
        while (true) {
            Thread.sleep(1000);
            // System.out.println("Size now = " + WorkList.size());
            if (WorkerList.size() == WorkNum)
                break;
        }
        System.out.println("Add worker added.");
    }
}
