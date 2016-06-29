package worker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import share.WorkerFunc;

public class WorkerFuncImp extends UnicastRemoteObject implements WorkerFunc {
	public WorkerFuncImp() throws RemoteException {  
        super();  
    }  
	public synchronized int sendPrMsg(String[] workerUrls, String[] workerIds) throws RemoteException{
    	return 0;
    }
}
