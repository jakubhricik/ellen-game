package sk.tuke.kpi.oop.game.popup;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class Info extends Popup  {

    public Info(){
        Animation animation = new Animation("sprites/popup_info.png");
        setAnimation(animation);
    }

    @Override
    public Class<Info> getPopupClass() {
        return Info.class;
    }
}
