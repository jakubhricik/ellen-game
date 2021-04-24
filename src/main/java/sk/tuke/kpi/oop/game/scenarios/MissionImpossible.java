package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Locker;
import sk.tuke.kpi.oop.game.Ventilator;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.LockedDoor;



public class MissionImpossible implements SceneListener {

    private Disposable killingRipley = null;



    /**
     * trieda factory vytvara akterov podla mena
     *
     * je vyuzuta v main, pri inicializovani sveta
     */
    public static class Factory implements ActorFactory{

        /**
         * Create an object based on name or type
         *
         * @param type type of object we wanted
         * @param name name of object we wanted
         * @return returning the object based on name and type
         */
        @Override
        public @Nullable Actor create(@Nullable String type, @Nullable String name) {
            if(name == null) return null;
            switch (name){
                case "ellen" : return new Ripley();
                case "energy" : return new Energy();
                case "door" : return new LockedDoor(name, Door.Orientation.VERTICAL);
                case "access card" : return new AccessCard();
                case "locker" : return new Locker();
                case "ventilator" : return new Ventilator();
                default: return null;
            }
        }
    }

    @Override
    public void sceneInitialized(@NotNull Scene scene) {

        Ripley player = (Ripley) scene.getFirstActorByName("Ellen");

        //controlling keeper actions (drop, take, shift)
        KeeperController keeperController = new KeeperController(player);
        Disposable kpControl = scene.getInput().registerListener(keeperController);

        //controlling player movement
        MovableController controller = new  MovableController(player);
        Disposable playerControl = scene.getInput().registerListener(controller);

            //if dor are opened, poison start killing player
        scene.getMessageBus().subscribe(Door.DOOR_OPENED, door-> killPlayer(player));
            //if ventilator is started until player is alive, the poison damage is gone
        scene.getMessageBus().subscribe(Ventilator.VENTILATOR_REPAIRED, ventilator -> killingRipley.dispose());
            //else if player is dead the poison have no mor damage, and movement is also disposed
        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED,
            ripley -> {
            kpControl.dispose();
            playerControl.dispose();
            killingRipley.dispose();
        });
    }


    /**
     * start counting and taking 1 HP every second
     *
     * @param player Reference to Ripley object,
     *               main character of player
     */
    private void killPlayer(Ripley player){
        if(killingRipley != null) return;
        killingRipley = new Loop<>( //toto je disposable
            new ActionSequence<>(
                new Invoke<Actor>(()-> player.getHealth().drain(1)),
                new Wait<>(1)
            )
        ).scheduleFor(player);
    }


    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        Ripley player = (Ripley) scene.getFirstActorByName("Ellen");
        assert player != null;

        player.showRipleyState();
        scene.follow(player);

    }
}
