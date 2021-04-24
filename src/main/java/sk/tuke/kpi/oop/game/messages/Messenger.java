package sk.tuke.kpi.oop.game.messages;

import sk.tuke.kpi.gamelib.Scene;

public class Messenger<A extends Sender> implements Printable<A> {

    private final String message;
    private Sender sender;
    private Scene scene;

    public Messenger(String message){
        this.message = message;
    }

    @Override
    public void printMessage(int duration) {
        if(sender != null && scene != null){
            int windowHeight = scene.getGame().getWindowSetup().getHeight();
            scene.getGame().getOverlay().drawText(message, 20 , windowHeight - 80).showFor(duration);
        }
    }

    @Override
    public void scheduleFor(A sender) {
        this.sender = sender;
        if(sender != null) scheduleOn(sender.getScene());
    }

    @Override
    public void scheduleOn(Scene scene) {
        this.scene = scene;
    }
}
