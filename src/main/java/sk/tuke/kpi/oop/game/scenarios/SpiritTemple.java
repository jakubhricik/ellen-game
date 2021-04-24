package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.map.MapMarker;
import sk.tuke.kpi.oop.game.*;
import sk.tuke.kpi.oop.game.characters.AliensSpawner;
import sk.tuke.kpi.oop.game.characters.LurkerSpawner;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.items.*;
import sk.tuke.kpi.oop.game.items.termilans.HintTerminal;
import sk.tuke.kpi.oop.game.items.termilans.StrongDoorTerminal;
import sk.tuke.kpi.oop.game.openables.*;
import sk.tuke.kpi.oop.game.popup.*;
import sk.tuke.kpi.oop.game.puzzle.buttons.Button;
import sk.tuke.kpi.oop.game.puzzle.buttons.SecretLocker;
import sk.tuke.kpi.oop.game.scenarios.rooms.MaintenanceRoom;
import sk.tuke.kpi.oop.game.scenarios.rooms.ReactorHall;
import sk.tuke.kpi.oop.game.weapons.Rocket;

import java.util.HashMap;
import java.util.Map;

public class SpiritTemple implements SceneListener {

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
            objectsMap.put("locker", new Locker());
            objectsMap.put("locker secret", new SecretLocker("access card"));
            objectsMap.put("terminal strong doors", new StrongDoorTerminal());
            objectsMap.put("terminal hint", new HintTerminal("You need to solve sam button problem"));
            objectsMap.put("strong door vertical", new StrongDoor(name, Door.Orientation.VERTICAL));
            objectsMap.put("strong door horizontal", new StrongDoor(name, Door.Orientation.HORIZONTAL));
            objectsMap.put("locked door vertical", new LockedDoor(name, Door.Orientation.VERTICAL));
            objectsMap.put("locked door horizontal", new LockedDoor(name, Door.Orientation.HORIZONTAL));
            objectsMap.put("door vertical", new Door(name, Door.Orientation.VERTICAL));
            objectsMap.put("door horizontal", new Door(name, Door.Orientation.HORIZONTAL));
            objectsMap.put("button 1", new Button(1));
            objectsMap.put("button 2", new Button(2));
            objectsMap.put("button 3", new Button(3));
            objectsMap.put("reactor broken", new Reactor(100));
            objectsMap.put("reactor off", new Reactor());
            objectsMap.put("extinguisher", new FireExtinguisher());
            objectsMap.put("body "+ type , new Body(type));
            objectsMap.put("access card", new AccessCard());
            objectsMap.put("switch", new PowerSwitch());
            objectsMap.put("spawner "+type, new AliensSpawner(type, 10));
            objectsMap.put("exit door vertical", new Door(name, Door.Orientation.VERTICAL));
            objectsMap.put("spawner lurker", new LurkerSpawner());
            objectsMap.put("rocket", new Rocket());
            objectsMap.put("wall breakable", new BreakableWall());

            if (name == null && type == null) return null;
            else if(name != null && type == null) return objectsMap.get(name);
            else return objectsMap.get(name + " " + type);
        }
    }

    @Override
    public void sceneInitialized(@NotNull Scene scene) {

        ReactorHall hall = new ReactorHall(scene);
        hall.setUp();

        MaintenanceRoom maintenanceRoom = new MaintenanceRoom(scene);
        maintenanceRoom.setUp();

        Map<String, MapMarker> markers = scene.getMap().getMarkers();

        MapMarker ellen = markers.get("ellen");
        Ripley  player = new Ripley();
        scene.addActor(player, ellen.getPosX(), ellen.getPosY());



        //controlling keeper actions (drop, take, shift)
        KeeperController keeperController = new KeeperController(player);
        Disposable kpControl = scene.getInput().registerListener(keeperController);

        //controlling player movement
        MovableController controller = new  MovableController(player);
        Disposable playerControl = scene.getInput().registerListener(controller);

        //controlling shooting
        ShooterController shooterController = new ShooterController(player);
        Disposable shooterCont = scene.getInput().registerListener(shooterController);


        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED,
            ripley -> {
                kpControl.dispose();
                playerControl.dispose();
                shooterCont.dispose();
                popup(scene, new PlayerDied());
            }
        );

        scene.getMessageBus().subscribe(Door.DOOR_OPENED, door ->{
            if (door.getName().equals("exit door")){
                kpControl.dispose();
                playerControl.dispose();
                shooterCont.dispose();
                this.popup(scene, new LevelComplete());
            }
        });

        scene.getMessageBus().subscribe(KeeperController.INFO, keeperController1-> this.popup(scene, new Info()));

        scene.getMessageBus().publish(KeeperController.INFO, keeperController);
    }


    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        Ripley player = (Ripley) scene.getFirstActorByName("Ellen");
        assert player != null;

        player.showRipleyState();
        scene.follow(player);
    }


    /**
     * It will shows only one popup on scene
     * @param scene there will be popup placed
     * @param popup will be placed on scene
     */
    private void popup(Scene scene, Popup popup){

        //find if there is any popup on scene
        Popup any = (Popup) scene.getActors().stream()
            .filter(actor -> actor instanceof Popup)
            .findAny().orElse(null);

        //if there is any popup on scene and is same as a new one than it will disappear
        if(any != null && any.getPopupClass() == popup.getPopupClass()){
            scene.removeActor(any);
        }
        //if there is any and it is different then it vil disappear and new onw will be shown
        else if(any != null && any.getPopupClass() != popup.getPopupClass()){
            scene.removeActor(any);
            Ripley player = (Ripley) scene.getFirstActorByName("Ellen");
            if(player != null)
                popup.showPopup(scene,player.getPosX() - popup.getWidth()/2, player.getPosY()+100 );
        }

        //or there is no any popup, so only new one will be shown
        else{
            Ripley player = (Ripley) scene.getFirstActorByName("Ellen");
            if(player != null)
                popup.showPopup(scene,player.getPosX() - popup.getWidth()/2, player.getPosY()+100 );
        }
    }
}
