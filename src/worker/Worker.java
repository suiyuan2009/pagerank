package worker;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import share.SharedFunc;

public class Worker {
	 public static void main(String[] args) throws Exception {
		 	String url = SharedFunc.GetIP();
	        //String url = "//162.105.96.50:8804/SAMPLE-SERVER";
		 	System.out.println(url);
	    }
}
