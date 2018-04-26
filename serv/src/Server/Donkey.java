package Server;

public class Donkey extends Animal implements Looker, Signer{
    public Donkey(String name, int x, int y){
        this.setName(name);
        this.setCoords(x,y);
    }

    @Override
    public void lookAt(String thing){
        System.out.println(this.getName() +" looked itently at "+ thing + ", lied on the earth");
    }

    @Override
    public void raiseLeg(){
        LegCondition leg = LegCondition.UP;
        System.out.println(this.getName() + " raised his leg");
    }

    @Override
    public void shakeLeg(String actionName, Walker walker){
        if (leg == LegCondition.UP && actionName.equals("to")) {
            walker.comeTo(this.getCoords()[0], this.getCoords()[1]);
            System.out.println(this.getName() + " shaked it him to come closer");
        }
        else if (leg == LegCondition.UP && actionName.equals("from")){
            walker.comeFrom(this.getCoords()[0], this.getCoords()[1],5,5);
            System.out.println(this.getName() + " shaked it him to go away");
        }
        else if (leg == LegCondition.DOWN){
            System.out.println("Leg was down and he cant shake it");
        }
    }
}
