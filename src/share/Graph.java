package share;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Graph {
    public int WorkNo; // % WorkNum = WorkNo;
    public int M, N;
    private ArrayList<Integer> X = new ArrayList<Integer>();
    private ArrayList<Integer> Y = new ArrayList<Integer>();
    public Graph(String filename) {
        //WorkNum = x; WorkNo = y;
        File file = new File(filename);
        BufferedReader reader = null;
        try {
           // System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            tempString = reader.readLine();
            String [] tmp = tempString.split(" ");
            //N = Integer.parseInt(tmp[0]);
            N = 0;
            M = 0;
            while ((tempString = reader.readLine()) != null) {
                // ��ʾ�к�
                //System.out.println("line " + (line++) + ": " + tempString);
                tmp = tempString.split(" ");
                int u = Integer.parseInt(tmp[0]);
                int v = Integer.parseInt(tmp[1]);
                N = Math.max(N,u+1);
                N = Math.max(N,v+1);
                //if (u % WorkNum == WorkNo) {
                    M++;
                    X.add(u);
                    Y.add(v);
                //}
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        System.out.println("Finished read graph N = " + N + " M = " + M);
    }
    
    public int getX(int id) {
        return (int)X.get(id);
    }
    
    public int getY(int id) {
        return (int)Y.get(id);
    }
    
    public void setWorkNo(int no) {
        WorkNo = no;
    }
    
    public boolean isVaild(int x, int WorkNum) {
        return x % WorkNum == WorkNo;
    }
    
}