package sk.tuke.kpi.oop.game.openables;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.gamelib.map.SceneMap;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;
import sk.tuke.kpi.oop.game.weapons.Bullet;
import sk.tuke.kpi.oop.game.weapons.Rocket;


public class BreakableWall extends AbstractActor  implements Sender {

    private boolean isWall;
    private Disposable check;

    public BreakableWall(){
        Animation wall = new Animation(
            "sprites/wall_breakable.png"
        );
        setAnimation(wall);
        isWall = true;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        blockOrRelease();

        check = new Loop<>(
            new Invoke<>(()-> isDestroyed(scene))
        ).scheduleFor(this);
    }


    private void isDestroyed(Scene scene){
        Actor bullet = scene.getActors().stream()
            .filter(actor -> actor instanceof Bullet || actor instanceof Rocket)
            .filter(actor -> Math.abs(actor.getPosX() - this.getPosX()) <= 17)
            .filter(actor -> Math.abs(actor.getPosY() - this.getPosY()) <= 17)
            .findAny().orElse(null);

        if(bullet != null){
            this.isWall = false;
            this.check.dispose();
            blockOrRelease();
            scene.removeActor(this);
        }
    }

    private void blockOrRelease(){
        int positionX = this.getPosX();
        int positionY = this.getPosY();

        int tileX = positionX / 16;
        int tileY = positionY / 16;
        if(this.getScene() == null) return;

        SceneMap map = this.getScene().getMap();
        if(!isWall) map.getTile(tileX, tileY).setType(MapTile.Type.CLEAR);
        else map.getTile(tileX, tileY).setType(MapTile.Type.WALL);
    }

    @Override
    public void sentMessage() {
        if(isWall){
            Messenger<Sender> messenger = new Messenger<>("This wall looks different,\nmaybe I can break it.");
            messenger.scheduleFor(this);
            messenger.printMessage(1);
        }
    }
}
