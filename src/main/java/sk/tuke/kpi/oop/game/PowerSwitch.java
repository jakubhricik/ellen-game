package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.items.Usable;


public class PowerSwitch extends AbstractActor implements Usable<Ripley> {
    private Switchable switchable;
    private boolean isOn;


    public PowerSwitch(){
        Animation controllerAnimation;
        controllerAnimation = new Animation("sprites/switch.png");
        setAnimation(controllerAnimation);
        this.isOn = false;
        this.switchable = null;
    }

    public PowerSwitch(Switchable switchable){
        //set switcher animation
        this();
        this.switchable = switchable;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        this.switchOff();
    }

    public Switchable getDevice(){
        return this.switchable;
    }

    public void switchOn(){
        if(getDevice() != null){
            this.getDevice().turnOn();
            this.getAnimation().setTint(Color.WHITE);
            this.isOn = true;
        }
    }

    public void switchOff(){
        if(getDevice() != null){
            this.getDevice().turnOff();
            this.getAnimation().setTint(Color.GRAY);
            this.isOn = false;
        }
    }

    public void toggle(){
        if(isOn) switchOff();
        else switchOn();
    }

    @Override
    public void useWith(Ripley actor) {
        if(getDevice()!= null && actor != null){
            this.toggle();
        }
    }

    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }
}
