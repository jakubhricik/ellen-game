package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.behaviours.Lootable;
import sk.tuke.kpi.oop.game.factory.UsableFactory;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;


public class Locker extends AbstractActor implements Usable<Actor>, Lootable , Sender {

    private boolean isLooted;
    private String itemToLoot;

    public Locker(){
        Animation lockerAnimation = new Animation(
            "sprites/locker.png",
            16,
            16
        );
        setAnimation(lockerAnimation);
        setLooted(false);
        itemToLoot = "hammer";
    }

    public Locker(String itemToLoot){
        this();
        this.itemToLoot = itemToLoot;
    }


    @Override
    public void useWith(Actor actor) {
        if(!isLooted()){
            this.loot();
        }
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }

    @Override
    public void loot() {
        Actor item = new UsableFactory().create(this.itemToLoot);
        if(item instanceof AbstractActor && this.getScene() != null){
            this.getScene().addActor(item, this.getPosX(), this.getPosY());
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

    public void setItemToLoot(String itemToLoot) {
        this.itemToLoot = itemToLoot;
    }

    public String getItemToLoot(){
        return this.itemToLoot;
    }

    @Override
    public void sentMessage() {
        Messenger<Sender> messenger;
        if(!isLooted()){
            messenger = new Messenger<>("This Locker is not looted!");
        }
        else{
            messenger = new Messenger<>("This Locker is already looted!");
        }
        messenger.scheduleFor(this);
        messenger.printMessage(1);
    }
}
