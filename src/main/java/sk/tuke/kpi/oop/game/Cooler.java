package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Cooler extends AbstractActor implements Switchable{

    private boolean isRunning;
    private final Animation fanAnimation;
    private Reactor reactor;

    public Cooler(Reactor reactor){
        this.reactor = reactor;
        isRunning = false;
        fanAnimation = new Animation(
            "sprites/fan.png",
            32,
            32,
            0.2f,
            Animation.PlayMode.LOOP_PINGPONG
        );
        fanAnimation.pause();
        setAnimation(fanAnimation);
    }

    @Override
    public boolean isOn(){
        return isRunning;
    }

    @Override
    public void turnOn(){
        if(reactor == null) return;
        if(!isRunning) {
            isRunning = true;
            this.fanAnimation.play();
        }
    }

    @Override
    public void turnOff(){
        if(reactor == null) return;
        if(isRunning){
            isRunning = false;
            this.fanAnimation.pause();
        }
    }

    public void coolReactor(){
        if(reactor == null) return;
        if (this.isRunning){
            this.reactor.decreaseTemperature(1);
        }
    }

    public Reactor getReactor(){
        return this.reactor;
    }

    //after turn on reactor its temperature start increasing by 1
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        // v metode addedToScene triedy Cooler
        new Loop<>(new Invoke<>(this::coolReactor)).scheduleFor(this);
    }
}
