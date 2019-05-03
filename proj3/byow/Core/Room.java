package byow.Core;

import java.util.ArrayList;
import java.util.Objects;

public class Room {

    int width;
    int height;
    Position position;
    Position center;
    int label;
    boolean visited;

    public Room(int width, int height, int xPos, int yPos, int label) {
        this.width = width;
        this.height = height;
        this.position = new Position(xPos, yPos);
        this.center = new Position(xPos + (width / 2), yPos + (height / 2));
        this.label = label;
        this.visited = false;
    }

    public Room(int width, int height, Position center) {
        this.width = width;
        this.height = height;
        this.center = center;
        this.position = new Position(center.getX() - (width / 2), center.getY() - (height / 2));
        this.label = 0;
    }

    /*
    public Room(int width, int height) {
        this.width = width;
        this.height = height;

    }
    */

    public boolean doesOverlap(Room other) {

        if ((this.position.getY() + this.height) < other.position.getY()
            || this.position.getY() > (other.position.getY() + other.height)) {
            return false;
        }
        if ((this.position.getX() + this.width) < other.position.getX()
            || this.position.getX() > (other.position.getX() + other.width)) {
            return false;
        }

        return true;

        /*
        if (this.position.getX() > other.position.getX()
                && this.position.getX() < (other.position.getX() + other.width)) {
            return true;
        }
        */
        /*
        if (this.position.getY() > other.position.getY()
                && this.position.getY() < (other.position.getY() + other.height)) {
            return true;
        }
        */

    }

    public ArrayList<Room> neighbors(ArrayList<Room> rooms) {
        ArrayList<Room> nbors = new ArrayList<>();
        for (Room r : rooms) {
            if (this.doesOverlap(r)) {
                nbors.add(r);
            }
        }
        return nbors;
    }

    public void moveLeft(int dis) {
        this.position.setX(this.position.getX() - dis);
    }

    public void moveRight(int dis) {
        this.position.setX(this.position.getX() + dis);

    }

    public void moveUp(int dis) {
        this.position.setY(this.position.getY() + dis);

    }

    public void moveDown(int dis) {
        this.position.setY(this.position.getY() - dis);
    }


    public int[] overlapsBy(Room other) {
        if (!doesOverlap(other)) {
            return null;
        }



        int[] overlappingVals = new int[2];

        overlappingVals[0] = Math.min(((this.position.getX() + this.width)
                - other.position.getX()), ((other.position.getX() + other.width)
                - this.position.getX()));

        overlappingVals[1] = Math.min(((this.position.getY() + this.height)
                - other.position.getY()), ((other.position.getY() + this.height)
                - this.position.getY()));





        return overlappingVals;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return width == room.width
                && height == room.height
                && Objects.equals(position, room.position)
                && Objects.equals(center, room.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, position, center);
    }

    public static void main(String[] args) {
        /*
        Room room1 = new Room(2, 2, 0, 0,  0);
        Room room2 = new Room(2, 2, 1, 0,  1);
        Room room3 = new Room(2,2,0,1, 2);
        Room room4 = new Room(2,2,1,1, 3);
        Room room5 = new Room(2,2,2,0, 4);


        System.out.println(room1.overlapsBy(room2)[0]);
        System.out.println(room1.overlapsBy(room2)[1]);
        System.out.println();

        System.out.println(room1.overlapsBy(room3)[0]);
        System.out.println(room1.overlapsBy(room3)[1]);
        System.out.println();


        System.out.println(room1.overlapsBy(room4)[0]);
        System.out.println(room1.overlapsBy(room4)[1]);
        System.out.println();

        System.out.println(room4.overlapsBy(room1)[0]);
        System.out.println(room4.overlapsBy(room1)[1]);
        System.out.println();

        System.out.println(room1.overlapsBy(room5)[0]);
        System.out.println(room1.overlapsBy(room5)[1]);
        System.out.println(room5.overlapsBy(room1)[0]);
        System.out.println(room5.overlapsBy(room1)[1]);

        */

    }
}
