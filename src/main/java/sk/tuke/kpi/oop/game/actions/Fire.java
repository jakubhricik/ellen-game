package sk.tuke.kpi.oop.game.actions;


import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.weapons.Fireable;
import sk.tuke.kpi.oop.game.weapons.Firearm;


public class Fire<A extends Armed> extends AbstractAction<A> {

    //parameters
    private A actor;
    private boolean isDone = false;


    @Override
    public void execute(float deltaTime) {
        //check actor
        if(actor == null) {
            setDone(true);
            return;
        }

        //check some weapon
        Firearm firearm = actor.getFirearm();
        if(firearm == null){
            setDone(true);
            return;
        }

        //checking ammo in weapon
        if(firearm.getAmmo() > 0){
            //getting bullet
            Fireable bullet = actor.getFirearm().fire();
            Scene scene = actor.getScene();
            if(bullet == null || scene == null){
                isDone = true;
                return;
            }
            //getting position to place bullet
            int posX = actor.getPosX() + 8;
            int posY = actor.getPosY() + 8;

            //place bullet witch started moved
            scene.addActor(bullet, posX, posY);
            new Move<Fireable>(
                Direction.fromAngle(actor.getAnimation().getRotation()),
                bullet.getSpeed()
            ).scheduleFor(bullet);
        }
        setDone(true);
    }


    //getters and setters:
    @Override
    public @Nullable A getActor() {
        return actor;
    }

    @Override
    public void setActor(@Nullable A actor) {
        this.actor = actor;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    protected void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    @Override
    public void reset() {
        setDone(false);
    }
}
