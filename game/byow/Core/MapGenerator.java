package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.*;
import java.util.List;


public class MapGenerator {

    public static final int WIDTH = 75;

    public static final int HEIGHT = 40;

    public long SEED;

    public Random RAND;

    public List<Room> roomList;

    public TETile[][] world;

    public Player player;

    public HashMap<Integer, Coin> coinMap = new HashMap<>();

    public List<Integer> removedList = new ArrayList<>();

    public HashMap<Integer, Enemy> enemyMap = new HashMap<>();

    public List<Integer> removedEnemies = new ArrayList<>();

    public int enemySize;

    public MapGenerator(long seed) {
        SEED = seed;
        RAND = new Random(seed);
        roomList = new ArrayList<>();
        world = new TETile[WIDTH][HEIGHT];
        generateWorld(RAND, world, roomList);
        Room room = roomList.get(0);
        int x_pos = room.x_pos + RAND.nextInt(0, room.width);
        int y_pos = room.y_pos + RAND.nextInt(0, room.height);
        player = new Player("Goblin Slayer", x_pos, y_pos, 0, 5, "Fist");
        movePlayer(x_pos, y_pos, world, "right", true, true);
        addCoins();
        addEnemies();
        enemySize = enemyMap.size();
    }


    public void generateWorld(Random RANDOM, TETile[][] world, List<Room> roomList) {

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        for (int i = 0; i < 100; i += 1) {
            int x = RANDOM.nextInt(WIDTH);
            int y = RANDOM.nextInt(HEIGHT);
            int height = RANDOM.nextInt(3, 8);
            int width = RANDOM.nextInt(3, 8);
            addBox(x, y, world, width, height, roomList);
        }

        connectAll(roomList, world, RANDOM);

        buildWalls(world);

    }

    public void addBox(int x, int y, TETile[][] world, int width, int height, List<Room> roomList) {
        if (width + x > WIDTH - 2 || height + y > HEIGHT - 3 || x < 2 || y < 2) {
            return;
        }
        Room newRoom = new Room(width, height, x, y);
        if (Room.overlaps(newRoom, roomList)) {
            return;
        }
        for (int i = x; i < width + x; i += 1) {
            for (int j = y; j < height + y; j += 1) {
                world[i][j] = Tileset.FLOOR;
            }
        }
        roomList.add(newRoom);
    }

    public void connectAll(List<Room> rooms, TETile[][] world, Random RANDOM) {
        while (rooms.size() > 1) {
            Room room = rooms.remove(0);
            connect(room, closest(room, rooms), world, RANDOM);
        }
    }

    public void connect(Room room1, Room room2, TETile[][] world, Random RANDOM) {
        int x1 = RANDOM.nextInt(room1.x_pos, room1.max_x);
        int y1 = RANDOM.nextInt(room1.y_pos, room1.max_y);
        int x2 = RANDOM.nextInt(room2.x_pos, room2.max_x);
        int y2 = RANDOM.nextInt(room2.y_pos, room2.max_y);
        if (x2 > x1) {
            buildHallRight(x2 - x1, x1, y1, world);
            x1 += x2 - x1;
        }
        if (x2 < x1) {
            buildHallLeft(x1 - x2, x1, y1, world);
            x1 -= x1 - x2;
        }
        if (y2 > y1) {
            buildHallUp(y2 - y1, x1, y1, world);
            y1 += y2 - y1;
        }
        if (y2 < y1) {
            buildHallDown(y1 - y2, x1, y1, world);
        }
    }

    public void buildHallRight(int length, int x, int y, TETile[][] world) {
        for (int i = 0; i < length; i += 1) {
            world[x + i][y] = Tileset.FLOOR;
        }
    }

    public void buildHallLeft(int length, int x, int y, TETile[][] world) {
        for (int i = 0; i < length; i += 1) {
            world[x - i][y] = Tileset.FLOOR;
        }
    }

