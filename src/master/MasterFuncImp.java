package master;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import share.MasterFunc;
  
public class MasterFuncImp extends UnicastRemoteObject implements MasterFunc {  
    public MasterFuncImp() throws RemoteException {  
        super();  
    }  
      
     
    public int getFib(int n) throws RemoteException {  
        return n+2;  
    }  
    
    public synchronized int AddWorker(String url) throws RemoteException {
    	System.out.println("Add worker " + url);
    	Master.WorkerList.add(url);
    	System.out.println("WorkList size = " + Master.WorkerList.size());
		return Master.WorkerList.size() - 1;
    }
    
    public String GetWorker(int index) throws RemoteException {
    	return Master.WorkerList.get(index);
    }
}