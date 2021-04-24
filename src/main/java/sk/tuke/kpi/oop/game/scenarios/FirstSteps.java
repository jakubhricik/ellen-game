package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.SceneListener;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.*;

public class FirstSteps implements SceneListener {

    @Override
    public void sceneInitialized(@NotNull Scene scene) {

        //player:
        Ripley player = new Ripley();
        scene.addActor(player, 0, 0);

        //energy:
        Energy energy = new Energy();
        scene.addActor(energy, 100, 100);

        //ammo:
        Ammo ammo = new Ammo();
        scene.addActor(ammo, 150,150);

        //backpack:
        //Wrench
        Wrench wrench = new Wrench();
        scene.addActor(wrench, 200, 100);

        //hammer
        Hammer hammer = new Hammer();
        scene.addActor(hammer, 220, 100);

        //fire extinguisher
        FireExtinguisher fireExtinguisher = new FireExtinguisher();
        player.getBackpack().add(fireExtinguisher);


        //controlling keeper actions (drop, take, shift)
        KeeperController keeperController = new KeeperController(player);
        scene.getInput().registerListener(keeperController);

        //controlling player movement
        MovableController controller = new  MovableController(player);
        scene.getInput().registerListener(controller);


        new When<>(
            ()->player.intersects(energy),
            new Use<>(energy)
        ).scheduleFor(player);

        new When<>(
            ()->player.intersects(ammo),
            new Use<>(ammo)
        ).scheduleFor(player);

    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        Ripley player = (Ripley) scene.getFirstActorByName("Ellen");
        if(player != null){
            int playerEnergy = player.getHealth().getValue();
            int windowHeight = scene.getGame().getWindowSetup().getHeight();
            int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
            scene.getGame().getOverlay().drawText("| Energy: " + playerEnergy, 104, yTextPos);
            scene.getGame().getOverlay().drawText("| Ammo: "+ player.getFirearm().getAmmo(), 254, yTextPos);


            scene.getGame().pushActorContainer(player.getBackpack());
        }
    }
}
