package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;

public class LurkerAlien extends Alien implements Enemy {

    private final Animation lurkerBorn;
    private final Animation lurkerIdle;

    public LurkerAlien(){
        super(50, new RandomlyMoving());
        setSpeed(2);
        setDealDamage(20);

        lurkerBorn = new Animation(
            "sprites/lurker_born.png",
            32,
            32,
            0.1f,
            Animation.PlayMode.ONCE
        );
        lurkerIdle = new Animation(
            "sprites/lurker_alien.png",
            32,
            32,
            0.1f,
            Animation.PlayMode.LOOP_PINGPONG
        );
        lurkerBorn.pause();
        setAnimation(lurkerBorn);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        lurkerBorn();
    }


    private void lurkerBorn(){
        new ActionSequence<>(
            new Invoke<Actor>(lurkerBorn::play),
            new Wait<>(1),
            new Invoke<Actor>(()-> setAnimation(lurkerIdle))
        ).scheduleFor(this);
    }
}
