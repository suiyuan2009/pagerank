package worker;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import share.WorkerFunc;
import worker.WorkerPr;
import worker.Worker;

public class WorkerFuncImp extends UnicastRemoteObject implements WorkerFunc {

	public WorkerFuncImp() throws RemoteException {
		super();
	}

	public synchronized int sendPrMsg(String[] workerUrls, int[] workerIds, int masterRound) throws Exception {
		Worker.workerUrls = workerUrls;
		Worker.workerIds = workerIds;
		Worker.sendMsgFlag = true;
		Worker.masterRound = masterRound;
		System.out.println("\nround "+masterRound + "\nmaster say to worker " + Worker.wpr.WorkerId + " to sendPrMsg");
		return 0;
	}

	public synchronized int receivePrMsg(ArrayList prs, ArrayList idxs) throws Exception {
		Worker.wpr.addMsg(prs, idxs);
		/*for(int i=0;i<prs.size();i++){
			Worker.wpr.addMsg((double)prs.get(i), (int)idxs.get(i));
		}*/
		return 0;
	}

	public synchronized int calcPr() throws Exception {
		Worker.calcPrFlag = true;
		System.out.println("master say to worker " + Worker.wpr.WorkerId + " to calc Pr");
		return 0;
	}
	
	public synchronized int setRound(int masterRound) throws Exception{
		Worker.masterRound = masterRound;
		Worker.wpr.setRound();
		return 0;
	}
}
