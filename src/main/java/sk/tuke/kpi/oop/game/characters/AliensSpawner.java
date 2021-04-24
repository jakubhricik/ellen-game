package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.oop.game.factory.Spawner;
import sk.tuke.kpi.oop.game.puzzle.Subscriber;

public class AliensSpawner extends AbstractActor implements Subscriber {

    private int numberOfAliens;
    private String typeOfAlien;
    private final Animation spawnerAnimation;
    private Disposable spawning;


    /**
     * It will spawn some type of alien and some num of aliens
     * @param typeOfAlien which type of alien you want to spawn
     * @param numberOfAliens how many aliens you want to spawn
     */
    public AliensSpawner(String typeOfAlien, int numberOfAliens){
        this.numberOfAliens = numberOfAliens;
        this.typeOfAlien = typeOfAlien;
        spawnerAnimation = new Animation(
            "sprites/tunnel_black.png",
            32,
            32,
            0.1f,
            Animation.PlayMode.ONCE
        );
        spawnerAnimation.setTint(Color.GRAY);
        spawnerAnimation.pause();
        setAnimation(spawnerAnimation);
    }


    /**
     * using spawner factory to spawn alien you want to
     */
    private void spawnAlien(){
        if(getTypeOfAlien() != null && this.getScene() != null){
            Enemy alien = (Enemy) new Spawner().create(getTypeOfAlien());
            if(alien != null){
                this.getScene().addActor(alien, this.getPosX(), this.getPosY());
            }
        }
    }


    private void updateAnimation(){
        if(spawnerAnimation.getCurrentFrameIndex() == spawnerAnimation.getFrameCount()-1){
            spawnerAnimation.resetToFirstFrame();
            spawnerAnimation.setPlayMode(Animation.PlayMode.ONCE_REVERSED);
        }else{
            spawnerAnimation.resetToFirstFrame();
            spawnerAnimation.setPlayMode(Animation.PlayMode.ONCE);
        }
        spawnerAnimation.play();
    }


    @Override
    public void update() {
        //spawning alien every 6 second until there are enough of aliens
        spawning = new Loop<>(
            new ActionSequence<>(
                new Invoke<>(this::updateAnimation),
                new Wait<>(1),
                new Invoke<>(this::spawnAlien),
                new Invoke<>(this::updateAnimation),
                new Wait<>(5),
                new Invoke<>(this::isDone)
            )
        ).scheduleFor(this);
    }

    private void isDone(){
        numberOfAliens--;
        if(numberOfAliens == 0){
            spawning.dispose();
        }
    }

    public String getTypeOfAlien(){
        return this.typeOfAlien;
    }

}
