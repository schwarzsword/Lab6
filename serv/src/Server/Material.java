package Server;

import java.awt.*;
import java.io.Serializable;

public enum Material implements Serializable {
    OAK(149,95,32),
    BIRCH(215,215,215),
    ELM(109,59,42),
    LINDEN(89,53,31),
    PALM(111,79,40),
    PINE(89,35,33);
    Material(int r, int g, int b){
        this.col = new Color(r,g,b);
    }
     Color col;
    public Color getColor(){
        return col;
    }
}
