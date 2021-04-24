package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;

import java.util.Objects;

public abstract class BreakableTool<A extends Actor> extends AbstractActor implements Usable<A>{
    private int remainingUses;

    //constructor
    public BreakableTool(int remainingUses){
        this.remainingUses = remainingUses;
    }

    //method to remove object if there are no more uses

    @Override
    public void useWith(A actor){
        if(remainingUses > 0) this.remainingUses --;
        if (this.remainingUses <= 0) Objects.requireNonNull(getScene()).removeActor(this);
    }

    public void setRemainingUses(int remainingUses) {
        this.remainingUses = remainingUses;
    }
    public int getRemainingUses(){return this.remainingUses;}
}
