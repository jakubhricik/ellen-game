package sk.tuke.kpi.oop.game.puzzle.door;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.oop.game.EnergyConsumer;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;
import sk.tuke.kpi.oop.game.puzzle.Publisher;
import sk.tuke.kpi.oop.game.puzzle.Subscriber;
import sk.tuke.kpi.oop.game.puzzle.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class SmartButton extends Button implements Publisher<Subscriber> , EnergyConsumer, Sender {

    private final List<Subscriber> subscribers = new ArrayList<>();
    private Actor actor;
    private boolean isPowered;

    public SmartButton(){
        super();
        isPowered = false;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        mainBusinessLogic();
    }


    @Override
    public void useWith(Actor actor) {
        sentMessage();
    }



    private void checkIntersect(Scene scene) {
        if(isPowered && isAliens()){
            this.getAnimation().setTint(Color.GRAY);
            Actor item = scene.getActors().stream()
                .filter(actor1 -> actor1.intersects(this))
                .filter(actor1 -> actor1 != this)
                .findAny().orElse(null);

            if(actor != null && item == null){
                actor = null;
                this.toggle();
                notifySubscribers();
            }
            else if (actor == null && item != null){
                actor = item;
                this.toggle();
                notifySubscribers();
            }
            else actor = item;
        }
        else{
            this.getAnimation().setTint(Color.DARK_GRAY);
        }
    }


    @Override
    public void mainBusinessLogic() {
        new Loop<>(
            new ActionSequence<>(
                new Invoke<>(() -> this.checkIntersect(this.getScene())),
                new Wait<>(1)
            )
        ).scheduleFor(this);
    }

    private boolean isAliens(){
        Alien alien = (Alien) this.getScene().getActors().stream()
            .filter(actor1 -> actor1 instanceof Alien)
            .findAny().orElse(null);
        return alien == null;
    }


    @Override
    public void subscribe(Subscriber subscriber) {
        if(subscriber!= null) subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        for(Subscriber sub: subscribers){
            sub.update();
        }
    }

    @Override
    public void setPowered(boolean powered) {
        isPowered = powered;
    }


    @Override
    public void sentMessage() {
        Messenger<Sender> messenger = new Messenger<>("You need to start power, and kill all aliens!");
        messenger.scheduleFor(this);
        messenger.printMessage(1);

    }
}
