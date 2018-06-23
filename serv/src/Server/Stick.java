package Server;


import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.util.Objects.hash;

public class Stick implements Serializable {

    Stick(String sN, int bx, int by, int ex, int ey, Material mat, String s){
        setStickName(sN);
        setStickCoordBeg(bx, by);
        setStickCoordEnd(ex, ey);
        setStickLength();
        setMaterial(mat);
        initdate = ZonedDateTime.now();
    }
    Stick(String sN, int bx, int by, int ex, int ey, Material mat,  ZonedDateTime dateTime){
        setStickName(sN);
        setStickCoordBeg(bx, by);
        setStickCoordEnd(ex, ey);
        setStickLength();
        setMaterial(mat);
        initdate = dateTime;
    }
    private String stickName;
    public int coorbegx;
    public int coorbegy;
    public int coorendx;
    public int coorendy;
    private int stickLength;
    public ZonedDateTime initdate;
    Material material;


    public String getInit(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String s = formatter.format(initdate);
        return s;
    }

    public void setMaterial(Material mat){        this.material=mat;    }

    public Material getMaterial() {        return material;    }


    public void setStickCoordEnd(int x, int y) {
        coorendx = x;
        coorendy = y;

    }


    public void setStickCoordBeg(int x, int y) {
        coorbegx = x;
        coorbegy = y;
    }

    public int getStickLength() {return stickLength; }

    public void setStickLength() {
        this.stickLength = (int)Math.sqrt(Math.pow(coorbegx-coorendx,2)+Math.pow(coorbegy-coorendy,2));
    }

    public String getStickName() { return stickName;}

    public void setStickName(String stickName) { this.stickName = stickName;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stick)) return false;
        Stick stick = (Stick) o;
        return getStickLength() == stick.getStickLength() &&
                Objects.equals(getStickName(), stick.getStickName()) &&
                Objects.equals(coorbegx, stick.coorbegx) &&
                Objects.equals(coorbegy, stick.coorbegy) &&
                Objects.equals(coorendx, stick.coorendx) &&
                Objects.equals(coorendy, stick.coorendy) &&
                getMaterial() == stick.getMaterial();
    }

    @Override
    public int hashCode() {return hash(getStickName(),getMaterial(),getStickLength(),getInit());}

    @Override
    public String toString() {return  "Name:"+stickName +"; Len:"+stickLength+"; Init:"+ initdate;}


}
