package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.backends.lwjgl2.Lwjgl2Backend;
import sk.tuke.kpi.oop.game.scenarios.SpiritTemple;


public class Main {

    public static void main(String[] args) {

        WindowSetup windowSetup = new WindowSetup("Project Ellen", 800, 600);
        Game game;

        //these is for MacOs
        game = new GameApplication(windowSetup, new Lwjgl2Backend());

//        // these is for windows
//        game = new GameApplication(windowSetup, new LwjglBackend());

        // vytvorenie sceny pre hru
        Scene scene = new World("world", "maps/spirit-temple/spirit-temple.tmx", new SpiritTemple.Factory());
        game.addScene(scene);


        SpiritTemple spiritTemple = new SpiritTemple();
        scene.addListener(spiritTemple);

        // spustenie hry
        game.start();
        game.getInput().onKeyPressed(Input.Key.ESCAPE, game::stop);
    }

}
