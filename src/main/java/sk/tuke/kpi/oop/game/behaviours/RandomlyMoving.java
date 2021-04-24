package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

import java.util.Random;

public class RandomlyMoving implements Behaviour<Movable>{

    private Disposable movement;

    /**
     * using recursion to make random movement
     * @param actor is movable actor who wil start randomly moving
     */
    @Override
    public void setUp(Movable actor) {
        if (actor == null) return;
        if(movement != null) movement.dispose();

        Direction direction = (Direction.values())[(new Random()).nextInt(8)];

        movement = new ActionSequence<>(
            new Invoke<>(()-> actor.getAnimation().setRotation(direction.getAngle())),
            new Move<>(direction, 2),
            new Invoke<>(() -> (new RandomlyMoving()).setUp(actor))
        ).scheduleFor(actor);
    }
}
