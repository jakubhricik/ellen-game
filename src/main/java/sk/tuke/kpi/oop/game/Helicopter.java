package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

import java.util.Objects;

public class Helicopter extends AbstractActor {
    private Player player;

    public Helicopter(){
        Animation heliAnimation = new Animation(
            "sprites/heli.png",
            64,
            64,
            0.05f,
            Animation.PlayMode.LOOP_PINGPONG
        );
        setAnimation(heliAnimation);
    }

    //start searching player
    public void searchAndDestroy(){
        if(player != null) new Loop<>(new Invoke<>(this::destroy)).scheduleFor(this);
    }

    private void destroy(){
        int x = player.getPosX();
        int y = player.getPosY();

        if( x > this.getPosX()){
            setPosition(this.getPosX() + 1, this.getPosY());
        }
        else setPosition(this.getPosX() - 1, this.getPosY());

        if(y > this.getPosY()){
            setPosition(this.getPosX(), this.getPosY() + 1);
        }
        else setPosition(this.getPosX(), this.getPosY() - 1);

        if(intersects(player)){
            player.setEnergy(player.getEnergy() - 1);
        }
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        player = (Player) Objects.requireNonNull(this.getScene()).getFirstActorByName("Player");
    }
}
