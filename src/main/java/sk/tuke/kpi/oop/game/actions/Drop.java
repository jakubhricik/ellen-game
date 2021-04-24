package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.items.Collectible;


public class Drop<K extends Keeper> extends AbstractAction<K> {


    @Override
    public void execute(float deltaTime) {

        //check if actor is not null
        Keeper actor = getActor();
        if(actor == null) {
            setDone(true);
            return;
        }
        //getting actors positions
        int posX = actor.getPosX();
        int posY = actor.getPosY();

        //getting backpack
        Backpack backpack = actor.getBackpack();
        if(backpack == null) return;

        //peeking some item
        Collectible item = backpack.peek();
        if(item == null || actor.getScene() == null) return;

        //place the item in world on actors position
        actor.getScene().addActor(
            item,
            posX + ((getActor().getWidth() >> 1) >> 1),
            posY + ((getActor().getWidth() >> 1) >> 1)
        );

        //removing item from backpack
        backpack.remove(item);
        setDone(true);
    }
}
