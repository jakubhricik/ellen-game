package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;

public class LockedDoor extends Door{
    private boolean isLocked;

    public LockedDoor(String name, Orientation orientation){
        super(name, orientation);
        lock();
    }

    public void lock(){
        this.isLocked = true;
    }

    public void unlock(){
        this.isLocked = false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public void useWith(Actor actor) {
        if(!isLocked()){
            if(isOpen()) close();
            else open();
        }
    }

    @Override
    public void sentMessage() {
        Messenger<Sender> messenger ;
        if(isLocked()){
            messenger = new Messenger<>("Doors are locked, I need to find some access card!");
        }else{
            messenger = new Messenger<>("These doors are already unlocked.");
        }
        messenger.scheduleFor(this);
        messenger.printMessage(1);
    }
}
