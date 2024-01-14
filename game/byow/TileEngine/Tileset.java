package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile(' ', Color.white, new Color(46, 46, 59), "right facing knight", "Rightward.png");

    public static final TETile AVATARUP = new TETile(' ', Color.white, new Color(46, 46, 59), "up facing knight", "Upward.png");

    public static final TETile AVATARDOWN = new TETile(' ', Color.white, new Color(46, 46, 59), "down facing knight", "Downward.png");

    public static final TETile AVATARLEFT = new TETile(' ', Color.white, new Color(46, 46, 59), "left facing knight", "Leftward.png");
    public static final TETile WALL = new TETile('█', Color.BLACK, new Color(0, 255, 0),
            "wall");
    public static final TETile FLOOR = new TETile('▚', new Color(15, 15, 15), new Color(46, 46, 59),
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, new Color(12, 12, 12), "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile(' ', Color.yellow, new Color(100, 194, 253), "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    public static final TETile COIN = new TETile('⭗', Color.yellow, new Color(46, 46, 59), "coin");
    public static final TETile GOBLIN = new TETile(' ', Color.yellow, new Color(46, 46, 59), "goblin", "Goblin.png");

    public static final TETile GOBLINGIRL = new TETile(' ', Color.yellow, new Color(46, 46, 59), "goblin girl", "Goblin Girl.png");

    public static final TETile GOBLINSPEAR = new TETile(' ', Color.yellow, new Color(46, 46, 59), "goblin", "Goblin Spear.png");

    public static final TETile GOBLINPRIEST = new TETile(' ', Color.yellow, new Color(46, 46, 59), "goblin priest", "Goblin Priest.png");



}


