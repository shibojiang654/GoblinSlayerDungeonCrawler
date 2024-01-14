package byow.Core;

import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

public class Room {

    public int width;

    public int height;

    public int x_pos;

    public int y_pos;

    public int max_x;

    public int max_y;

    public Room(int w, int h, int x, int y) {
        width = w;
        height = h;
        x_pos = x;
        y_pos = y;
        max_x = x_pos + width;
        max_y = y_pos + height;
    }

    public static Boolean overlaps(Room newRoom, List<Room> rooms) {
        for (Room room: rooms) {
            Boolean x_range = inRange(newRoom.x_pos, newRoom.x_pos + newRoom.width, room.x_pos, room.x_pos + room.width);
            Boolean y_range = inRange(newRoom.y_pos, newRoom.y_pos + newRoom.height, room.y_pos, room.y_pos + room.height);
            if (x_range && y_range) {
                return true;
            }
        }
        return false;
    }

    public static Boolean inRange(int min_room1, int max_room1, int min_room2, int max_room2) {
        return max_room1 >= min_room2 - 1 && max_room2 >= min_room1 - 1;
    }



}
