package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Computer extends AbstractActor implements EnergyConsumer {

    //constructor for computer , setting animation
    private boolean isPowered;
    private final Animation normalAnimation;
    public Computer(){
        isPowered = false;
        normalAnimation = new Animation(
            "sprites/computer.png",
            80,
            48,
            0.2f,
            Animation.PlayMode.LOOP_PINGPONG
        );
        setAnimation(normalAnimation);
        updateAnimation();
    }

    @Override
    public void setPowered(boolean isElectricityDistributed){
        isPowered = isElectricityDistributed;
        updateAnimation();
    }

    private void updateAnimation(){
        if(isPowered) normalAnimation.play();
        else normalAnimation.pause();
    }


    //functions to add and sub numbers
    public int add(int a, int b){
        if(isPowered) return a + b;
        return 0;
    }
    public int sub(int a, int b){
        if(isPowered) return a - b;
        return 0;
    }

    public float add(float a, float b){
        if(isPowered) return a + b;
        return 0;
    }
    public float sub(float a, float b){
        if(isPowered) return a - b;
        return 0;
    }
}
