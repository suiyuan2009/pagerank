package master;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;


public class Master {
	static final int TMax = 10;
	static MasterFuncImp func;
	static ArrayList<String> WorkList = new ArrayList<String>();
	public static void main(String arg[]) {
		try {
			LocateRegistry.createRegistry(8804);    
	        func = new MasterFuncImp();
	        // ×¢²áµ½ registry ÖÐ  
	        Naming.rebind("//10.2.7.140:8804/SAMPLE-SERVER", func);  
	        System.out.println("master server ready");
		} catch (RemoteException re) {  
            System.out.println("Exception in FibonacciImpl.main: " + re);  
        } catch (MalformedURLException e) {  
            System.out.println("MalformedURLException " + e);  
        }
		Master master = new Master();
		master.WaitWorker();
	}
	
	private void WaitWorker() {
		while (WorkList.size() < 2);
	}
}
