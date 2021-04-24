package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Game;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.items.Collectible;


public class Take<K extends Keeper> extends AbstractAction<K> {

    private boolean isDone = false;

    @Override
    public void execute(float deltaTime) {
        //checking actor
        Keeper actor = getActor();
        if(actor == null) {
            setDone(true);
            return;
        }
        //checking scene
        Scene scene = actor.getScene();
        if(scene == null) {
            setDone(true);
            return;
        }

        //finding collectible item in intersection with actor
        Collectible item = (Collectible) scene
            .getActors()
            .stream()
            .filter(i -> i instanceof Collectible && i.intersects(actor))
            .findFirst()
        .orElse(null);

        //checking if there is any item
        if(item == null){
            setDone(true);
            return;
        }

        //make params of backpack and items scene
        Scene itemScene = item.getScene();
        Backpack backpack = actor.getBackpack();

        //checking backpack and scene
        if(backpack != null && itemScene != null){
            //trying to ad item into backpack
            try{
                backpack.add(item);
                itemScene.removeActor(item);
            //or print some message on overlay
            }catch(IllegalStateException exception){
                Game game = scene.getGame();
                int windowHeight = game.getWindowSetup().getHeight();
                int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
                scene.getGame().getOverlay().drawText(exception.getMessage(), 20, yTextPos - 100 );
            }
        }
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
