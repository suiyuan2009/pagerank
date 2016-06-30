package share;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface WorkerFunc extends Remote {
	public int sendPrMsg(String[] workerUrls, int[] workerIds, int masterRound) throws Exception;

	public int receivePrMsg(ArrayList prs, ArrayList idxs) throws Exception;

	public int calcPr() throws Exception;
	
	public int setRound(int masterRound) throws Exception;
}
