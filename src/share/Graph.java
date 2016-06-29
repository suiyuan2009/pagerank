package share;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Graph {
	public int WorkNo; // % WorkNum = WorkNo;
	public int M, N;
	private ArrayList X = new ArrayList();
	private ArrayList Y = new ArrayList();
	public Graph(String filename) {
		//WorkNum = x; WorkNo = y;
		File file = new File(filename);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            tempString = reader.readLine();
            String [] tmp = tempString.split(" ");
            N = Integer.parseInt(tmp[0]);
            M = 0;
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
            	System.out.println("line " + (line++) + ": " + tempString);
                tmp = tempString.split(" ");
                int u = Integer.parseInt(tmp[0]);
                int v = Integer.parseInt(tmp[1]);
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
	
	public boolean isVaild(int no, int WorkNum) {
		int x = (int)X.get(no);
		return x % WorkNum == WorkNo;
	}
	
}