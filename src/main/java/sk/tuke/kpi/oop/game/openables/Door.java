package sk.tuke.kpi.oop.game.openables;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.gamelib.map.SceneMap;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;

import java.util.Objects;

public class Door extends AbstractActor implements Openable, Usable<Actor> , Sender {

    //static topics about that if the dor are opened or closed
    public static final Topic<Door> DOOR_OPENED = Topic.create("door opened", Door.class);
    public static final Topic<Door> DOOR_CLOSED = Topic.create("door closed", Door.class);


    /**
     * this enum is about which orientation dor will have
     * it contains some info about the doors
     */
    public enum Orientation {
        HORIZONTAL("sprites/hdoor.png",32, 16), VERTICAL("sprites/vdoor.png", 16, 32);
        public String spriteName;
        public int width;
        public int height;
        Orientation(String spriteName, int width, int height){
            this.spriteName = spriteName;
            this.width = width;
            this.height = height;
        }

        public String getSpriteName() {
            return spriteName;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }


    private boolean isOpen;
    private final Orientation orientation;


    public Door(Orientation orientation){
        this("", orientation);
    }

    public Door(String name, Orientation orientation){
        super(name);
        this.orientation = orientation;
        isOpen = false;
        Animation doorAnimation = new Animation(
            orientation.getSpriteName(),
            orientation.getWidth(),
            orientation.getHeight(),
            0.1f,
            Animation.PlayMode.ONCE
        );
        doorAnimation.pause();
        setAnimation(doorAnimation);
    }



    /**
     * set tiles where dor are placed to the WALL if is closed or CLEA if is opened
     *
     * it also calculate the position of current tile based of position fo sprite
     * it only works for vertical placed dor
     */
    protected void blockOrRelease(){
        int positionX = this.getPosX();
        int positionY = this.getPosY();

        int tileX = positionX / 16;
        int tileY = positionY / 16;

        SceneMap map = Objects.requireNonNull(this.getScene()).getMap();

        if(isOpen()){
            if(this.orientation == Orientation.VERTICAL){
                map.getTile(tileX, tileY).setType(MapTile.Type.CLEAR);
                map.getTile(tileX, tileY + 1).setType(MapTile.Type.CLEAR);
            }
            else if (this.orientation == Orientation.HORIZONTAL){
                map.getTile(tileX, tileY).setType(MapTile.Type.CLEAR);
                map.getTile(tileX + 1, tileY).setType(MapTile.Type.CLEAR);
            }

        }
        else {
            if(this.orientation == Orientation.VERTICAL){
                map.getTile(tileX, tileY).setType(MapTile.Type.WALL);
                map.getTile(tileX, tileY + 1).setType(MapTile.Type.WALL);
            }
            else if (this.orientation == Orientation.HORIZONTAL){
                map.getTile(tileX, tileY).setType(MapTile.Type.WALL);
                map.getTile(tileX + 1, tileY).setType(MapTile.Type.WALL);
            }
        }
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        //dor are closed after they are placed on the scene
        this.close();
    }

    @Override
    public void open() {
        if(!isOpen()){
            this.getAnimation().setPlayMode(Animation.PlayMode.ONCE);
            this.getAnimation().resetToFirstFrame();
            this.getAnimation().play();
            isOpen = true;
            Objects.requireNonNull(getScene()).getMessageBus().publish(DOOR_OPENED, this);
        }
        blockOrRelease();
    }

    @Override
    public void close() {
        if(isOpen()){
            this.getAnimation().setPlayMode(Animation.PlayMode.ONCE_REVERSED);
            this.getAnimation().resetToFirstFrame();
            this.getAnimation().play();
            isOpen = false;
            Objects.requireNonNull(getScene()).getMessageBus().publish(DOOR_CLOSED, this);
        }
        blockOrRelease();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void useWith(Actor actor) {
        if(isOpen) close();
        else open();
    }


    @Override
    public void sentMessage() {
        Messenger<Sender> messenger = new Messenger<>("You can easily open these doors.");
        messenger.scheduleFor(this);
        messenger.printMessage(1);
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }

}
