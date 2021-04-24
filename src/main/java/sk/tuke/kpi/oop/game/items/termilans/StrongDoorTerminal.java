package sk.tuke.kpi.oop.game.items.termilans;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.openables.StrongDoor;

public class StrongDoorTerminal extends Terminal {

    public StrongDoorTerminal() {
        super();
    }

    //if you intersect with this terminal and press I then the message will be showed on ovelay
    @Override
    public void sentMessage() {
        String message = "Use this terminal to open Strong Doors";
        new HintTerminal(message, this).sentMessage();
    }

    // if ripley use this terminal ti will open all strong doors in 400 x 400 px
    @Override
    public void useWith(Ripley actor) {
        if(actor != null){
            Scene scene = actor.getScene();
            if(scene == null) return;

            scene.getActors().stream()
                .filter(actor1 -> actor1 instanceof StrongDoor)
                .forEach(actor1 -> {
                    if ((Math.abs(actor.getPosX() - actor1.getPosX()) <= 200) && Math.abs(actor.getPosY() - actor1.getPosY()) <= 200){
                        ((StrongDoor) actor1).useWith(this);
                    }
                });

        }
    }
}
