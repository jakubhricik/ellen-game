package sk.tuke.kpi.oop.game.popup;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class PlayerDied extends Popup {

    public PlayerDied(){
        Animation animation = new Animation(
            "sprites/popup_level_failed.png"
        );
        setAnimation(animation);
    }

    @Override
    public Class<PlayerDied> getPopupClass() {
        return PlayerDied.class;
    }
}
