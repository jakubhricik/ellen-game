package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.factory.Spawner;


public class LurkerSpawner extends AbstractActor {


    private Disposable spawned;
    private final Animation lurkerSpawnerAnimation;

    public LurkerSpawner(){
         lurkerSpawnerAnimation = new Animation(
            "sprites/alien_egg.png",
            32,
            32,
            0.2f,
            Animation.PlayMode.ONCE
        );
        lurkerSpawnerAnimation.pause();
        setAnimation(lurkerSpawnerAnimation);
    }


    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        spawned = new Loop<>(
            new ActionSequence<>(
                new Invoke<>(()-> checkForInteract(scene)),
                new Wait<>(1)
            )
        ).scheduleFor(this);
    }

    private void checkForInteract(Scene scene){
        scene.getActors().stream()
            .filter(actor -> actor.intersects(this) && actor != this)
            .findFirst().ifPresent(actor -> {
            spawnLurker(scene);
            spawned.dispose();
        });

    }

    private void spawnLurker(Scene scene){
        Actor alien = new Spawner().create("lurker alien");
        if(alien != null){
            lurkerSpawnerAnimation.play();
            scene.addActor(alien, this.getPosX(), this.getPosY());
        }
    }
}
