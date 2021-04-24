package sk.tuke.kpi.oop.game.puzzle.buttons;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;
import sk.tuke.kpi.oop.game.puzzle.Subscriber;

public class Button extends AbstractActor implements Subscriber, Usable<Actor>, Sender {

    private Animation buttonOn;
    private Animation buttonOff;
    private boolean isOn;
    private int num;

    public Button(){
        buttonOff = new Animation(
            "sprites/button_red.png",
            16,
            16
        );
        buttonOn = new Animation(
            "sprites/button_green.png",
            16,
            16
        );
        buttonOn.setTint(Color.GRAY);
        buttonOff.setTint(Color.GRAY);
        isOn = false;
        setAnimation(buttonOff);
    }

    public Button(int num){
        this();
        this.num = num;
    }

    protected void toggle(){
        isOn = !isOn;
        updateAnimation();
    }

    @Override
    public void update() {
        if(isOn) {
            isOn = false;
            updateAnimation();
        }
    }

    private void updateAnimation() {
        if(isOn) setAnimation(buttonOn);
        else setAnimation(buttonOff);
    }

    @Override
    public void useWith(Actor actor) {
        if(actor != null) this.toggle();
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }


    //getters:
    public int getNum() {
        return num;
    }

    public boolean isOn() {
        return isOn;
    }

    @Override
    public void sentMessage() {
        Messenger<Sender> messenger;
        if(!isOn()){
            messenger = new Messenger<>("That looks like button,\nwhat will happened if I press it?");
        }
        else{
            messenger = new Messenger<>("Hmm...  this button is on.");
        }
        messenger.scheduleFor(this);
        messenger.printMessage(1);
    }
}
