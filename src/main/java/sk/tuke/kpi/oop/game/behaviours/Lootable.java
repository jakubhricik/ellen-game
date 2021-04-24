package sk.tuke.kpi.oop.game.behaviours;


public interface Lootable {
    void loot();
    boolean isLooted();
    default void setLooted(boolean isLooted){}
}
