package share;

import java.rmi.Remote;
import java.util.ArrayList;

public interface WorkerFunc extends Remote {
    public int sendPrMsg(String[] workerUrls, int[] workerIds, int masterRound) throws Exception;

    public int receivePrMsg(ArrayList prs, ArrayList idxs) throws Exception;

    public int calcPr() throws Exception;
    
    public int setRound(int masterRound) throws Exception;
    
    public ArrayList getResultPr() throws Exception;
    public ArrayList getResultId() throws Exception;
    public void gameOver() throws Exception;
}
