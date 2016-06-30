package worker;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import master.Master;
import master.MasterFuncImp;
import share.Graph;
import share.MasterFunc;
import share.SharedFunc;
import share.WorkerFunc;

public class Worker {
	public static WorkerPr wpr;
	public static boolean sendMsgFlag;
	public static String[] workerUrls;
	public static int[] workerIds;
	public static boolean calcPrFlag;
	public static int round;
	public static int masterRound;

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
			double pr = ((double) Worker.wpr.Pr.get(j)) / (int) ((ArrayList) Worker.wpr.edges.get(j)).size();
			for (int k = 0; k < ((ArrayList) Worker.wpr.edges.get(j)).size(); k++) {
				int nodey = (int) ((ArrayList) Worker.wpr.edges.get(j)).get(k);
				int workerid = nodey % Worker.wpr.WorkerNum;
				((ArrayList) MsgPrs.get(workerid)).add(pr);
				((ArrayList) MsgIds.get(workerid)).add(nodey);
			}
		}

		for (int i = 0; i < workerIds.length; i++) {
			int id = workerIds[i];
			String url = workerUrls[i];
			System.out.println("worker "+ wpr.WorkerId +" sendPrMsg to " + " worker " + id + ", worker's url is " + url);
			if (id == Worker.wpr.WorkerId) {
				Worker.wpr.addMsg((ArrayList) MsgPrs.get(id), (ArrayList) MsgIds.get(id));
			} else {
				WorkerFunc send = (WorkerFunc) Naming.lookup(url);
				send.receivePrMsg((ArrayList) MsgPrs.get(id), (ArrayList) MsgIds.get(id));
			}
		}
		System.out.println("worker "+ Worker.wpr.WorkerId + " finished sending");
		return 0;
	}

	public static void main(String[] args) throws Exception {
		ArrayList portList = new ArrayList();
		portList.add(7876);
		// String url = SharedFunc.GetIP("10.2.5.185", portList);
		System.out.println("this worker port: " + portList.get(0));
		String url = "//10.2.5.185:" + portList.get(0) + "/FUNCTION";

		String serverUrl = "//162.105.96.50:8804/SAMPLE-SERVER";
		MasterFunc master = (MasterFunc) Naming.lookup(serverUrl);
		System.out.println("master url is" + serverUrl);

		Graph g = new Graph("p2p-Gnutella08.txt");
		int workerNum = 2;
		int id = master.AddWorker(url);
		g.setWorkNo(id);
		System.out.println("this worker id is " + id);

		String checkpointPath = "checkpoint";
		Worker worker = new Worker();
		worker.wpr = new WorkerPr(g, workerNum, checkpointPath);
		worker.wpr.saveCheckPoint();

		try {
			LocateRegistry.createRegistry((int) portList.get(0));
			WorkerFuncImp func = new WorkerFuncImp();
			// ע�ᵽ registry ��
			Naming.rebind(url, func);
			System.out.println("worker "+ wpr.WorkerId +" ready");
		} catch (RemoteException re) {
			System.out.println("Exception in FibonacciImpl.main: " + re);
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException " + e);
		}
		System.out.println("worker "+ wpr.WorkerId +" page rank begins");

		round = 0;
		worker.wpr.print(round);
		round++;
		
		while (true) {
			Thread.sleep(100);
			if (worker.sendMsgFlag == true) {
				try {
					worker.wpr.setRound();
					worker.sendPrMsg();
				} catch (Exception e) {
					System.out.println("sendPrMsg failed: " + e);
					e.printStackTrace();
				}
				worker.sendMsgFlag = false;
				System.out.println("worker "+ wpr.WorkerId +" say to master send msg finished");
				master.Completed(id, MasterFunc.SENT_COMPLETED);
			} else if (worker.calcPrFlag == true) {
				worker.wpr.calcPr();
				worker.wpr.saveCheckPoint();
				worker.calcPrFlag = false;
				System.out.println("worker "+ wpr.WorkerId +" say to master calc Pr finished");
				master.Completed(id, MasterFunc.SAVE_COMPLETED);
				worker.wpr.print(round);
				round++;
			}
		}

	}
}
