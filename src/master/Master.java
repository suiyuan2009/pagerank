package master;

import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import share.WorkerFunc;

public class Master {
	static final int WorkNum = 3;
	static final int TMax = 10;
	static final int CHUNK_SIZE = 10000;
	static final String FILE_NAME = "web-Google.txt";
	static int SetCompleted = 0;
	static int SendCompleted = 0;
	static int SaveCompleted = 0;
	static HashSet<String> Recovered = new HashSet<String>();
	static MasterFuncImp func;
	static ArrayList<String> WorkerList = new ArrayList<String>();

	public static void main(String arg[]) throws Exception {
		try {
			LocateRegistry.createRegistry(8804);
			func = new MasterFuncImp();
			// ע�ᵽ registry ��
			Naming.rebind("//162.105.96.50:8804/SAMPLE-SERVER", func);
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
		
		
		ArrayList Pr = new ArrayList();
		ArrayList ID = new ArrayList();
		HashMap map = new HashMap();
		for (int i = 0; i < WorkNum; i++) {
			funcs[i].getResult(Pr, ID);
			for (int j = 0; j < Pr.size(); j++) {
				map.put(-(double)Pr.get(i), (int)ID.get(i));
			}
			System.out.println("Get result from " + WorkerList.get(i));
		}
		
		FileWriter fileWriter = null;  
		try { 
			fileWriter = new FileWriter("result.txt");
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				HashMap.Entry entry = (HashMap.Entry) iter.next();
				double pr = -(double)entry.getKey();
				int id = (int)entry.getValue();
				System.out.println(id + "\t" + pr);
			}
		} catch (Exception e) {
        } finally {
			if (fileWriter != null) fileWriter.close(); 
        }
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
