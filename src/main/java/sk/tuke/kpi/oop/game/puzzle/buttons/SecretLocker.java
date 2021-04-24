package sk.tuke.kpi.oop.game.puzzle.buttons;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Locker;
import sk.tuke.kpi.oop.game.factory.UsableFactory;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;
import sk.tuke.kpi.oop.game.puzzle.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SecretLocker extends Locker implements Publisher<Button> {

    private final List<Button> buttonsList = new ArrayList<>();
    private Disposable solvePuzzle = null;
    private boolean solved;


    public SecretLocker(String itemToLoot){
        super(itemToLoot);
    }

    @Override
    public void useWith(Actor actor) {
        sentMessage();
    }

    @Override
    public void subscribe(Button subscriber) {
        buttonsList.add(subscriber);
    }

    @Override
    public void unsubscribe(Button subscriber) {
        buttonsList.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        for (Button subscriber : buttonsList) {
            subscriber.update();
        }
    }

    @Override
    public void mainBusinessLogic() {
        boolean[] tableTruth = new boolean[buttonsList.size()];
        boolean check = true;
        solved = false;
        for (Button subscriber : buttonsList){
            tableTruth[subscriber.getNum() -1] = subscriber.isOn();
        }
        for(int i = 0; i<buttonsList.size()-1; i++){
            if(!tableTruth[i] && tableTruth[i + 1]){
                check = false;
                solved = false;
                break;
            }
        }
        for (boolean bool: tableTruth) {
            if(bool)solved = true;
            else {
                solved = false;
                break;
            }
        }
        if(!check) notifySubscribers();
        else if (solved) {
            Actor item = new UsableFactory().create(this.getItemToLoot());
            Objects.requireNonNull(this.getScene()).addActor(item,this.getPosX() + 16 ,this.getPosY());
            solvePuzzle.dispose();
        }
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);

        solvePuzzle = new Loop<>(
            new ActionSequence<>(
                new Invoke<>(this::mainBusinessLogic),
                new Wait<>(1)
            )
        ).scheduleFor(this);
    }

    @Override
    public void sentMessage() {
        if(!solved){
            Messenger<Sender> messenger = new Messenger<>("This locker is locked, after solving puzzle\nyou can open it!");
            messenger.scheduleFor(this);
            messenger.printMessage(1);
        }
    }
}
