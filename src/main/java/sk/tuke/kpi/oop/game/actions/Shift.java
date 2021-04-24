package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Backpack;

public class Shift<K extends Keeper> extends AbstractAction<K> {

    private boolean isDone = false;

    @Override
    public void execute(float deltaTime) {
        //checking actor
        if(getActor() == null){
            setDone(true);
            return;
        }
        //checking backpack
        Backpack backpack = getActor().getBackpack();
        if(backpack == null){
            setDone(true);
            return;
        }
        //shift in backpack
        backpack.shift();
        setDone(true);
    }


    @Override
    public boolean isDone() {
        return this.isDone;
    }

    @Override
    protected void setDone(boolean isDone) {
        this.isDone = isDone;
    }
}
