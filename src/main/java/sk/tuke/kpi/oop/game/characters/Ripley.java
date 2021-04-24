package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Game;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.weapons.Firearm;
import sk.tuke.kpi.oop.game.weapons.Gun;


public class Ripley extends AbstractActor implements Movable, Keeper, Alive, Armed {

    public static final Topic<Ripley> RIPLEY_DIED = Topic.create("ripley died", Ripley.class);

    private final Backpack backpack;
    private final Animation playerDied;
    private final Health ripleyHealth;
    private Firearm gun;

    /**
     * Actor Ripley is character of player
     */
    public Ripley(){
        super("Ellen");
        Animation playerAnimation = new Animation(
            "sprites/player.png",
            32,
            32,
            0.1f,
            Animation.PlayMode.LOOP_PINGPONG
        );

        playerDied = new Animation(
            "sprites/player_die.png",
            32,
            32,
            0.1f,
            Animation.PlayMode.ONCE
        );

        setAnimation(playerAnimation);
        getAnimation().pause();
        ripleyHealth = new Health(100);
        backpack = new Backpack("Ripley's backpack", 10);
        gun = new Gun(100, 500);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        getHealth().onExhaustion(()-> {
            setAnimation(playerDied);
            scene.cancelActions(this);
            scene.getMessageBus().publish(Ripley.RIPLEY_DIED, this);
        });
    }

    /**
     * Write stats on display
     *
     * also checking if player is died or no
     * This method have to be called in scenario class, in sceneUpdating
     */
    public void showRipleyState(){
        Scene scene = this.getScene();
        if (scene == null) return;

        Game game = scene.getGame();
        int playerEnergy = this.getHealth().getValue();

        int windowHeight = game.getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;

        game.getOverlay().drawText("| Energy: " + playerEnergy, 104, yTextPos);
        game.getOverlay().drawText("| Ammo: "+ gun.getAmmo(), 254, yTextPos);

        game.pushActorContainer(this.getBackpack());
    }


    @Override
    public Firearm getFirearm() {
        return this.gun;
    }

    @Override
    public void setFirearm(Firearm weapon) {
        this.gun = weapon;
    }

    @Override
    public Backpack getBackpack() {
        return backpack;
    }

    @Override
    public void startedMoving(Direction direction) {
        getAnimation().setRotation(direction.getAngle());
        getAnimation().play();
    }

    @Override
    public void stoppedMoving() {
        getAnimation().pause();
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    //getter:
    @Override
    public Health getHealth() {
        return ripleyHealth;
    }

}
