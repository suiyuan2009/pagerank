package master;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import share.WorkerFunc;

public class Master {
	static final int WorkNum = 2;
	static final int TMax = 10;
	static int SendCompleted = 0;
	static int SaveCompleted = 0;
	static MasterFuncImp func;
	static ArrayList<String> WorkerList = new ArrayList<String>();
	public static void main(String arg[]) throws Exception{
		try {
			LocateRegistry.createRegistry(8804);    
	        func = new MasterFuncImp();
	        // 注册到 registry 中  
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
		//String[] workerUrls = ;
		int [] workerIds = {0,1};
		String[] workerUrls = new String[WorkNum];
		for (int i = 0; i < WorkNum; i++) 
			workerUrls[i] = WorkerList.get(i);
		
		
		for (int o = 1; o <= TMax; o++) {
			//try {
				
				// while (1) {
				//   sleep(1000);
				//   所有人当前已经是空的, break
				//   CLEAR();
				// }
				
				System.out.println("Round " + o);
				SendCompleted = 0;
				for (int i = 0; i < WorkNum; i++) {
					funcs[i].sendPrMsg(workerUrls, workerIds, 1);
					System.out.println("Send  " + workerUrls[i]);
				}
				while (true) {
					Thread.sleep(500);
					//	System.out.println("Size now = " + WorkList.size());
						if (SendCompleted == WorkNum) {
							System.out.println("ALL Send Completed");
							break;
						}
					//if 
				}
				SaveCompleted = 0;
				for (int i = 0; i < WorkNum; i++) {
				
					funcs[i].calcPr();
					System.out.println("Calc  " + workerUrls[i]);
				}
				while (true) {
					Thread.sleep(500);
					//	System.out.println("Size now = " + WorkList.size());
						if (SaveCompleted == WorkNum) {
							System.out.println("ALL Calc Completed");
							break;
						}
					// if 
				}
			//} catch (Exception e) {
			//	S
			//}
		}
	}
	
	private void WaitWorker() throws Exception {
		System.out.println("Waiting worker.");
		while (true) {
			Thread.sleep(1000);
		//	System.out.println("Size now = " + WorkList.size());
			if (WorkerList.size() == WorkNum)
				break;
		}
		System.out.println("Add worker added.");
	}
}
