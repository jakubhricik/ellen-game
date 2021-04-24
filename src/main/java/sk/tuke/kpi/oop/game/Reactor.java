package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.actions.PerpetualReactorHeating;

import java.util.HashSet;
import java.util.Set;

public class Reactor extends AbstractActor implements Switchable, Repairable {
    //private variables
    private int temperature;
    private int damage;
    private boolean isRunning;
    private final Set<EnergyConsumer> devices;
    private final Animation hotAnimation;
    private final Animation brokenAnimation;
    private final Animation normalAnimation;
    private final Animation offAnimation;
    private final Animation reactorEstinguished;

    //constructor
    public Reactor(){
        //set damage & temperature to 0
        this.damage = 0;
        this.temperature = 0;
        this.isRunning = false;
        devices = new HashSet<>();

        //create animation object
        normalAnimation = new Animation(
            "sprites/reactor_on.png",
            80,
            80,
            0.1f,
            Animation.PlayMode.LOOP_PINGPONG
        );

        hotAnimation = new Animation(
            "sprites/reactor_hot.png",
            80,
            80,
            0.05f,
            Animation.PlayMode.LOOP_PINGPONG
        );

        brokenAnimation = new Animation(
            "sprites/reactor_broken.png",
            80,
            80,
            0.1f,
            Animation.PlayMode.LOOP_PINGPONG
        );

        offAnimation = new Animation(
          "sprites/reactor.png"
        );

        reactorEstinguished = new Animation(
            "sprites/reactor_extinguished.png"
        );

        //set actor's animation to just created Animation object
        updateAnimation();
    }

    public Reactor(int damage){
        this();
        this.damage = damage;
        updateAnimation();
    }

    //after turn on reactor its temperature start increasing by 1
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new PerpetualReactorHeating(1).scheduleFor(this);
    }


    //methods
    //increase temperature due to increment
    public void increaseTemperature(int increment){
        //updating temperature
        if (increment > 0 && isOn()){
            if (damage < 33){
                temperature += (int) Math.ceil(increment);
            }
            else if (damage < 66){
                temperature += (int) Math.ceil(increment * 1.5f);
            }
            else {
                temperature += (int) Math.ceil(increment * 2);
            }

            //updating damage
            if (temperature >= 2000 && temperature < 6000 && ((temperature - 2000) * 0.025f) > damage){
                this.damage = (int) Math.floor((this.temperature - 2000) * 0.025f);
            }
            else if (temperature >= 6000){
                damage = 100;
            }
            updateAnimation();
        }
    }

    private void updateElectricityFlow(){
        if(isOn()){
            for (EnergyConsumer customer :devices) {
                customer.setPowered(true);
            }
        }
        else for (EnergyConsumer customer :devices) {
            customer.setPowered(false);
        }
    }



    //decrease temperature
    public void decreaseTemperature(int decrement){
        if (decrement > 0 && isOn()){
            if (damage < 50){
                if (temperature < decrement){
                    temperature = 0;
                }
                else{
                    temperature -= decrement;
                }
            }
            else if (damage < 100){
                if (temperature < decrement / 2){
                    temperature = 0;
                }
                else{
                    temperature -= decrement / 2;
                }
            }
        }
        updateAnimation();
    }

    //repair reactor with hammer object
    @Override
    public boolean repair(){
        if (damage > 0 && damage < 100){
            int decrement = damage - 50;
            if (decrement > 0) {
                temperature = ((int) Math.round((decrement / 0.025) + 2000));
                damage -= 50;
            } else {
                temperature = 2000;
                damage = 0;
            }
            updateAnimation();
            return true;
        }
        return false;
    }

    public boolean extinguish() {
        if (damage == 100 ) {
            temperature = 4000;
            setAnimation(reactorEstinguished);
            return true;
        }
        return false;
    }


    //turning on and off reactor
    @Override
    public void turnOn(){
        this.isRunning = true;
        setAnimation(normalAnimation);
        updateAnimation();
        updateElectricityFlow();
    }
    @Override
    public void turnOff(){
        this.isRunning = false;
        setAnimation(offAnimation);
        updateElectricityFlow();
    }

    //updating animation due to temperature of reactor
    public void updateAnimation(){

        if (damage >= 100 && isOn()){
            setAnimation(brokenAnimation);
            this.isRunning = false;
        }
        else if (temperature > 4000 && isOn()){
            setAnimation(hotAnimation);
        }
        else if (temperature <= 4000 && isOn()) {
            if (damage > 50){
                normalAnimation.setFrameDuration(0.05f);
            }
            else {
                normalAnimation.setFrameDuration(0.1f);
            }
            setAnimation(normalAnimation);
        }
        else {
            if(damage < 100){
                setAnimation(offAnimation);
            }else{
                setAnimation(brokenAnimation);
            }
        }
        updateElectricityFlow();
    }

    @Override
    public boolean isOn(){
        return this.isRunning;
    }

    //lights
    public void addDevice(EnergyConsumer customer){
        if(customer != null){
            this.devices.add(customer);
            updateElectricityFlow();
        }
    }
    public void removeDevice(EnergyConsumer customer){
        if(customer != null){
            customer.setPowered(false);
            this.devices.remove(customer);
        }
    }

    //getters
    public int getDamage() {
        return damage;
    }
    public int getTemperature() {
        return temperature;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
