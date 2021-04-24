package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.items.Usable;

public class Use<A extends Actor> extends AbstractAction<A> {

    private final Usable<A> usable;
    public Use(Usable<A> usable){
        this.usable = usable;
    }

    @Override
    public void execute(float deltaTime) {
        this.usable.useWith(getActor());
        setDone(true);
    }

    /**
     * added actor asi actor in Use , disposable function
     *
     * @param mediatingActor actor, that is called whit method useWith(actor) in usableItem
     * @return Disposable acton
     */
    public Disposable scheduleForIntersectingWith(Actor mediatingActor) {
        Scene scene = mediatingActor.getScene();
        if (scene == null) return null;
        Class<A> usingActorClass = usable.getUsingActorClass();
        return scene.getActors().stream()
            .filter(mediatingActor::intersects)
            .filter(usingActorClass::isInstance)
            .map(usingActorClass::cast)
            .findFirst()
            .map(this::scheduleFor)
            .orElse(null);
    }
}
