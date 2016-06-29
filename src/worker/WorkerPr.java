package worker;

import java.util.ArrayList;
import java.util.HashMap;

import share.Graph;

public class WorkerPr {
	public int WorkerId;
	public ArrayList ids = new ArrayList();
	public HashMap idsmp = new HashMap();
	public ArrayList edges = new ArrayList();
	public ArrayList Pr = new ArrayList();
	public ArrayList nPr = new ArrayList();
	public ArrayList nids = new ArrayList();
	public int WorkerNum;
	public WorkerPr(Graph g, int WorkerNum_){
		WorkerId = g.WorkNo;
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