    public void buildHallUp(int length, int x, int y, TETile[][] world) {
        for (int i = 0; i < length; i += 1) {
            world[x][y + i] = Tileset.FLOOR;
        }
    }

    public void buildHallDown(int length, int x, int y, TETile[][] world) {
        for (int i = 0; i < length; i += 1) {
            world[x][y - i] = Tileset.FLOOR;
        }
    }

    public void buildWalls(TETile[][] world) {
        for (int x = 1; x < WIDTH - 1; x += 1) {
            for (int y = 1; y < HEIGHT - 1; y += 1) {
                if (world[x][y] == Tileset.NOTHING) {
                    if (world[x + 1][y] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    }
                    if (world[x - 1][y] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    }
                    if (world[x][y + 1] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    }
                    if (world[x][y - 1] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    }
                    if (world[x - 1][y - 1] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    }
                    if (world[x + 1][y - 1] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    }
                    if (world[x - 1][y + 1] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    }
                    if (world[x + 1][y + 1] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public Room closest(Room room1, List<Room> rooms) {
        Room closest = rooms.get(0);
        for (Room room: rooms) {
            int hall1 = distance(room1, room);
            int hall2 = distance(room1, closest);
            if (hall1 < hall2) {
                closest = room;
            }
        }
        return closest;
    }

    public int distance(Room room1, Room room2) {
        return Math.abs(room1.x_pos - room2.x_pos) + Math.abs(room1.y_pos - room2.y_pos);
    }

    public void movePlayer(int x, int y, TETile[][] world, String direction, boolean start, boolean animation) {
        TETile oldTile = world[x][y];
        if (!start && !outOfBounds(x, y) && (world[x][y] == Tileset.FLOOR || world[x][y] == Tileset.COIN
                || world[x][y] == Tileset.GOBLIN || world[x][y] == Tileset.GOBLINGIRL
                || world[x][y] == Tileset.GOBLINPRIEST || world[x][y] == Tileset.GOBLINSPEAR)) {
            if (Objects.equals(direction, "up")) {
                world[x][y] = Tileset.AVATARUP;
            }
            if (Objects.equals(direction, "down")) {
                world[x][y] = Tileset.AVATARDOWN;
            }
            if (Objects.equals(direction, "left")) {
                world[x][y] = Tileset.AVATARLEFT;
            }
            if (Objects.equals(direction, "right")) {
                world[x][y] = Tileset.AVATAR;
            }
            world[player.x][player.y] = Tileset.FLOOR;
            player.x = x;
            player.y = y;
        }
        faceDirection(direction);
        if (oldTile == Tileset.COIN) {
            player.coins += 1;
            int coinCode = Objects.hash(List.of(x, y));
            coinMap.remove(coinCode);
            removedList.add(coinCode);
        }
        if (!start && (oldTile == Tileset.GOBLIN || oldTile == Tileset.GOBLINGIRL || oldTile == Tileset.GOBLINSPEAR
                || oldTile == Tileset.GOBLINPRIEST)) {
            boolean remove = fightEnemy();
            if (!remove) {
                player.health -= 1;
            }
            int enemyCode = Objects.hash(List.of(x, y));
            enemyMap.remove(enemyCode);
            removedEnemies.add(enemyCode);
            if (animation) {
                victoryScreen(remove);
            }
        }
    }

    public void victoryScreen(boolean decision) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH * 1.0 / 2, HEIGHT * 1.0 / 2, 21, 11);
        StdDraw.setPenColor(new Color(0, 255, 0));
        StdDraw.filledRectangle(WIDTH * 1.0 / 2, HEIGHT * 1.0 / 2, 20, 10);
        Font fontBig = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH * 1.0 / 2, HEIGHT * 1.0 / 2, "You encountered a goblin and decide to fight...");
        StdDraw.show();
        StdDraw.pause(700);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH * 1.0 / 2, HEIGHT * 1.0 / 2, 21, 11);
        StdDraw.setPenColor(new Color(0, 255, 0));
        StdDraw.filledRectangle(WIDTH * 1.0 / 2, HEIGHT * 1.0 / 2, 20, 10);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(WIDTH * 1.0 / 2, HEIGHT * 1.0 / 2, "Battling..");
        StdDraw.show();
        StdDraw.pause(700);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH * 1.0 / 2, HEIGHT * 1.0 / 2, 21, 11);
        StdDraw.setPenColor(new Color(0, 255, 0));
        StdDraw.filledRectangle(WIDTH * 1.0 / 2, HEIGHT * 1.0 / 2, 20, 10);
        StdDraw.setPenColor(Color.BLACK);
        if (decision) {
            StdDraw.text(WIDTH * 1.0 / 2, HEIGHT * 1.0 / 2, "You have defeated the goblin with your " + player.weapon + "!");
        } else {
            StdDraw.text(WIDTH * 1.0 / 2, HEIGHT * 1.0 / 2, "The goblin deals 1 damage and flees...");
        }
        StdDraw.show();
        StdDraw.pause(700);
    }


