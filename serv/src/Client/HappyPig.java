package Client;

public class HappyPig extends Pig implements Flowerist, Walker{
    public HappyPig(String name, int x, int y){
        super(name, x, y);
    }
    private boolean happiness = false;
    Flowers tinyFlower = Flowers.CHAMOMILE;
    @Override
    public void cutFlower(Flowers f) {
        tinyFlower = f;
        System.out.println(this.getName() + " has cutted some violets");
    }
    @Override
    public void toSniff(Flowers f){
        if( f == Flowers.VIOLET){
            happiness = true;
        }
        else happiness = false;
        System.out.println(this.getName() + " sniffled violets and felt very happy");
    }
}

