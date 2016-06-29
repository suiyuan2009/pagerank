package share;

import java.rmi.Remote;  
import java.rmi.RemoteException;  
  
public interface MasterFunc extends Remote { 
	public static final int SENT_COMPLETED = 0;
	public static final int CALC_COMPLETED = 1;
	
    public int getFib(int n) throws RemoteException;  
//    public BigInteger getFib(BigInteger n) throws RemoteException;
    public int AddWorker(String url) throws RemoteException;
    
    public void Completed(int id, int type) throws RemoteException;
    
    public String GetWorker(int index) throws RemoteException;  
}