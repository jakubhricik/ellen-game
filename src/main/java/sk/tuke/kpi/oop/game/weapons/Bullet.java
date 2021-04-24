package sk.tuke.kpi.oop.game.weapons;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.characters.Ripley;

public class Bullet extends AbstractActor implements Fireable{

    private final int bulletSpeed;
    private final Animation bulletAnimation;

    public Bullet(){
        bulletAnimation = new Animation(
            "sprites/bullet.png",
            16,
            16
        );
        setAnimation(bulletAnimation);
        bulletSpeed = 4;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        Ripley ripley = (Ripley) scene.getFirstActorByName("Ellen");
        if(ripley != null){
            bulletAnimation.setRotation(ripley.getAnimation().getRotation());
        }

        new Loop<>(
            new Invoke<>(()-> hitPlayer(scene))
        ).scheduleFor(this);

    }

    private void hitPlayer(Scene scene){
        Alive alive = (Alive) scene.getActors().stream()
            .filter(actor -> actor instanceof Alive && actor.intersects(this))
            .findFirst().orElse(null);

        if(!(alive instanceof Armed) && alive != null){
            alive.getHealth().drain(10);
            scene.removeActor(this);
        }
        else if ( alive != null && !((Armed) alive).getFirearm().getBullets().contains(this)){
            alive.getHealth().drain(10);
            scene.removeActor(this);
        }

    }

    @Override
    public void collidedWithWall() {
        Scene scene = getScene();
        if(scene != null){
            this.stoppedMoving();
            scene.removeActor(this);
        }
    }

    @Override
    public int getSpeed() {
        return bulletSpeed;
    }
}
