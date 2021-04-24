package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;

public class Energy extends AbstractActor implements Usable<Alive> , Sender {

    public Energy(){
        Animation animation = new Animation(
            "sprites/energy.png",
            16,
            16
        );
        setAnimation(animation);
    }

    @Override
    public void useWith(Alive actor) {
        //toto funguje ak stlacime klavesu U a intersektujem s energy item
        if(actor == null) return;
        Scene scene = getScene();

        if(scene == null) return;
        if(actor.getHealth().getValue() < actor.getHealth().getMaxHealth()){
            actor.getHealth().restore();
            scene.removeActor(this);
        }
    }

    @Override
    public Class<Alive> getUsingActorClass() {
        return Alive.class;
    }

    @Override
    public void sentMessage() {
        Messenger<Sender> messenger = new Messenger<>("Use it to gain more energy!");
        messenger.scheduleFor(this);
        messenger.printMessage(1);
    }
}
