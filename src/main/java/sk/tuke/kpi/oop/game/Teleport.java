package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

import java.awt.*;

public class Teleport extends AbstractActor {
    private Teleport destination;
    private Player player;
    private boolean teleported;

    public Teleport(){
        teleported = false;
        Animation teleportAnimation = new Animation(
            "sprites/lift.png",
            48,
            48
        );
        setAnimation(teleportAnimation);
    }

    public Teleport(Teleport teleport){
        this.destination = teleport;
        teleported = false;
        Animation teleportAnimation = new Animation(
            "sprites/lift.png",
            48,
            48
        );
        setAnimation(teleportAnimation);
    }

    public void teleportPlayer(Player player){
        teleported = true;
        if(this.destination != null){
            this.destination.teleported = true;
        }
        player.setPosition(
            this.getPosX() + 8,
            this.getPosY() + 8
        );
        new When<>(
            ()-> !this.isPlayerTeleported(),
            new Invoke<>(()-> {
                this.teleported = false;
                if(this.destination != null){
                    this.destination.teleported = false;
                }
            })
        ).scheduleFor(this);

    }

    private boolean isPlayerOnTeleport(){
        if(destination != null && !teleported){
            var myRectangle = new Rectangle(getPosX(), getPosY(), getWidth()>>1, getHeight()>>1);
            var actorRectangle = new Rectangle(player.getPosX(), player.getPosY(), player.getWidth()>>1, player.getHeight()>>1);
            return myRectangle.intersects(actorRectangle);
        }
        else return false;
    }

    private boolean isPlayerTeleported(){
        if(teleported){
            return this.intersects(player);
        }
        else return false;
    }

    public Teleport getDestination() {
        return destination;
    }

    public void setDestination(Teleport destination) {
        if(this != destination){
            this.destination = destination;
        }
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        this.player = (Player) scene.getFirstActorByName("Player");
        super.addedToScene(scene);
        new Loop<>(
            new When<>(
                this::isPlayerOnTeleport,
                new Invoke<>(()-> destination.teleportPlayer(this.player))
            )
        ).scheduleFor(this);
    }
}
