package Server;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public class Stick implements Serializable {
    Stick(String sN, int bx, int by, int ex, int ey, Material mat){
        setStickName(sN);
        setStickCoordBeg(bx, by);
        setStickCoordEnd(ex, ey);
        setStickLength();
        setMaterial(mat);
    }
    private String stickName;
    private Point stickCoordBeg = new Point();
    private Point stickCoordEnd = new Point();
    private int stickLength;
    Material material;

    public void setMaterial(Material mat){
        this.material=mat;
    }

    public Material getMaterial() {
        return material;
    }

    public Point getStickCoordEnd() {
        return stickCoordEnd;
    }

    public void setStickCoordEnd(int x, int y) {
        this.stickCoordEnd.x = x;
        this.stickCoordEnd.y = y;

    }

    public Point getStickCoordBeg() {
        return stickCoordBeg;
    }

    public void setStickCoordBeg(int x, int y) {
        this.stickCoordBeg.x = x;
        this.stickCoordBeg.y = y;
    }




    public int getStickLength() {
        return stickLength;
    }

    public void setStickLength() {
        this.stickLength = (int)Math.sqrt(Math.pow(getStickCoordBeg().getX()-getStickCoordEnd().getX(),2)+Math.pow(getStickCoordBeg().getY()-getStickCoordEnd().getY(),2));
    }

    public String getStickName() {
        return stickName;
    }

    public void setStickName(String stickName) {
        this.stickName = stickName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stick)) return false;
        Stick stick = (Stick) o;
        return getStickLength() == stick.getStickLength() &&
                Objects.equals(getStickName(), stick.getStickName()) &&
                Objects.equals(getStickCoordBeg(), stick.getStickCoordBeg()) &&
                Objects.equals(getStickCoordEnd(), stick.getStickCoordEnd()) &&
                getMaterial() == stick.getMaterial();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getStickName(), getStickCoordBeg(), getStickCoordEnd(), getStickLength(), getMaterial());
    }

    @Override
    public String toString() {
        return  stickName +
                ": (" + stickCoordBeg.x +"; "+ stickCoordBeg.y +
                "); (" + stickCoordEnd.x +"; "+ stickCoordEnd.y +
                "), stickLength=" + stickLength +
                ", material=" + material;
    }
}