    public boolean fightEnemy() {
        int chance;
        if (Objects.equals(player.weapon, "Fist")) {
            chance = RAND.nextInt(0, 15);
            return chance == 14;
        }
        if (Objects.equals(player.weapon, "Stone Sword")) {
            chance = RAND.nextInt(0, 4);
            return chance == 3;
        }
        if (Objects.equals(player.weapon, "Iron Sword")) {
            chance = RAND.nextInt(0, 2);
            return chance == 1;
        }
        chance = RAND.nextInt(0, 4);
        return chance != 0;
    }

    public boolean noEnemies() {
        return enemyMap.isEmpty();
    }

    public void faceDirection(String way) {
        if (Objects.equals(way, "up")) {
            world[player.x][player.y] = Tileset.AVATARUP;
        }
        if (Objects.equals(way, "down")) {
            world[player.x][player.y] = Tileset.AVATARDOWN;
        }
        if (Objects.equals(way, "left")) {
            world[player.x][player.y] = Tileset.AVATARLEFT;
        }
        if (Objects.equals(way, "right")) {
            world[player.x][player.y] = Tileset.AVATAR;
        }
    }


    public Boolean outOfBounds(int x, int y) {
        if (x < 0 || x >= WIDTH) {
            return true;
        }
        return y < 0 || y >= HEIGHT;
    }

    public void addCoins() {
        for (int x = 1; x < WIDTH - 1; x += 1) {
            for (int y = 1; y < HEIGHT - 1; y += 1) {
                int chance = RAND.nextInt(1, 21);
                if (world[x][y] == Tileset.FLOOR && chance == 20) {
                    world[x][y] = Tileset.COIN;
                    int id = Objects.hash(List.of(x, y));
                    Coin coin = new Coin(id, x, y);
                    coinMap.put(id, coin);
                }
            }
        }
    }

    public void addEnemies() {
        for (int x = 1; x < WIDTH - 1; x += 1) {
            for (int y = 1; y < HEIGHT - 1; y += 1) {
                int chance = RAND.nextInt(0, 50);
                if (world[x][y] == Tileset.FLOOR && chance == 49) {
                    int type = RAND.nextInt(0, 4);
                    if (type == 0) {
                        world[x][y] = Tileset.GOBLIN;
                    }
                    if (type == 1) {
                        world[x][y] = Tileset.GOBLINSPEAR;
                    }
                    if (type == 2) {
                        world[x][y] = Tileset.GOBLINGIRL;
                    }
                    if (type == 3) {
                        world[x][y] = Tileset.GOBLINPRIEST;
                    }
                    int id = Objects.hash(List.of(x, y));
                    Enemy enemy = new Enemy(id, x, y);
                    enemyMap.put(id, enemy);
                }
            }
        }
    }



}
