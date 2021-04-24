package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MovableController  implements KeyboardListener {

    private final Movable actor;

    private final Map<Input.Key, Direction> keyDirectionMap = Map.ofEntries(
        Map.entry(Input.Key.UP, Direction.NORTH),
        Map.entry(Input.Key.DOWN, Direction.SOUTH),
        Map.entry(Input.Key.LEFT, Direction.WEST),
        Map.entry(Input.Key.RIGHT, Direction.EAST)
    );

    private final Set<Input.Key> setOfCombinations = new HashSet<>();
    private Move<Movable> move;

    /**
     * Constructor of MovableController
     * @param actor Type Movable
     */
    public MovableController(Movable actor){
        this.actor = actor;
    }

    /**
     * Checking if key is pressed, then make move in correct direction to the key
     * @param key is input key from keyboard
     */
    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if(keyDirectionMap.containsKey(key)){
            //if it starting to move or set is empty than move in direction that the key is mapped
            if(move == null && setOfCombinations.size()<=1){
                updateDirection(keyDirectionMap.get(key));
            }
            //if there are more keys pressed then combine the directions
            Direction direction = keyDirectionMap.get(key);
            for (Input.Key ky:setOfCombinations) {
                direction = direction.combine(keyDirectionMap.get(ky));
            }
            //and update the move to the final direction
            updateDirection(direction);
            //added pressed key to the set
            setOfCombinations.add(key);
        }
    }

    /**
     * Checking if key is released, then it remove key from set if it was pressed
     * @param key is input key from keyboard
     */
    @Override
    public void keyReleased(@NotNull Input.Key key) {
        if(keyDirectionMap.containsKey(key)){
            //remove key from the set
            setOfCombinations.remove(key);
            //if set is empty the move is stopped
            if(setOfCombinations.isEmpty()){
                move.stop();
            }
            else if (setOfCombinations.size() == 1){
                //if in set is 1 element after key was released then update move to this direction
                for (Input.Key ky:setOfCombinations) {
                    updateDirection(keyDirectionMap.get(ky));
                }
            }
        }
    }

    /**
     * Update "move" to correct direction
     * @param direction param of specific direction where the movable is rotated
     */
    private void updateDirection(Direction direction){
        //if move is not null than stop move
        if(move != null){
            move.stop();
        }
        //update move with new direction
        move = new Move<>(direction, Float.MAX_VALUE);
        move.scheduleFor(actor);
    }
}
