package worker;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import share.MasterFunc;
import share.SharedFunc;


public class Worker {
	 public static void main(String[] args) throws Exception {
		 	String url = SharedFunc.GetIP("10.2.5.185");
	        //String url = "//162.105.96.50:8804/SAMPLE-SERVER";
		 	String serverUrl = "//162.105.96.50:8804/SAMPLE-SERVER";
		 	MasterFunc calc = (MasterFunc) Naming.lookup(serverUrl);
		 	calc.AddWorker(url);
		 	System.out.println(url);
	    }
}
