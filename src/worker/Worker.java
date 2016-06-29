package worker;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Worker {
	 public static void main(String[] args) {
	        String url = "//162.105.96.50:8804/SAMPLE-SERVER";
	        try {
	        
	         
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (RemoteException e) {
	            e.printStackTrace();
	        } catch (NotBoundException e) {
	            e.printStackTrace();
	        }
	    }
}
