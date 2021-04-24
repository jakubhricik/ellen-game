package sk.tuke.kpi.oop.game;


import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

public class SmartCooler extends Cooler{

    public SmartCooler(Reactor reactor) {
        super(reactor);
    }

    //checking if temperature is in interval 1500 to 2500
    //then cool the reactor into interval
    public void checkTemperature() {
        if (getReactor() == null) return;
        if (getReactor().getTemperature() > 2500) turnOn();
        if (getReactor().getTemperature() < 1500) turnOff();
        coolReactor();
    }

    //after turn on reactor its temperature start increasing by 1
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        // v metode addedToScene triedy Cooler
        new Loop<>(new Invoke<>(this::checkTemperature)).scheduleFor(this);
    }
}
