package Server;

public class Stick extends Object{
    Stick(String sN, int bx, int by, int ex, int ey){
        setStickName(sN);
        setStickCoordBeg(bx, by);
        setStickCoordEnd(ex, ey);
        setStickLength();
    }
    private String stickName;
    private int[] stickCoordBeg = new int[2];
    private int[] stickCoordEnd = new int[2];
    private int stickLength;


    public int getStickLength() {
        return stickLength;
    }

    public void setStickLength() {
        this.stickLength = (int)Math.sqrt(Math.pow(getStickCoordBeg()[0]-getStickCoordEnd()[0],2)+Math.pow(getStickCoordBeg()[1]-getStickCoordEnd()[1],2));
    }

    public String getStickName() {
        return stickName;
    }

    public void setStickName(String stickName) {
        this.stickName = stickName;
    }

    public int[] getStickCoordBeg(){
        int[] rStickCoordBeg = new int[2];
        rStickCoordBeg[0]  = stickCoordBeg[0];
        rStickCoordBeg[1] = stickCoordBeg[1];
        return rStickCoordBeg;
    }
    public int getStickCoordBeg(int index){
        int tmpStickCoordBeg = stickCoordBeg[index];
        return tmpStickCoordBeg;
    }
    public int getStickCoordEnd(int index){
        int tmpStickCoordEnd = stickCoordEnd[index];
        return tmpStickCoordEnd;
    }

    public void setStickCoordBeg(int x, int y) throws TestRuntimeException{
        stickCoordBeg[0] = x;
        stickCoordBeg[1] = y;
    }

    public int[] getStickCoordEnd(){
        int[] rStickCoordEnd = new int[2];
        rStickCoordEnd[0]  = stickCoordEnd[0];
        rStickCoordEnd[1] = stickCoordEnd[1];
        return rStickCoordEnd;
    }

    public void setStickCoordEnd(int x, int y){
        stickCoordEnd[0] = x;
        stickCoordEnd[1] = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Stick other = (Stick) obj;
        if (stickCoordBeg[0] != other.stickCoordBeg[0])
            return false;
        if (stickCoordBeg[1] != other.stickCoordBeg[1])
            return false;
        if (stickCoordEnd[0] != other.stickCoordEnd[0])
            return false;
        if (stickCoordEnd[1] != other.stickCoordEnd[1])
            return false;
        return true;
    }
}
