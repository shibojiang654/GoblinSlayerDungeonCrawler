package byow.Core;

public class Player {
    public int x;

    public int y;

    public int coins;

    public int health;

    public String weapon;

    public String name;

    public Player(String username, int x_pos, int y_pos, int coinCount, int hp, String wep) {
        x = x_pos;
        y = y_pos;
        coins = coinCount;
        health = hp;
        weapon = wep;
        name = username;
    }

}

