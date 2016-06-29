import java.math.BigInteger;  
import java.rmi.*;  
import java.rmi.server.UnicastRemoteObject;  
  
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
		return 0;
    }
     
}