package sk.tuke.kpi.oop.game.scenarios.rooms;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.map.MapMarker;
import sk.tuke.kpi.oop.game.puzzle.buttons.Button;
import sk.tuke.kpi.oop.game.puzzle.buttons.SecretLocker;

import java.util.Map;

public class MaintenanceRoom implements Room {

    private final Scene scene;

    public MaintenanceRoom(Scene scene){
        this.scene = scene;
    }

    public void setUp(){

        Map<String, MapMarker> markers = scene.getMap().getMarkers();

        MapMarker button_1 = markers.get("button-1");
        MapMarker button_2 = markers.get("button-2");
        MapMarker button_3 = markers.get("button-3");
        MapMarker locker_secret = markers.get("locker-button-secret");

        Button button1 = new Button(1);
        Button button2 = new Button(2);
        Button button3 = new Button(3);
        SecretLocker secretLocker = new SecretLocker("access card");

        scene.addActor(button1, button_1.getPosX(), button_1.getPosY());
        scene.addActor(button2, button_2.getPosX(), button_2.getPosY());
        scene.addActor(button3, button_3.getPosX(), button_3.getPosY());
        scene.addActor(secretLocker, locker_secret.getPosX(), locker_secret.getPosY());

        secretLocker.subscribe(button1);
        secretLocker.subscribe(button2);
        secretLocker.subscribe(button3);
    }
}
