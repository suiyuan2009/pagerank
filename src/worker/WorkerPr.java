package worker;

import java.util.ArrayList;
import java.util.HashMap;

import share.Graph;
import share.SharedFunc;

public class WorkerPr {
    public int WorkerId;
    public ArrayList<Integer> ids = new ArrayList<Integer>();
    public HashMap<Integer, Integer> idsmp = new HashMap();
    public ArrayList edges = new ArrayList();
    public ArrayList<Double> Pr = new ArrayList<Double>();
    public ArrayList<Double> nPr = new ArrayList<Double>();
    public ArrayList<Integer> nids = new ArrayList<Integer>();
    public int WorkerNum;
    public String checkpointPath;
    public Graph g;

    public WorkerPr(Graph g_, int WorkerNum_, String checkpointPath_) throws Exception {
        g = g_;
        WorkerId = g.WorkNo;
        WorkerNum = WorkerNum_;
        checkpointPath = checkpointPath_;
        int cnt = 0;
        for (int i = 0; i < g.N; i++)
            if (g.isVaild(i, WorkerNum)) {
                ids.add(i);
                Pr.add(1.0);
                idsmp.put(i, cnt);
                cnt++;
                ArrayList e = new ArrayList();
                edges.add(e);
            }
        System.out.println(Pr.size() + "," + ids.size());
        for (int i = 0; i < g.M; i++) {
            int x = g.getX(i);
            if (g.isVaild(x, WorkerNum)) {
                int idx = (int) idsmp.get(x);
                ArrayList e = (ArrayList) (edges.get(idx));
                e.add(g.getY(i));
            }
        }
        System.out.println("worker " + WorkerId + " init finished");
    }

    public int setRound() throws Exception {
        if (Worker.round != Worker.masterRound) {
            System.out.println(
                    "master say to worker " + Worker.wpr.WorkerId + " to reset to round " + Worker.masterRound);
            SharedFunc.ReadCheckpoint(checkpointPath, Worker.masterRound - 1, ids, Pr);
            Worker.round = Worker.masterRound;
            System.out.println("worker " + Worker.wpr.WorkerId + " read from checkpoint, round " + Worker.masterRound);
        } else {
            System.out.println("worker " + Worker.wpr.WorkerId + " read from memory, round " + Worker.masterRound);
        }
        Worker.wpr.clearMsg();
        // System.out.println("finished clear");
        return 1;
    }

    public int saveCheckPoint() throws Exception {
        SharedFunc.WriteCheckpoint(checkpointPath, Worker.round, ids, Pr);
        return 0;
    }

    public int clearMsg() {
        nPr.clear();
        nids.clear();
        return 0;
    }

    public int calcPr() throws Exception {
        /*
         * for (int i = 0; i < ids.size(); i++) { Pr.set(i, 0.15 / g.N); } for
         * (int i = 0; i < (int) nids.size(); i++) { int idx = (int)
         * idsmp.get((int) nids.get(i)); double cur = (double) Pr.get(idx);
         * Pr.set(idx, cur + 0.85 * (double) nPr.get(i)); }
         */
        for (int i = 0; i < ids.size(); i++) {
            Pr.set(i, 0.0);
        }
        for (int i = 0; i < (int) nids.size(); i++) {
            int idx = (int) idsmp.get((int) nids.get(i));
            double cur = (double) Pr.get(idx);
            Pr.set(idx, cur + (double) nPr.get(i));
        }
        for (int i = 0; i < ids.size(); i++) {
            double cur = (double) Pr.get(i);
            Pr.set(i, cur * 0.85 + 0.15);
        }
        Worker.wpr.clearMsg();
        return 1;
    }

    public synchronized void addMsg(double pr, int idx) {
        nPr.add(pr);
        nids.add(idx);
        //System.out.println("msg: " + pr + "," + idx);
        Worker.countMsg++;
    }

    public synchronized void addMsg(ArrayList<Double> prs, ArrayList<Integer> idxs) {
        for (int i = 0; i < prs.size(); i++) {
            Worker.wpr.addMsg((double) prs.get(i), (int) idxs.get(i));
        }
    }

    public void print(int round) {
        System.out.println("worker " + Worker.wpr.WorkerId + " round " + round + ", output pr");
        for (int i = 0; i < ids.size(); i++) {
            int idx = (int) ids.get(i);
            if (idx % 100000 == 0)
            System.out.println("id: " + ids.get(i) + ",pr: " + Pr.get(i));
        }
    }
}
