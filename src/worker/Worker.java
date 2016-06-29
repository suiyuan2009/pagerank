package worker;

import share.Graph;
import share.SharedFunc;


public class Worker {
	public static void main(String arg[]) throws Exception {
		System.out.println(SharedFunc.GetIP());
        //File file = new File();
        Graph g = new Graph(3, 2, "TestData01.txt");
        for (int i = 0; i < g.M; i++) {
        	System.out.println(g.getX(i) + " " + g.getY(i));
        }
	}
}
