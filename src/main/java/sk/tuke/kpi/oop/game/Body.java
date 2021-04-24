package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.behaviours.Lootable;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.factory.UsableFactory;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;
import sk.tuke.kpi.oop.game.puzzle.Publisher;
import sk.tuke.kpi.oop.game.puzzle.Subscriber;

import java.util.ArrayList;
import java.util.List;


public class Body extends AbstractActor implements Usable<Ripley>, Lootable, Publisher<Subscriber>, Sender {

    private final String item;
    private boolean isLooted;
    private final List<Subscriber> subscriberList = new ArrayList<>();

    public Body(String item){
        Animation bodyAnimation = new Animation(
            "sprites/body.png",
            64,
            48
        );
        setAnimation(bodyAnimation);
        this.item = item;
        isLooted = false;
    }


    @Override
    public void useWith(Ripley actor) {
        if(!isLooted() && actor != null){
            this.loot();
            this.mainBusinessLogic();
        }
    }

    @Override
    public void loot() {
        Actor item = new UsableFactory().create(this.item);
        if(item instanceof AbstractActor && this.getScene() != null){
            this.getScene().addActor(item, this.getPosX() + 64, this.getPosY()+60);
        }
        setLooted(true);
    }

    @Override
    public boolean isLooted() {
        return this.isLooted;
    }

    @Override
    public void setLooted(boolean isLooted) {
        this.isLooted = isLooted;
    }

    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }



    @Override
    public void subscribe(Subscriber subscriber) {
        if(subscriber != null) subscriberList.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscriberList.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        for (Subscriber sub: subscriberList) {
            sub.update();
        }
    }

    @Override
    public void mainBusinessLogic() {
        notifySubscribers();
    }


    @Override
    public void sentMessage() {
        Messenger<Sender> messenger;
        if(!isLooted()){
            messenger = new Messenger<>("This body is not looted!");
        }
        else{
            messenger = new Messenger<>("This body is already looted!");
        }
        messenger.scheduleFor(this);
        messenger.printMessage(1);
    }
}
