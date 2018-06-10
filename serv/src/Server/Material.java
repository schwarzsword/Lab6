package Server;

import java.awt.*;
import java.io.Serializable;

public enum Material implements Serializable {
    OAK(149,95,32,255),
    BIRCH(215,215,215,255),
    ELM(109,59,42,255),
    LINDEN(89,53,31,255),
    PALM(111,79,40,255),
    PINE(89,35,33,255),
    REDTREE(100,20,4,255),
    IRONTREE(50,20,20,255);
    Material(int r, int g, int b, int a){
        this.col = new Color(r,g,b,a);
    }
     Color col;
    public Color getColor(){
        return col;
    }
}
