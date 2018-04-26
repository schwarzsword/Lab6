package Server;

public class Pig extends Animal implements Walker{
    Pig(String name, int x, int y){
        this.setName(name);
        this.setCoords(x,y);
    }
    @Override
    public void comeTo(int x, int y){
        this.setCoords(x,y);
        System.out.println(this.getName() + " approached to see what happens");
    }

    @Override
    public void comeFrom(int x, int y, int dx, int dy){
        int[] rCoords = this.getCoords();
        int detX = rCoords[0] - x;
        int detY = rCoords[1] - y;
        if  (detX > 0 && detY > 0 ){
            this.setCoords(rCoords[0] + dx, rCoords[1] + dy);
        }
        else if (detX > 0 && detY < 0 ){
            this.setCoords(rCoords[0]+dx, rCoords[1]-dy);}
        else if (detX < 0 && detY > 0){
            this.setCoords(rCoords[0] - dx, rCoords[1] + dy);        }
        else{
            this.setCoords(rCoords[0] - dx, rCoords[1] - dy);        }
    }

}
