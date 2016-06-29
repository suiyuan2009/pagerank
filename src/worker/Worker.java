package worker;

import java.rmi.Naming;

import share.Graph;
import share.MasterFunc;
import share.SharedFunc;


public class Worker {
	 public static void main(String[] args) throws Exception {
		 	String url = SharedFunc.GetIP("10.2.5.185");
	        //String url = "//162.105.96.50:8804/SAMPLE-SERVER";
		 	String serverUrl = "//162.105.96.50:8804/SAMPLE-SERVER";
		 	MasterFunc calc = (MasterFunc) Naming.lookup(serverUrl);
		 	Graph g = new Graph("TestData01.txt");
		 	int id = calc.AddWorker(url);
		 	g.setWorkNo(id);
		 	System.out.println(id);
	    }
}
