package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdAudio;

public class MapTest {
    public static void main(String[] args) {
        Engine game = new Engine();
        TETile[][] world = game.interactWithInputString("N1234S");
        System.out.println(TETile.toString(world));
    }

}
