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
	public Graph g;

	public WorkerPr(Graph g_, int WorkerNum_) {
		g = g_;
		WorkerId = g.WorkNo;
		WorkerNum = WorkerNum_;
		int cnt = 0;
		for (int i = 0; i < g.N; i++)
			if (g.isVaild(i, WorkerNum)) {
				ids.add(i);
				Pr.add(1.0 / g.N);
				idsmp.put(i, cnt);
				cnt++;
				ArrayList e = new ArrayList();
				edges.add(e);
			}
		System.out.println(Pr.size()+ ","+ids.size());
		for (int i = 0; i < g.M; i++) {
			int x = g.getX(i);
			if (g.isVaild(x, WorkerNum)) {
				//if(!idsmp.containsKey(x))System.out.println("idsmp not exists: "+ x);
				int idx = (int) idsmp.get(x);
				ArrayList e = (ArrayList) (edges.get(idx));
				e.add(g.getY(i));
			}
		}
		// System.out.println(ids);
		// System.out.println(((ArrayList)edges.get(0)).size());
		// System.out.println(((ArrayList)edges.get(1)).size());
	}

	public int calcPr() {
		for (int i = 0; i < ids.size(); i++) {
			Pr.set(i, 0.15 / g.N);
		}
		System.out.println("size2: "+ nids.size()+ ","+ nPr.size());
		for (int i = 0; i < (int)nids.size(); i++) {
			
			if(i>=(int)nids.size())System.out.println("nids big: "+ i);
			
			if((!idsmp.containsKey((int)nids.get(i))))System.out.println("idsmp not exits: "+ nids.get(i));
			int idx = (int) idsmp.get((int)nids.get(i));
			double cur = (double) Pr.get(idx);
			if(Pr.size()<=idx)System.out.println("pr.size()<="+idx);
			if(nPr.size()<=i)System.out.println("npr.size()<="+i);
			Pr.set(idx, cur + 0.85 * (double) nPr.get(i));
			
		}
		nPr.clear();
		nids.clear();
		return 0;
	}

	public synchronized void addMsg(double pr,int idx){
		nPr.add(pr);
		nids.add(idx);
	}
	
	public void print(int round) {
		System.out.println("round: " + round);
		for (int i = 0; i < ids.size(); i++) {
			int idx = (int) ids.get(i);
			if(idx % 100000 == 0)
			System.out.println("id: " + ids.get(i) + ",pr: " + Pr.get(i));
		}
	}
}
