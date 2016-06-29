package worker;

import java.util.ArrayList;
import java.util.HashMap;

import share.Graph;

public class WorkerPr {
	private ArrayList ids = new ArrayList();
	private HashMap idsmp = new HashMap();
	private ArrayList edges = new ArrayList();
	private ArrayList Pr = new ArrayList();
	private int WorkerNum;
	public WorkerPr(Graph g, int WorkerNum_){
		WorkerNum = WorkerNum_;
		int cnt = 0;
		for(int i=0;i<g.N;i++)
			if(g.isVaild(i, WorkerNum)){
				ids.add(i);
				Pr.add(1.0/g.N);
				idsmp.put(i,cnt);
				cnt++;
				ArrayList e = new ArrayList();
				edges.add(e);
			}
		for(int i=0;i<g.M;i++){
			int x = g.getX(i);
			if(g.isVaild(x, WorkerNum)){
				int idx = (int) idsmp.get(x);
				ArrayList e = (ArrayList)(edges.get(idx));
				e.add(g.getY(i));
			}
		}
		System.out.println(ids);
		System.out.println(((ArrayList)edges.get(0)).size());
		System.out.println(((ArrayList)edges.get(1)).size());
	}
}
