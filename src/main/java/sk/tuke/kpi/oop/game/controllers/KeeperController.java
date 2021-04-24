package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.actions.Drop;
import sk.tuke.kpi.oop.game.actions.Shift;
import sk.tuke.kpi.oop.game.actions.Take;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.messages.Sender;

import java.util.Objects;

public class KeeperController implements KeyboardListener{

    private final Keeper keeper;
    public static final Topic<KeeperController> INFO = Topic.create("show info", KeeperController.class);


    public KeeperController(Keeper keeper){
        this.keeper = keeper;
    }

    @Override
    public void keyPressed(@NotNull Input.Key key) {
        try{
            switch (key){
                case ENTER:
                    new Take<>().scheduleFor(keeper);
                    break;
                case BACKSPACE:
                    new Drop<>().scheduleFor(keeper);
                    break;
                case S:
                    new Shift<>().scheduleFor(keeper);
                    break;
                case U:
                    Actor actor = Objects.requireNonNull(keeper.getScene()).getActors().stream()
                        .filter(act -> act instanceof Usable && keeper.intersects(act))
                        .findFirst()
                        .get();
                    new Use<>((Usable<?>) actor).scheduleForIntersectingWith(keeper);
                    break;
                case B:
                    new Use<>((Usable<?>) keeper.getBackpack().peek()).scheduleForIntersectingWith(keeper);
                    break;

                case I:
                    Sender sender = (Sender) Objects.requireNonNull(keeper.getScene()).getActors().stream()
                        .filter(actor1 -> actor1 instanceof Sender && keeper.intersects(actor1))
                        .findAny().get();
                    sender.sentMessage();
                    break;

                case A:
                    keeper.getScene().getMessageBus().publish(KeeperController.INFO, this);
                    break;
                default: break;
            }
        }catch (Exception e){
            System.out.println("something is not working correctly");
        }

    }

}
