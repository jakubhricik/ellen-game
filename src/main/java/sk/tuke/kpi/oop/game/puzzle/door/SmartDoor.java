package sk.tuke.kpi.oop.game.puzzle.door;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;
import sk.tuke.kpi.oop.game.openables.LockedDoor;
import sk.tuke.kpi.oop.game.puzzle.Subscriber;

public class SmartDoor extends LockedDoor implements Subscriber, Sender {

    public SmartDoor(String name, Orientation orientation) {
        super(name, orientation);
    }

    @Override
    public void useWith(Actor actor) {
        sentMessage();
    }

    @Override
    public void update() {
        if(isLocked() && !isOpen()){
            this.unlock();
            this.open();
        }
        else{
            this.close();
            this.lock();
        }
    }



    @Override
    public void sentMessage() {
        if(!isOpen()){
            Messenger<Sender> messenger = new Messenger<>("This door can be opened only with button\nwhich is somewhere here.");
            messenger.scheduleFor(this);
            messenger.printMessage(2);
        }
    }
}
