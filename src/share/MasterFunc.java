package share;

import java.rmi.Remote;  
import java.rmi.RemoteException;  
  
public interface MasterFunc extends Remote {  
    public int getFib(int n) throws RemoteException;  
//    public BigInteger getFib(BigInteger n) throws RemoteException;
    public int AddWorker(String url) throws RemoteException;
    
    public String GetWorker(int index) throws RemoteException;  
}