package sk.tuke.kpi.oop.game.popup;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;

public abstract class Popup extends AbstractActor {
    public void showPopup(Scene scene, int x, int y){
        scene.addActor(this, x, y);
    }
    public abstract Class<?> getPopupClass();
}
