package worker;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import share.WorkerFunc;
import worker.WorkerPr;

public class WorkerFuncImp extends UnicastRemoteObject implements WorkerFunc {
	public WorkerPr wpr; 
	public WorkerFuncImp(WorkerPr wpr_) throws RemoteException {  
        super();
        wpr = wpr_;
    }  
	public synchronized int sendPrMsg(String[] workerUrls, int[] workerIds) throws Exception{
    	for(int i=0;i<workerIds.length;i++){
    		int id = workerIds[i];
    		String url = workerUrls[i];
    		WorkerFunc send = (WorkerFunc)Naming.lookup(url);
    		
    		for(int j=0;j<wpr.ids.size();j++){
    			int idx = (int) wpr.ids.get(j);
    			double pr=(double) wpr.Pr.get(j);
    			for(int k=0;k<((ArrayList)wpr.edges.get(j)).size();k++){
    			int idy = (int) ((ArrayList)wpr.edges.get(j)).get(k);
    			if(idy%wpr.WorkerNum == id)
    				send.receivePrMsg(pr, idy);
    			}
    		}
    	}
		return 0;
    }
	public synchronized int receivePrMsg(double pr, int idx) throws Exception{
		
	wpr.nPr.add((double)pr);
		wpr.nids.add((int)idx);
		return 0;
	}
	
}
