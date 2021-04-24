package sk.tuke.kpi.oop.game;

import java.awt.geom.Rectangle2D;

public class ChainBomb extends TimeBomb{

    private Rectangle2D rectangle;
    public ChainBomb(float time){
        super(time);
    }

    public void makeRectangle(){
        this.rectangle = new Rectangle2D.Float(
            this.getPosX(),
            this.getPosY(),
            16,
            16
        );
    }

    public Rectangle2D getRectangle() {
        return rectangle;
    }

}
