package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;


public class Alien extends AbstractActor implements Movable, Enemy, Alive {

    private Health health;
    private int speed;
    private int dealDamage;
    private Behaviour<? super Alien> behaviour;

    public Alien(){
        Animation alienAnimation = new Animation(
            "sprites/alien.png",
            32,
            32,
            0.1f,
            Animation.PlayMode.LOOP_PINGPONG
        );
        setAnimation(alienAnimation);
        health = new Health(100);
        speed = 1;
        dealDamage = 10;
    }

    public Alien(int healthValue, Behaviour<? super Alien> behaviour){
        this();
        this.behaviour = behaviour;
        health = new Health(healthValue);
    }


    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        //setting up behaviour
        if(behaviour != null){
            behaviour.setUp(this);
        }

        //new loop to hit any alive that is intersecting with alien
        new Loop<>(
            new ActionSequence<>(
                new Invoke<>(() -> this.hitAlive(scene)),
                new Wait<>(1)
            )
        ).scheduleFor(this);

        //schedule what will happened when alien died
        getHealth().onExhaustion(()-> {
            scene.cancelActions(this);
            scene.removeActor(this);
        });
    }

    /**
     * When intersecting with any alive it deal damage to them
     * @param scene param of Scene
     */
    private void hitAlive(Scene scene){
        Alive alive = (Alive) scene.getActors().stream()
            .filter(actor -> actor instanceof Alive && !(actor instanceof Enemy) && actor.intersects(this))
            .findFirst().orElse(null);
        if(alive != null){
            alive.getHealth().drain(getDealDamage());
        }
    }


    @Override
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public int getDealDamage() {
        return dealDamage;
    }

    public void setDealDamage(int newDamage){
        this.dealDamage = newDamage;
    }

    @Override
    public Health getHealth() {
        return health;
    }
}
