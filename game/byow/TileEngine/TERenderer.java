package byow.TileEngine;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.awt.Font;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world, String text, int coinCount, int hp, int x_pos, int y_pos, boolean lights, String username) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }
        StdDraw.setPenColor(Color.BLACK);
        int rightBlock = width - (x_pos + 7);
        int leftBlock = x_pos - 5;
        int topBlock = height - (y_pos + 7);
        int botBlock = y_pos - 5;
        if (lights) {
            if (rightBlock >= 0) {
                StdDraw.filledRectangle(74, height * 1.0 / 2, Math.abs(width - (x_pos + 7)), 39);
            }
            if (leftBlock >= 0) {
                StdDraw.filledRectangle(0, height * 1.0 / 2, Math.abs(x_pos - 5), 39);
            }
            if (topBlock >= 0) {
                StdDraw.filledRectangle(width * 1.0 / 2, height - 1, 74, Math.abs(height - (y_pos + 7)));
            }
            if (botBlock >= 0) {
                StdDraw.filledRectangle(width * 1.0 / 2, 0, 74, Math.abs(y_pos - 5));
            }
        }
        DateTimeFormatter form = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        StdDraw.setPenColor(new Color(0, 255, 0));
        Font fontSmall = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(fontSmall);
        StdDraw.textLeft(0.5, height - 1, text);
        StdDraw.text(((width - 0.5) / 2) / 2, height - 1, String.valueOf("Time: " + form.format(time)));
        StdDraw.text((74.5 - (width * 1.0 / 2) / 2), height - 1, "Name: " + username);
        StdDraw.textRight(74.5, height - 1, "Coins: " + coinCount);
        StdDraw.text(width * 1.0 / 2, height - 1, "Health: " + "♡".repeat(Math.max(0, hp)));
        StdDraw.show();
    }
}
