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
    	for (int i = 0; i < Master.WorkerList.size(); i++) {
    		if (url.equals(Master.WorkerList.get(i))) {
    			System.out.println("Already Exists " + url);
    			Master.Recovered.add(url);
    			return i;
    		}
    	}
    	Master.WorkerList.add(url);
    	System.out.println("WorkList size = " + Master.WorkerList.size());
		return Master.WorkerList.size() - 1;
    }
    
    public int GetWorkerNum() throws RemoteException {
    	return Master.WorkNum;
    }
    
    public synchronized void Completed(int id, int type) throws RemoteException {
    	if (type == MasterFunc.SENT_COMPLETED) Master.SendCompleted ++;
    	if (type == MasterFunc.SAVE_COMPLETED) Master.SaveCompleted ++;
    	if (type == MasterFunc.SET_COMPLETED) Master.SetCompleted ++;
    }
    
    public int GetChunkSize() throws RemoteException {
    	return Master.CHUNK_SIZE;
    }
    
    public String GetFileName() throws RemoteException {
    	return Master.FILE_NAME;
    }
}