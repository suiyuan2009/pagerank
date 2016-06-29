package share;

import java.rmi.Remote;  
import java.rmi.RemoteException; 

public interface WorkerFunc extends Remote{
	public int sendPrMsg(String[] workerUrls, String[] workerIds) throws RemoteException;
}
