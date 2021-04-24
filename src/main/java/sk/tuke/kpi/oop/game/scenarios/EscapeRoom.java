package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.oop.game.behaviours.Observing;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.AlienMother;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.items.Ammo;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.openables.Door;

import java.util.HashMap;
import java.util.Map;


public class EscapeRoom implements SceneListener {

    /**
     * trieda factory vytvara akterov podla mena
     * je vyuzuta v main, pri vytvarani sveta
     */
    public static class Factory implements ActorFactory {

        private final Map<String, Actor> objectsMap = new HashMap<>();

        /**
         * Create an object based on name or type
         *
         * @param type type of object we wanted
         * @param name name of object we wanted
         * @return returning the object based on name and type
         */
        @Override
        public @Nullable Actor create(@Nullable String type, @Nullable String name) {
            objectsMap.put("ellen ripley", new Ripley());
            objectsMap.put("energy", new Energy());
            objectsMap.put("ammo", new Ammo());
            objectsMap.put("alien running", new Alien(100, new RandomlyMoving()));
            objectsMap.put("alien waiting1", new Alien(100, new Observing<>(Door.DOOR_OPENED, door -> door.getName().equals("front door"), new RandomlyMoving())));
            objectsMap.put("alien waiting2", new Alien(100, new Observing<>(Door.DOOR_OPENED, door -> door.getName().equals("back door"), new RandomlyMoving())));
            objectsMap.put("alien mother waiting2", new AlienMother(new Observing<>(Door.DOOR_OPENED, door -> door.getName().equals("back door"), new RandomlyMoving())));
            objectsMap.put("front door vertical", new Door(name, Door.Orientation.VERTICAL));
            objectsMap.put("back door vertical", new Door(name, Door.Orientation.VERTICAL));
            objectsMap.put("exit door vertical", new Door(name, Door.Orientation.VERTICAL));
            objectsMap.put("front door horizontal", new Door(name, Door.Orientation.HORIZONTAL));
            objectsMap.put("back door horizontal", new Door(name, Door.Orientation.HORIZONTAL));
            objectsMap.put("exit door horizontal", new Door(name, Door.Orientation.HORIZONTAL));

            if (name == null && type == null) return null;
            else if(name != null && type == null) return objectsMap.get(name);
            else return objectsMap.get(name + " " + type);
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

        ShooterController shooterController = new ShooterController(player);
        scene.getInput().registerListener(shooterController);

        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED,
            ripley -> {
                kpControl.dispose();
                playerControl.dispose();
            }
        );

    }


    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        Ripley player = (Ripley) scene.getFirstActorByName("Ellen");
        assert player != null;

        player.showRipleyState();
        scene.follow(player);
    }
}
