package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Objects;

public class TimeBomb extends AbstractActor {
    private final Animation bombActivated;
    private final Animation bombExplosion;
    private final float timeToDetonate;
    private boolean activated = false;
    //    private int time;
    private Ellipse2D ellipse;

    public TimeBomb(float detonationTime) {
        this.timeToDetonate = detonationTime;
        Animation bombNonActivated = new Animation("sprites/bomb.png");
        bombActivated = new Animation(
            "sprites/bomb_activated.png",
            16,
            16,
            0.07f,
            Animation.PlayMode.LOOP_PINGPONG
        );
        bombExplosion = new Animation(
            "sprites/small_explosion.png",
            16,
            16,
            0.1f,
            Animation.PlayMode.ONCE);
        setAnimation(bombNonActivated);
    }

    public void activate(){
        if(this.activated) {
            return;
        }
        this.ellipse = new Ellipse2D.Float(
            this.getPosX() + 8 - 50,
            this.getPosY() + 8 - 50,
            100,
            100
        );
        this.activated = true;
        setAnimation(bombActivated);
        new ActionSequence<>(
            new Wait<>(timeToDetonate),
            new Invoke<>(this::detonate)
        ).scheduleFor(this);
    }
    private void detonate() {
        this.setAnimation(bombExplosion);
        this.destroyChainBombs();
        new When<>(
            ()->this.bombExplosion.getCurrentFrameIndex() == this.bombExplosion.getFrameCount()-1,
            new Invoke<>(() -> Objects.requireNonNull(this.getScene()).removeActor(this))
        ).scheduleFor(this);
    }
    private void destroyChainBombs(){
        @NotNull List<Actor> chainBombs = Objects.requireNonNull(this.getScene()).getActors();
        for (Actor bomb : chainBombs) {
            if(this != bomb && bomb instanceof ChainBomb){
                ChainBomb chainBomb = (ChainBomb) bomb;
                chainBomb.makeRectangle();
                if(!chainBomb.isActivated() && this.getEllipse().intersects(chainBomb.getRectangle())){
                    chainBomb.activate();
                }
            }
        }
    }
    public boolean isActivated() {
        return activated;
    }
    public Ellipse2D getEllipse() {
        return ellipse;
    }
}
