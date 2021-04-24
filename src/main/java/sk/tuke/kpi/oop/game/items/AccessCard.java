package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

public class AccessCard extends AbstractActor implements Collectible, Usable<Actor>, Sender {

    public AccessCard(){
        Animation keyAnimation = new Animation(
            "sprites/key.png",
            16,
            16
        );
        setAnimation(keyAnimation);
    }

    @Override
    public void useWith(Actor actor) {
        if(actor != null){
            LockedDoor door = (LockedDoor) actor.getScene().getActors().stream()
                .filter(actor1 -> actor1 instanceof LockedDoor)
                .filter(actor1 -> actor1.intersects(actor))
                .findFirst().orElse(null);
            if(door != null){
                door.unlock();
            }
        }
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }


    @Override
    public void sentMessage() {
        Messenger<Sender> messenger = new Messenger<>("Ohh, I find some access card, now i can open Locked Doors");
        messenger.scheduleFor(this);
        messenger.printMessage(1);
    }
}
