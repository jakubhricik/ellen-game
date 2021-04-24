package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor implements Switchable, EnergyConsumer {
    private final Animation lightOn;
    private final Animation lightOff;
    private boolean isLighted;
    private boolean isPowered;

    public Light(){
        lightOff = new Animation("sprites/light_off.png");
        lightOn = new Animation("sprites/light_on.png");
        setAnimation(lightOff);
        isLighted = false;
    }


    public void toggle(){
        isLighted = !isLighted;
        updateAnimation();
    }

    @Override
    public void setPowered(boolean isElectricityDistributed){
        isPowered = isElectricityDistributed;
        updateAnimation();
    }

    private void updateAnimation(){
        if (isLighted && isPowered){
            setAnimation(lightOn);
        }else setAnimation(lightOff);
    }

    @Override
    public void turnOff(){
        isLighted = false;
        updateAnimation();
    }

    @Override
    public void turnOn(){
        isLighted = true;
        updateAnimation();
    }

    @Override
    public boolean isOn() {
        return isLighted;
    }

    public boolean isPowered() {
        return isPowered;
    }
}
