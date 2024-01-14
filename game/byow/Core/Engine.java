package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 75;
    public static final int HEIGHT = 40;

    public static final int mainWidth = 75;

    public static final int mainHeight = 40;

    public TETile[][] save = null;

    public boolean worldExists = false;

    public MapGenerator world = null;

    public boolean gameOver = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        StdDraw.setCanvasSize(mainWidth * 16, mainHeight * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, mainWidth);
        StdDraw.setYscale(0, mainHeight);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        drawMain("main");
        char input = getInput(1);
        if (input == 'n' || input == 'N') {
            drawMain("seed");
        }
        if (input == 'q' || input == 'Q') {
            System.exit(0);
            return;
        }
        if (input == 'l' || input == 'L') {
            List<String> steps = new ArrayList<>();
            List<String> enemyGone = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
                BufferedReader reader2 = new BufferedReader(new FileReader("locations.txt"));
                String move;
                while((move = reader.readLine()) != null) {
                    steps.add(move);
                }
                reader.close();
                String enemy;
                while((enemy = reader2.readLine()) != null) {
                    enemyGone.add(enemy);
                }
                reader2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            long saveSeed = Long.parseLong(steps.get(0));
            MapGenerator loadMap = new MapGenerator(saveSeed);
            String[] split = steps.get(3).split(" ");
            int old_x = loadMap.player.x;
            int old_y = loadMap.player.y;
            loadMap.player.y = Integer.parseInt(steps.get(2));
            loadMap.player.x = Integer.parseInt(steps.get(1));
            loadMap.player.coins = Integer.parseInt(steps.get(4));
            loadMap.player.health = Integer.parseInt(steps.get(5));
            loadMap.player.weapon = steps.get(6);
            loadMap.player.name = enemyGone.get(0);
            if (steps.size() > 7) {
                for (int i = 7; i < steps.size(); i += 1) {
                    int x = loadMap.coinMap.get(Integer.parseInt(steps.get(i))).x;
                    int y = loadMap.coinMap.get(Integer.parseInt(steps.get(i))).y;
                    loadMap.removedList.add(Integer.parseInt(steps.get(i)));
                    loadMap.world[x][y] = Tileset.FLOOR;
                }
            }
            for (String id: enemyGone) {
                if (!Objects.equals(id, loadMap.player.name)) {
                    int x = loadMap.enemyMap.get(Integer.parseInt(id)).x;
                    int y = loadMap.enemyMap.get(Integer.parseInt(id)).y;
                    loadMap.removedEnemies.add(Integer.parseInt(id));
                    loadMap.world[x][y] = Tileset.FLOOR;
                }
            }
            if (Objects.equals(split[0], "up")) {
                loadMap.world[loadMap.player.x][loadMap.player.y] = Tileset.AVATARUP;
            }
            if (Objects.equals(split[0], "down")) {
                loadMap.world[loadMap.player.x][loadMap.player.y] = Tileset.AVATARDOWN;
            }
            if (Objects.equals(split[0], "left")) {
                loadMap.world[loadMap.player.x][loadMap.player.y] = Tileset.AVATARLEFT;
            }
            if (Objects.equals(split[0], "right")) {
                loadMap.world[loadMap.player.x][loadMap.player.y] = Tileset.AVATAR;
            }
            loadMap.world[old_x][old_y] = Tileset.FLOOR;
            updateWorld(loadMap, true);
        }
    }

    public void drawMain(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 50);
        Font fontSmall = new Font("Monaco", Font.BOLD, 22);
        StdDraw.picture(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, "GoblinSlayer.jpg");
        if (Objects.equals(s, "main")) {
            StdDraw.setFont(fontBig);
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 10, "GOBLIN EXTERMINATOR");
            StdDraw.setFont(fontSmall);
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, "New Game (N)");
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 2, "Load Game (L)");
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 4, "Quit (Q)");
            StdDraw.show();
            return;
        }
        StdDraw.setFont(fontSmall);
        StdDraw.text( mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 8, "Enter your name:");
        StdDraw.text( mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 3, "Press (1) to confirm");
        StdDraw.show();
        String name = typedName();
        StdDraw.clear(Color.BLACK);
        StdDraw.picture(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, "GoblinSlayer.jpg");
        StdDraw.text( mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 8, "Enter a seed:");
        StdDraw.text( mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 3, "Press (s) to load world");
        StdDraw.show();
        long seed = typedInput(19);
        world = new MapGenerator(seed);
        world.player.name = name;
        updateWorld(world, true);
    }

    public String typedName() {
        StringBuilder name = new StringBuilder();
        while (!gameOver) {
            if (StdDraw.hasNextKeyTyped()) {
                char userInput = StdDraw.nextKeyTyped();
                if (userInput == '1') {
                    break;
                }
                StdDraw.picture(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, "GoblinSlayer.jpg");
                StdDraw.text( mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 8, "Enter your name:");
                StdDraw.text( mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 3, "Press (1) to confirm");
                if (Character.isLetter(userInput)) {
                    name.append(userInput);
                    StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 2.5, name.toString());
                    StdDraw.show();
                }
                StdDraw.clear(StdDraw.BLACK);
            }
        }
        return name.toString();
    }

    public void updateWorld(MapGenerator map, boolean turnOn) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(map.world, "", map.player.coins, map.player.health, map.player.x, map.player.y, turnOn, map.player.name);
        String object = "";
        char prev = ' ';
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"));
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("locations.txt"));
            while (!gameOver) {
                if (map.player.health < 1) {
                    gameoverScreen("lose");
                }
                if (map.removedEnemies.size() == map.enemySize) {
                    gameoverScreen("win");
                }
                if (StdDraw.hasNextKeyTyped()) {
                    char userInput = StdDraw.nextKeyTyped();
                    if (userInput == 'l' || userInput == 'L') {
                        turnOn = !turnOn;
                    }
                    if (userInput == 'w' || userInput == 'W') {
                        map.movePlayer(map.player.x, map.player.y + 1, map.world, "up", false, true);
                        prev = userInput;
                        continue;
                    }
                    if (userInput == 's' || userInput == 'S') {
                        map.movePlayer(map.player.x, map.player.y - 1, map.world, "down", false, true);
                        prev = userInput;
                        continue;
                    }
                    if (userInput == 'a' || userInput == 'A') {
                        map.movePlayer(map.player.x - 1, map.player.y, map.world, "left", false, true);
                        prev = userInput;
                        continue;
                    }
                    if (userInput == 'd' || userInput == 'D') {
                        map.movePlayer(map.player.x + 1, map.player.y, map.world, "right", false, true);
                        prev = userInput;
                        continue;
                    }
                    if (userInput == 'h' || userInput == 'H') {
                        showShop(map);
                    }
                    if (prev == ':' && (userInput == 'q' || userInput == 'Q')) {
                        writer.write(Long.toString(map.SEED));
                        writer2.write(map.player.name);
                        writer.write("\n" + map.player.x);
                        writer.write("\n" + map.player.y);
                        writer.write("\n" + map.world[map.player.x][map.player.y].description());
                        writer.write("\n" + map.player.coins);
                        writer.write("\n" + map.player.health);
                        writer.write("\n" + map.player.weapon);
                        for (Integer coin: map.removedList) {
                            writer.write("\n" + coin);
                        }
                        for (Integer ID: map.removedEnemies) {
                            writer2.write("\n" + ID);
                        }
                        writer2.close();
                        writer.close();
                        System.exit(0);
                    }
                    prev = userInput;
                }
                int x = (int) Math.floor(StdDraw.mouseX());
                int y = (int) Math.floor(StdDraw.mouseY());
                if ((x >= 0 && x <= 74) && (y >= 0 && y <= 39)) {
                    object = map.world[x][y].description();
                }
                ter.renderFrame(map.world, object, map.player.coins, map.player.health, map.player.x, map.player.y, turnOn, map.player.name);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public long typedInput(int n) {
        StringBuilder word = new StringBuilder();
        int count = 0;
        while (count < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char userInput = StdDraw.nextKeyTyped();
                if (userInput == 's' || userInput == 'S') {
                    break;
                }
                StdDraw.picture(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, "GoblinSlayer.jpg");
                StdDraw.text( mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 8, "Enter a seed:");
                StdDraw.text( mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 3, "Press s to load world");
                if (Character.isDigit(userInput)) {
                    word.append(userInput);
                    StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 2.5, word.toString());
                    StdDraw.show();
                    count += 1;
                }
                StdDraw.clear(StdDraw.BLACK);
            }
        }
        return Long.parseLong(word.toString());
    }

    public char getInput(int n) {
        int count = 0;
        char unicode = ' ';
        while (count < n) {
            if (StdDraw.hasNextKeyTyped()) {
                unicode = StdDraw.nextKeyTyped();
                count += 1;
            }
        }
        return unicode;
    }

    public void showShop(MapGenerator map) {
        while(!gameOver) {
            StdDraw.setPenColor(Color.white);
            StdDraw.filledRectangle(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, 21, 11);
            StdDraw.setPenColor(new Color(235, 215, 141));
            StdDraw.filledRectangle(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, 20, 10);
            Font fontSmall = new Font("Monaco", Font.BOLD, 18);
            Font fontBig = new Font("Monaco", Font.BOLD, 22);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setFont(fontBig);
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 8, "Welcome to the shop!");
            StdDraw.setFont(fontSmall);
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 6, "Purchase a weapon to increase your win rate or press (y) to leave");
            StdDraw.text(mainWidth * 1.0 / 2 - 15, mainHeight * 1.0 / 2 - 4, "Stone Sword");
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 4, "Iron Sword");
            StdDraw.text(mainWidth * 1.0 / 2 + 15, mainHeight * 1.0 / 2 - 4, "Titanium Sword");
            StdDraw.picture(mainWidth * 1.0 / 2 - 15, mainHeight * 1.0 / 2 + 2, "Stone Sword.png");
            StdDraw.picture(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 2, "Iron Sword.png");
            StdDraw.picture(mainWidth * 1.0 / 2 + 15, mainHeight * 1.0 / 2 + 2, "Titanium Sword.png");
            StdDraw.text(mainWidth * 1.0 / 2 - 15, mainHeight * 1.0 / 2 - 6, "25% Chance");
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 6, "50% Chance");
            StdDraw.text(mainWidth * 1.0 / 2 + 15, mainHeight * 1.0 / 2 - 6, "75% Chance");
            StdDraw.text(mainWidth * 1.0 / 2 - 15, mainHeight * 1.0 / 2 - 7, "5 Coins");
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 7, "10 Coins");
            StdDraw.text(mainWidth * 1.0 / 2 + 15, mainHeight * 1.0 / 2 - 7, "15 Coins");
            StdDraw.text(mainWidth * 1.0 / 2 - 15, mainHeight * 1.0 / 2 - 8, "(z) to buy");
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 8, "(x) to buy");
            StdDraw.text(mainWidth * 1.0 / 2 + 15, mainHeight * 1.0 / 2 - 8, "(c) to buy");
            Font fontSmaller = new Font("Monaco", Font.BOLD, 12);
            StdDraw.setFont(fontSmaller);
            StdDraw.textRight(mainWidth * 1.0 / 2 + 20, mainHeight * 1.0 / 2 - 9.5, "(n) next page ");
            if (StdDraw.hasNextKeyTyped()) {
                char userInput = StdDraw.nextKeyTyped();
                if (userInput == 'y' || userInput == 'Y') {
                    return;
                }
                if (userInput == 'z' || userInput == 'Z') {
                    if (map.player.coins >= 5) {
                        map.player.weapon = "Stone Sword";
                        map.player.coins -= 5;
                        purchasedWeapon("Stone Sword");
                    } else {
                        insufficientFunds(5 - map.player.coins);
                    }
                    return;
                }
                if (userInput == 'x' || userInput == 'X') {
                    if (map.player.coins >= 10) {
                        map.player.weapon = "Iron Sword";
                        map.player.coins -= 10;
                        purchasedWeapon("Iron Sword");
                    } else {
                        insufficientFunds(10 - map.player.coins);
                    }
                    return;
                }
                if (userInput == 'c' || userInput == 'C') {
                    if (map.player.coins >= 15) {
                        map.player.weapon = "Titanium Sword";
                        map.player.coins -= 15;
                        purchasedWeapon("Titanium Sword");
                    } else {
                        insufficientFunds(15 - map.player.coins);
                    }
                    return;
                }
                if (userInput == 'n' || userInput == 'N') {
                    char newInput = nextShop(map);
                    if (newInput == 'v' || newInput == 'V') {
                        return;
                    }
                    if (newInput == 'b' || newInput == 'B') {
                        continue;
                    }
                }
            }
            StdDraw.show();
        }
    }

    public char nextShop(MapGenerator map) {
        StdDraw.setPenColor(Color.white);
        StdDraw.filledRectangle(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, 21, 11);
        StdDraw.setPenColor(new Color(235, 215, 141));
        StdDraw.filledRectangle(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, 20, 10);
        Font fontSmall = new Font("Monaco", Font.BOLD, 18);
        Font fontBig = new Font("Monaco", Font.BOLD, 22);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(fontBig);
        StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 8, "Welcome to the shop!");
        StdDraw.setFont(fontSmall);
        StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 6, "Purchase a health potion or press (b) to go back");
        StdDraw.picture(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 1, "Health.png");
        StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 4, "Health Potion");
        StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 6, "2 Coins");
        StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 8, "(v) to buy");
        while(!gameOver) {
            if (StdDraw.hasNextKeyTyped()) {
                char userInput = StdDraw.nextKeyTyped();
                if (userInput == 'b' || userInput == 'B') {
                    break;
                }
                if (userInput == 'v' || userInput == 'V') {
                    if (map.player.coins >= 2) {
                        map.player.coins -= 2;
                        map.player.health += 1;
                        purchasedWeapon("Health Potion");
                        return userInput;
                    } else {
                        insufficientFunds(2 - map.player.coins);
                    }
                    return userInput;
                }
            }
            StdDraw.show();
        }
        return 'b';
    }

    public void insufficientFunds(int remaining) {
        StdDraw.setPenColor(Color.white);
        StdDraw.filledRectangle(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, 21, 11);
        StdDraw.setPenColor(new Color(235, 215, 141));
        StdDraw.filledRectangle(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, 20, 10);
        Font fontSmall = new Font("Monaco", Font.BOLD, 18);
        Font fontBig = new Font("Monaco", Font.BOLD, 22);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(fontBig);
        StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 1, "Insufficient Funds!");
        StdDraw.setFont(fontSmall);
        StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 1, "You need " + remaining + " more coins");
        StdDraw.show();
        StdDraw.pause(1000);
    }

    public void purchasedWeapon(String weapon) {
        StdDraw.setPenColor(Color.white);
        StdDraw.filledRectangle(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, 21, 11);
        StdDraw.setPenColor(new Color(235, 215, 141));
        StdDraw.filledRectangle(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, 20, 10);
        Font fontBig = new Font("Monaco", Font.BOLD, 22);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(fontBig);
        StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, "You Purchased: " + weapon);
        StdDraw.show();
        StdDraw.pause(1000);
    }


    public void gameoverScreen(String result) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, 21, 11);
        StdDraw.setPenColor(new Color(0, 255, 0));
        StdDraw.filledRectangle(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, 20, 10);
        Font fontBig = new Font("Monaco", Font.BOLD, 25);
        Font fontSmall = new Font("Monaco", Font.BOLD, 25);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(fontBig);
        if (Objects.equals(result, "lose")) {
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 1.5, "DEFEAT");
            StdDraw.setFont(fontSmall);
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, "Long live the goblins!");
        } else {
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 + 1.5, "VICTORY");
            StdDraw.setFont(fontSmall);
            StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2, "You have vanquished the goblins!");
        }
        StdDraw.text(mainWidth * 1.0 / 2, mainHeight * 1.0 / 2 - 1.5, "Press (q) to quit");
        while (!gameOver) {
            if (StdDraw.hasNextKeyTyped()) {
                char userInput = StdDraw.nextKeyTyped();
                if (userInput == 'q' || userInput == 'Q') {
                    System.exit(0);
                }
            }
            StdDraw.show();
        }
    }

    public TETile[][] interactWithInputString(String input) {
        List<String> inputList = readWorld(input);
        for (int i = 0; i < inputList.size(); i += 1) {
            if (Objects.equals(inputList.get(i), "l") || Objects.equals(inputList.get(i), "L")) {
                world.world = save;
                continue;
            }
            if ((Objects.equals(inputList.get(i), "q") || Objects.equals(inputList.get(i), "Q")) && Objects.equals(inputList.get(i - 1), ":")) {
                save = world.world;
                break;
            }
            if (Objects.equals(inputList.get(i), "w") || Objects.equals(inputList.get(i), "W")) {
                world.movePlayer(world.player.x, world.player.y + 1, world.world, "up", false, false);
                continue;
            }
            if (Objects.equals(inputList.get(i), "s") || Objects.equals(inputList.get(i), "S")) {
                world.movePlayer(world.player.x, world.player.y - 1, world.world, "down", false, false);
                continue;
            }
            if (Objects.equals(inputList.get(i), "a") || Objects.equals(inputList.get(i), "A")) {
                world.movePlayer(world.player.x - 1, world.player.y, world.world, "left", false, false);
                continue;
            }
            if (Objects.equals(inputList.get(i), "d") || Objects.equals(inputList.get(i), "D")) {
                world.movePlayer(world.player.x + 1, world.player.y, world.world, "right", false, false);
                continue;
            }
            if (inputList.get(i).length() > 1) {
                world.player.name = inputList.get(i);
                continue;
            }
            if ((Objects.equals(inputList.get(i), "y") || Objects.equals(inputList.get(i), "Y"))
                    && (Objects.equals(inputList.get(i - 1), "h") || Objects.equals(inputList.get(i - 1), "H"))) {
                continue;
            }
            if ((Objects.equals(inputList.get(i), "z") || Objects.equals(inputList.get(i), "Z"))
                    && (Objects.equals(inputList.get(i - 1), "h") || Objects.equals(inputList.get(i - 1), "H"))) {
                if (world.player.coins >= 5) {
                    world.player.weapon = "Stone Sword";
                    world.player.coins -= 5;
                    continue;
                }
            }
            if ((Objects.equals(inputList.get(i), "x") || Objects.equals(inputList.get(i), "X"))
                    && (Objects.equals(inputList.get(i - 1), "h") || Objects.equals(inputList.get(i - 1), "H"))) {
                if (world.player.coins >= 10) {
                    world.player.weapon = "Iron Sword";
                    world.player.coins -= 10;
                    continue;
                }
            }
            if ((Objects.equals(inputList.get(i), "c") || Objects.equals(inputList.get(i), "C"))
                    && (Objects.equals(inputList.get(i - 1), "h") || Objects.equals(inputList.get(i - 1), "H"))) {
                if (world.player.coins >= 15) {
                    world.player.weapon = "Titanium Sword";
                    world.player.coins -= 15;
                }
            }
            if ((Objects.equals(inputList.get(i), "v") || Objects.equals(inputList.get(i), "V"))
                    && (Objects.equals(inputList.get(i - 1), "n") || Objects.equals(inputList.get(i - 1), "N"))) {
                if (world.player.coins >= 2) {
                    world.player.health += 1;
                    world.player.coins -= 2;
                }
            }
        }
        return world.world;
    }

    public List<String> readWorld(String input) {
        boolean n = false;
        boolean s = false;
        boolean one = false;
        StringBuilder seed = new StringBuilder();
        List<Character> values = toList(input.toCharArray());
        List<String> remaining = new ArrayList<>();
        StringBuilder name = new StringBuilder();
        int index = 0;
        for (int i = 0; i < values.size(); i += 1) {
            if (n && !s && values.get(i) != '1') {
                if (!one && Character.isLetter(values.get(i))) {
                    name.append(values.get(i));
                    continue;
                }
            }
            if (n && !s && !one && values.get(i) == '1') {
                one = true;
                continue;
            }
            if (!n && (values.get(i) == 'n' || values.get(i) == 'N')) {
                n = true;
                continue;
            }
            if (!s && (values.get(i) == 's' || values.get(i) == 'S')) {
                s = true;
                index = i + 1;
                continue;
            }
            if (n && one && !s && Character.isDigit(values.get(i))) {
                seed.append(values.get(i));
                continue;
            }
            if (n && s) {
                break;
            }
        }
        for (int j = index; j < values.size(); j += 1) {
            remaining.add(values.get(j).toString());
        }
        remaining.add(name.toString());
        if (!worldExists) {
            long newSeed = Long.parseLong(String.valueOf(seed));
            world = new MapGenerator(newSeed);
            worldExists = true;
        }
        return remaining;
    }

    public List<Character> toList(char[] array) {
        List<Character> values = new ArrayList<>();
        for (char character : array) {
            values.add(character);
        }
        return values;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Engine game = new Engine();
        game.interactWithInputString("nshibo1637sdddsdwwwwddddddddddddddddddddddddddshz");
        ter.renderFrame(game.world.world, "", game.world.player.coins, game.world.player.health, game.world.player.x, game.world.player.y, false, game.world.player.name);
    }


}
