package sk.tuke.kpi.oop.game.puzzle.lasers;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.EnergyConsumer;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;

public class Laser extends Handler implements EnergyConsumer, Sender {

    private final int order;
    private final Animation laserAnimation;
    private boolean isPowered;

    public Laser (Handler successor, int order){
        super(successor);
        this.order = order;
        this.isPowered = false;

        laserAnimation = new Animation(
            "sprites/laser.png",
            48,
            16,
            0.2f,
            Animation.PlayMode.ONCE
        );
        setAnimation(laserAnimation);
    }

    @Override
    public void handle(Order e) {
        if(isPowered){
            if (e.getOrderNum() == this.order){
                this.dealDamage();
            }
            else if (getSuccessor() != null)
                getSuccessor().handle(e);
            else
                System.out.println("End of chain.");
        }
    }

    private void dealDamage(){
        laserAnimation.resetToFirstFrame();
        laserAnimation.play();
        if(getScene() == null) return;

        Alive actor = (Alive) getScene().getActors().stream()
            .filter(actor1 -> actor1 instanceof Alive)
            .filter(actor1 -> actor1.intersects(this))
            .findFirst().orElse(null);

        if(actor != null){
            actor.getHealth().drain(30);
        }
    }

    @Override
    public void setPowered(boolean powered) {
        isPowered = powered;
    }



    @Override
    public void sentMessage() {
        Messenger<Sender> messenger;
        if(isPowered){
            messenger = new Messenger<>("Ouch!  That laser is very dangerous!");
        }
        else{
            messenger = new Messenger<>("Look there is Laser, It looks very dangerous!");
        }
        messenger.scheduleFor(this);
        messenger.printMessage(1);
    }
}
