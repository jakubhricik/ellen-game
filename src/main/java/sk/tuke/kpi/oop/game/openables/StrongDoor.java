package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.oop.game.items.termilans.Terminal;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;

public class StrongDoor extends Door{

    public StrongDoor(String name, Orientation orientation) {
        super(name, orientation);
        String sprite;
        //change sprites
        if(orientation == Orientation.HORIZONTAL) sprite = "sprites/hdoor_strong.png";
        else sprite = "sprites/vdoor_strong.png";
        Animation doorAnimation = new Animation(
            sprite,
            orientation.getWidth(),
            orientation.getHeight(),
            0.2f,
            Animation.PlayMode.ONCE
        );
        //change color of doors
        doorAnimation.setTint(Color.WHITE.alpha(90));
        setAnimation(doorAnimation);
        doorAnimation.pause();
    }

    @Override
    public void useWith(Actor actor) {
        //its can be opened only with terminal for strong doors
        if(actor instanceof Terminal) {
            if(isOpen()) close();
            else open();
        }
    }

    @Override
    public void sentMessage() {
        if(!isOpen()){
            Messenger<Sender> messenger;
            messenger = new Messenger<>("There must be some way to open these doors.\nI need figure it out!");
            messenger.scheduleFor(this);
            messenger.printMessage(2);
        }
    }
}
