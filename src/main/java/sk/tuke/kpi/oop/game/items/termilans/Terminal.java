package sk.tuke.kpi.oop.game.items.termilans;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.messages.Sender;

public abstract class Terminal extends AbstractActor implements Usable<Ripley>, Sender {

    public Terminal(){
        Animation deskAnimation = new Animation(
            "sprites/desk.png",
            48,
            48
        );
        setAnimation(deskAnimation);
    }

    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }
}
