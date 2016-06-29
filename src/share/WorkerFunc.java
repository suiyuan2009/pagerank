package share;

import java.rmi.Remote;  
import java.rmi.RemoteException; 

public interface WorkerFunc extends Remote{
	public int sendPrMsg(String[] workerUrls, int[] workerIds) throws Exception;
	public int receivePrMsg(double pr, int idx) throws Exception;
}
