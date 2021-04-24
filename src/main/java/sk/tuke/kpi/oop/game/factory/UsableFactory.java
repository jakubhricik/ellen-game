package sk.tuke.kpi.oop.game.factory;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.oop.game.items.*;

import java.util.HashMap;
import java.util.Map;

public class UsableFactory implements Factory {
    private final Map<String, Actor> itemsMap = new HashMap<>();

    @Override
    public Actor create(String itemName) {
        itemsMap.put("ammo", new Ammo());
        itemsMap.put("energy", new Energy());
        itemsMap.put("hammer", new Hammer());
        itemsMap.put("access card", new AccessCard());
        itemsMap.put("wrench", new Wrench());
        //.... there will be more usable items


        if(itemName != null) return itemsMap.get(itemName);
        else return null;
    }
}

