package sk.tuke.kpi.oop.game.weapons;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.actions.Move;
import sk.tuke.kpi.oop.game.actions.Take;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Enemy;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.items.Usable;

public class Rocket extends AbstractActor implements Usable<Ripley>, Fireable, Collectible {

    private final Animation explosion;
    private final Animation rocketLaunched;
    private boolean isOnGround;
    private Disposable move;

    public Rocket(){
        Animation rocketAnimation = new Animation(
            "sprites/rocket.png"
        );

        rocketLaunched = new Animation(
            "sprites/rocket_launched.png"
        );

        explosion = new Animation(
            "sprites/large_explosion.png",
            32,
            32,
            0.1f,
            Animation.PlayMode.ONCE
        );

        setAnimation(rocketAnimation);
        isOnGround = true;
    }



    @Override
    public void useWith(Ripley actor) {
        Scene scene = actor.getScene();
        if(scene == null) return;

        if(!isOnGround){
            //getting position to place bullet
            int posX = actor.getPosX() + 8;
            int posY = actor.getPosY() + 8;

            //place bullet witch started moved
            scene.addActor(this, posX, posY);
            setAnimation(rocketLaunched);
            this.getAnimation().setRotation(actor.getAnimation().getRotation());
            move = new Move<>(
                Direction.fromAngle(actor.getAnimation().getRotation()),
                100
            ).scheduleFor(this);

            new Loop<>(
                new ActionSequence<>(
                    new Wait<>(0.3f),
                    new Invoke<>(()-> hit(scene))
                )
            ).scheduleFor(this);

            actor.getBackpack().remove(this);
        }
        else{
            new Take<>().scheduleFor(actor);
            isOnGround = false;
        }
    }

    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }

    private void hit(Scene scene){
        Alive enemy = (Alive) scene.getActors().stream()
            .filter(actor -> actor instanceof Enemy && actor.intersects(this))
            .findFirst().orElse(null);

        if (enemy != null){
            enemy.getHealth().drain(100);
            this.collidedWithWall();
        }
    }


    @Override
    public void collidedWithWall() {
        Scene scene = getScene();
        if(scene != null){
            this.stoppedMoving();
            new ActionSequence<>(
                new Invoke<>(()-> setAnimation(explosion)),
                new Wait<>(1.5f),
                new Invoke<>(()-> scene.removeActor(this))
            ).scheduleFor(this);
        }
    }

    @Override
    public void stoppedMoving() {
        move.dispose();
    }

    @Override
    public int getSpeed() {
        return 2;
    }
}
