package sk.tuke.kpi.oop.game.puzzle.lasers;

import sk.tuke.kpi.gamelib.framework.AbstractActor;

public abstract class Handler extends AbstractActor {

    private final Handler successor;

    public Handler(Handler successor) {
        this.successor = successor;
    }

    public Handler getSuccessor() {
        return this.successor;
    }

    public abstract void handle(Order e);
}
