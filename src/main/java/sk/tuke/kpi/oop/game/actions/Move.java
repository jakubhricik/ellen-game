package sk.tuke.kpi.oop.game.actions;

import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.gamelib.graphics.Point;
import sk.tuke.kpi.gamelib.map.SceneMap;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;

import java.util.Objects;


public class Move <A extends Movable> implements Action<A> {

    private A actor;
    private Direction direction;
    private float duration;
    private final Direction directionSave;
    private final float durationSave;
    private boolean isDone;
    private boolean isMoving;

    /**
     * Constructor without duration, duration is sett to 1
     * @param direction direction from Direction enum
     */
    public Move(Direction direction) {
        this(direction, 1);
        isDone = false;
        isMoving = false;
    }

    /**
     * Constructor with both params
     * @param direction direction from Direction Enum
     * @param duration time of executing
     */
    public Move(Direction direction, float duration) {
        this.directionSave = direction;
        this.direction = direction;
        this.durationSave = duration;
        this.duration = 0;
        isDone = false;
        isMoving= false;
    }



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
    public void execute(float deltaTime) {
        //checking actor
        if(actor == null) {
            isDone = true;
            return;
        }
        //save old position
        Point oldPosition = actor.getPosition();
        SceneMap map = Objects.requireNonNull(actor.getScene()).getMap();

        //checking direction
        if(!direction.name().equals("NONE")){
            //start moving
            if(!isMoving){
                actor.startedMoving(direction);
                isMoving = true;
            }
            //calculate move
            actor.setPosition(
                actor.getPosX() + direction.getDx() * actor.getSpeed(),
                actor.getPosY() + direction.getDy() * actor.getSpeed()
            );
            //check if intersect with wall
            if(map.intersectsWithWall(actor)){
                actor.setPosition(oldPosition.getX(), oldPosition.getY());
                actor.collidedWithWall();
            }
        }

        //calculate duration of move
        this.duration += deltaTime;
        if(Math.abs(durationSave) - Math.abs(duration) <= 1e-5 && !isDone){
            stop();
        }
    }


    /**
     * it stops execute by setting isDont to true
     */
    public void stop(){
        isDone = true;
        if(actor != null) actor.stoppedMoving();
    }

    // setting direction
    public void setDirection(Direction direction){
        this.direction = direction;
    }


    //reset to starting point
    @Override
    public void reset() {
        this.direction = this.directionSave;
        this.duration = 0;
        isDone = false;
        isMoving = false;
    }
}
