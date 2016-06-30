package master;

public class ForSort implements Comparable<ForSort> {

    private double pr;
    private int id;

    public ForSort(double a, int b) {
        pr = a;
        id = b;
    }
    
    public double getPr() {
        return pr;
    }
    
    public int getID() {
        return id;
    }
    public String toString() {
        return id + " " + String.format("%.5f", pr);
    }
    @Override
    public int compareTo(ForSort o) {
        // ÏÈ°´ageÅÅÐò
        if (this.pr > o.pr) 
            return -1;
        else
        if (this.pr < o.pr)
            return 1;
        else
        return 0;
    }
}