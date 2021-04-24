package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.Scenario;
import sk.tuke.kpi.gamelib.map.MapMarker;
import sk.tuke.kpi.oop.game.*;
import sk.tuke.kpi.oop.game.items.Hammer;
import sk.tuke.kpi.oop.game.items.Wrench;

import java.util.Map;

public class TrainingGameplay extends Scenario {
    @Override
    public void setupPlay(@NotNull Scene scene) {


        Map<String, MapMarker> markers = scene.getMap().getMarkers();
        MapMarker reactorArea1 = markers.get("reactor-area-1");
        MapMarker reactorArea2 = markers.get("reactor-area-2");
        MapMarker coolerArea1 = markers.get("cooler-area-1");
        MapMarker coolerArea2 = markers.get("cooler-area-2");
        MapMarker coolerArea3 = markers.get("cooler-area-3");
        MapMarker computerArea = markers.get("computer-area");



        Reactor reactor = new Reactor();
        scene.addActor(reactor, reactorArea1.getPosX(), reactorArea1.getPosY());
        reactor.turnOn();

        Reactor reactor2 = new Reactor();
        scene.addActor(reactor2, reactorArea2.getPosX(), reactorArea2.getPosY());
        reactor2.turnOn();


        SmartCooler cooler = new SmartCooler(reactor);
        scene.addActor(cooler, coolerArea1.getPosX(), coolerArea1.getPosY());

        Cooler cooler2 = new Cooler(reactor2);
        scene.addActor(cooler2, coolerArea2.getPosX(), coolerArea2.getPosY());
        cooler2.turnOff();

        Cooler cooler3 = new Cooler(reactor2);
        scene.addActor(cooler3, coolerArea3.getPosX(), coolerArea3.getPosY());

        Hammer hammer = new Hammer();
        scene.addActor(hammer,100,100);
        new When<>(
            () -> reactor.getTemperature() >= 3000,
            new Invoke<>(() -> hammer.useWith(reactor))
        ).scheduleFor(reactor);

        Wrench wrench = new Wrench();
        scene.addActor(wrench, 90, 100);

        Computer computer = new Computer();
        scene.addActor(computer, computerArea.getPosX(), computerArea.getPosY());
        reactor.addDevice(computer);

        DefectiveLight light = new DefectiveLight();
        scene.addActor(light,176,216);
        reactor.addDevice(light);
        light.turnOn();

        //switch pre reactor
        PowerSwitch powerSwitch = new PowerSwitch(reactor);
        scene.addActor(powerSwitch, reactorArea1.getPosX() -20, reactorArea1.getPosY());
        if(reactor.isOn())powerSwitch.switchOn();
        else powerSwitch.switchOff();

        //switch pre cooler
        PowerSwitch powerSwitch1 = new PowerSwitch(cooler2);
        scene.addActor(powerSwitch1, coolerArea2.getPosX()+30, coolerArea2.getPosY());
        if(cooler2.isOn())powerSwitch1.switchOn();
        else powerSwitch1.switchOff();
        new ActionSequence<>(
            new Wait<>(5),
            new Invoke<>(powerSwitch1::switchOn)
        ).scheduleFor(powerSwitch1);

        //switch pre svetlo
        PowerSwitch powerSwitch2 = new PowerSwitch(light);
        scene.addActor(powerSwitch2, 160,216);
        if(light.isOn())powerSwitch2.switchOn();
        else powerSwitch2.switchOff();


//        TimeBomb bomb = new TimeBomb(5f);
//        ChainBomb chainBomb1 = new ChainBomb(3f);
//        ChainBomb chainBomb2 = new ChainBomb(3f);
//        ChainBomb chainBomb3 = new ChainBomb(3f);
//        ChainBomb chainBomb4 = new ChainBomb(3f);
//        scene.addActor(bomb, 200, 200);
//        scene.addActor(chainBomb1, 200, 251);
//        scene.addActor(chainBomb2, 200, 149);
//        scene.addActor(chainBomb3, 251, 200);
//        scene.addActor(chainBomb4, 149, 200);
//        bomb.activate();
    }
}
