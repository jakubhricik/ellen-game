package sk.tuke.kpi.oop.game.factory;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.AlienMother;
import sk.tuke.kpi.oop.game.characters.LurkerAlien;

import java.util.HashMap;
import java.util.Map;

public class Spawner implements Factory {
    private final Map<String, Actor> actorMap = new HashMap<>();

    @Override
    public Actor create(String alienName) {
        actorMap.put("alien", new Alien(100, new RandomlyMoving()));
        actorMap.put("alien mother", new AlienMother(new RandomlyMoving()));
        actorMap.put("lurker alien", new LurkerAlien());
        // .... other types of aliens , but now there just two

        if(alienName != null && actorMap.containsKey(alienName)) return actorMap.get(alienName);
        return null;
    }
}
