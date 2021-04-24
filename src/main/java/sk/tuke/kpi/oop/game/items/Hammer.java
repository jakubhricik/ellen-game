package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Repairable;

public class Hammer extends BreakableTool<Repairable> implements Collectible{
    public Hammer(){
        super(1);
        Animation normalAnimation = new Animation("sprites/hammer.png");
        setAnimation(normalAnimation);
    }

    @Override
    public void useWith(Repairable actor) {
        if(actor != null && actor.repair()){
            super.useWith(actor);
        }
    }

    @Override
    public Class<Repairable> getUsingActorClass() {
        return Repairable.class;
    }
}
