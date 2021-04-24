package sk.tuke.kpi.oop.game.messages;

import sk.tuke.kpi.gamelib.Scene;

public interface Printable<S extends Sender> {
    void printMessage(int duration);
    default void scheduleFor(S sender){}
    default void scheduleOn(Scene scene) {}
}
