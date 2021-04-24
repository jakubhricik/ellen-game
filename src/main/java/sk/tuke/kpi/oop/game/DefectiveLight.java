package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

import java.util.Objects;
import java.util.Random;

public class DefectiveLight extends Light implements Repairable {
    private Disposable loop ;
    private boolean repaired;

    //we have to toggle the light before it start blinking !!!!!!
    public DefectiveLight(){
        super();
        repaired = false;
    }

    private void randomLight(){
        repaired = false;
        Random random = new Random();
        //if light is on it will randomly blink
        if ((random.nextInt(20)+1) == 1 ){
            toggle();
        }
    }

    @Override
    public boolean repair(){
        if(loop != null && !repaired){
            loop.dispose();
            loop = new ActionSequence<>(
                new Invoke<>(()->this.repaired = true),
                new Wait<>(10),
                new Loop<>(new Invoke<>(this::randomLight))
            ).scheduleOn(Objects.requireNonNull(this.getScene()));
            return true;
        }
        return false;
    }


    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        // v metode addedToScene triedy Cooler
        loop = new Loop<>(new Invoke<>(this::randomLight)).scheduleFor(this);
    }
}
