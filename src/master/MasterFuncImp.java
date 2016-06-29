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
    	Master.WorkList.add(url);
    	System.out.println("WorkList size = " + Master.WorkList.size());
		return Master.WorkList.size();
    }
    
    public String GetWorker(int index) throws RemoteException {
    	return Master.WorkList.get(index);
    }
}