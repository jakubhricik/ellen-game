package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;

public class Ammo extends AbstractActor implements Usable<Armed> , Sender {

    public Ammo(){
        Animation animation = new Animation(
            "sprites/ammo.png",
            16,
            16
        );
        setAnimation(animation);
    }


    @Override
    public void useWith(Armed actor) {
        if(actor != null && actor.getFirearm().getAmmo() <500){
            actor.getFirearm().reload(50);
            getScene().removeActor(this);
        }
    }

    @Override
    public Class<Armed> getUsingActorClass() {
        return Armed.class;
    }


    @Override
    public void sentMessage() {
        Messenger<Sender> messenger = new Messenger<>("I can use it to reload my gun.\nIt will give me some more ammo.");
        messenger.scheduleFor(this);
        messenger.printMessage(1);
    }
}
