package sk.tuke.kpi.oop.game.scenarios.rooms;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.map.MapMarker;
import sk.tuke.kpi.oop.game.Body;
import sk.tuke.kpi.oop.game.PowerSwitch;
import sk.tuke.kpi.oop.game.Reactor;
import sk.tuke.kpi.oop.game.SmartCooler;
import sk.tuke.kpi.oop.game.characters.AliensSpawner;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.puzzle.door.SmartButton;
import sk.tuke.kpi.oop.game.puzzle.door.SmartDoor;
import sk.tuke.kpi.oop.game.puzzle.lasers.Laser;
import sk.tuke.kpi.oop.game.puzzle.lasers.Order;

import java.util.Map;

public class ReactorHall implements Room {
    private Scene scene;

    public ReactorHall(Scene scene){
        this.scene = scene;
    }

    public void setUp(){

        Map<String, MapMarker> markers = scene.getMap().getMarkers();
        MapMarker reactor_hall = markers.get("reactor-hall");
        MapMarker spawner_hall = markers.get("spawner-hall");
        MapMarker body_hall = markers.get("body-hall");
        MapMarker powerSwitch_hall = markers.get("switch-hall");
        MapMarker smartCooler_hall = markers.get("smart-hall");
        MapMarker smartDoor_hall = markers.get("smart-door-hall");
        MapMarker smartButton_hall = markers.get("smart-button-hall");
        MapMarker laser1_hall = markers.get("laser-hall-1");
        MapMarker laser2_hall = markers.get("laser-hall-2");
        MapMarker laser3_hall = markers.get("laser-hall-3");



        Reactor reactor = new Reactor();
        PowerSwitch powerSwitch = new PowerSwitch(reactor);
        Body body = new Body("ammo");
        AliensSpawner spawner = new AliensSpawner("alien", 10);
        SmartCooler cooler = new SmartCooler(reactor);
        SmartDoor smartDoor = new SmartDoor(smartDoor_hall.getName(), Door.Orientation.VERTICAL);
        SmartButton smartButton = new SmartButton();
        Laser laser3 = new Laser(null, 3);
        Laser laser2 = new Laser(laser3, 2);
        Laser laser1 = new Laser(laser2, 1);



        scene.addActor(reactor, reactor_hall.getPosX(), reactor_hall.getPosY());
        scene.addActor(body, body_hall.getPosX(), body_hall.getPosY());
        scene.addActor(spawner,spawner_hall.getPosX(), spawner_hall.getPosY());
        scene.addActor(powerSwitch,powerSwitch_hall.getPosX(), powerSwitch_hall.getPosY());
        scene.addActor(cooler, smartCooler_hall.getPosX(), smartCooler_hall.getPosY());
        scene.addActor(smartDoor, smartDoor_hall.getPosX(),smartDoor_hall.getPosY());
        scene.addActor(smartButton, smartButton_hall.getPosX(), smartButton_hall.getPosY());
        scene.addActor(laser1, laser1_hall.getPosX(), laser1_hall.getPosY());
        scene.addActor(laser2, laser2_hall.getPosX(), laser2_hall.getPosY());
        scene.addActor(laser3, laser3_hall.getPosX(), laser3_hall.getPosY());

        reactor.addDevice(laser1);
        reactor.addDevice(laser2);
        reactor.addDevice(laser3);

        new Loop<>(
            new ActionSequence<>(
                new Wait<>(1),
                new Invoke<>(()-> laser1.handle(Order.FIRST)),
                new Wait<>(0.5f),
                new Invoke<>(()-> laser1.handle(Order.SECOND)),
                new Wait<>(0.2f),
                new Invoke<>(()-> laser1.handle(Order.THIRD))
            )
        ).scheduleFor(laser1);

        reactor.addDevice(smartButton);
        smartButton.subscribe(smartDoor);
        body.subscribe(spawner);

    }
}
