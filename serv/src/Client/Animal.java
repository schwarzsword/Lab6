package Client;

abstract class Animal{
    private String name;
    private int[] Coords = new int[2];

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int[] getCoords(){
        int[] rCoords = new int[2];
        rCoords[0]  = Coords[0];
        rCoords[1] = Coords[1];
        return rCoords;
    }

    public void setCoords(int x, int y){
        Coords[0] = x;
        Coords[1] = y;
    }

}





