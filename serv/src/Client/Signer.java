package Client;

public interface Signer{
    void raiseLeg();
    void shakeLeg(String actionName, Walker walker);
    enum LegCondition{
        UP,
        DOWN
    }
    LegCondition leg = LegCondition.UP;
}

