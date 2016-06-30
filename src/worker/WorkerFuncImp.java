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
		System.out.println("master say to sendPrMsg:  " + "workerid: " + Worker.wpr.WorkerId);
		return 0;
	}

	public synchronized int receivePrMsg(double pr, int idx) throws Exception {

		Worker.wpr.addMsg(pr, idx);
		//Worker.wpr.nPr.add((double) pr);
		//Worker.wpr.nids.add((int) idx);
		// System.out.println("receive Msg: "+ "workerid: "+ Worker.wpr.WorkerId
		// + ", node pagerank value: " + pr + ", node id: " +idx);
		return 0;
	}

	public synchronized int calcPr() throws Exception {

		Worker.calcPrFlag = true;
		System.out.println("master say to calc Pr:  " + "workerid: " + Worker.wpr.WorkerId);
		return 0;
	}
}
