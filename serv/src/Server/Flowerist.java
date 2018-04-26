package Server;

public interface Flowerist{
    void cutFlower(Flowers f);
    void toSniff(Flowers f);
    static enum Flowers{
        VIOLET,
        ROSE,
        CHAMOMILE;
    }
}