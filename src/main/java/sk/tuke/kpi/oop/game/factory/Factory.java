package sk.tuke.kpi.oop.game.factory;


import sk.tuke.kpi.gamelib.Actor;

public interface Factory {
    Actor create(String itemName);
}
