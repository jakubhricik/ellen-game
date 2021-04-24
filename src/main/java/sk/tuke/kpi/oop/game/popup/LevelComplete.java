package sk.tuke.kpi.oop.game.popup;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class LevelComplete extends Popup {

    public LevelComplete(){
        Animation animation = new Animation(
            "sprites/popup_level_done.png"
        );
        setAnimation(animation);
    }

    @Override
    public Class<LevelComplete> getPopupClass() {
        return LevelComplete.class;
    }
}
