package byow.Core;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class KDTree implements PointSet {
    private ArrayList<Room> rooms;
    private Node root;
    //private int depth;

    private class Node implements Comparable<Node> {
        Room room;
        Node leftChild;
        Node rightChild;
        int depth;
        //int min;
        //int max;

        Node(Room room) {
            this.room = room;
            depth = 0;
        }

        Node(Room room, int depth) {
            this.room = room;
            this.depth = depth;
        }

        double distance(Room goal) {
            return Position.distance(this.room.position, goal.position);

            //return Math.min(Position.distance(this.room.center, goal.center),
                    //Position.distance(this.room.position, goal.position));
        }

        @Override
        public int compareTo(Node other) {
            if (other == null || other.room == null) {
                return -1;
            }
            if (depth % 2 == 0) {
                return Double.compare(this.room.position.getX(), other.room.position.getX());
            } else {
                return Double.compare(this.room.position.getY(), other.room.position.getY());

            }
        }



    }

    public KDTree(List<Room> rms) {
        rooms = new ArrayList<>();
        //depth = 0;
        for (Room p : rms) {
            rooms.add(p);
        }
        Collections.shuffle(rooms);
        root = null;
        for (Room p : rooms) {
            add(p);
        }
    }

    public Room nearest(Room toCheck) {
        return (nearest(root, toCheck, root)).room;
    }

    public Room nearest(int x, int y) {
        Room goal = new Room(1, 1, x, y, 0);
        return (nearest(root, goal, root)).room;
    }

    /**
     * Private nearest helper function
     * @param n current node
     * @param goal goal point
     * @param best closest node
     */
    private Node nearest(Node n, Room goal, Node best) {
        if (n == null) {
            return best;
        }
        //System.out.println(n.point);
        Node goodSide;
        Node badSide;
        if ((n.distance(goal) < best.distance(goal))) {
            best = n;
        }

        Node temp = new Node(goal);
        if (n.compareTo(temp) > 0) {
            goodSide = n.leftChild;
            badSide = n.rightChild;
        } else {
            goodSide = n.rightChild;
            badSide = n.leftChild;
        }

        best = nearest(goodSide, goal, best);
        double distance;
        //Point bestBadSidePoint;

        if (n.depth % 2 == 0) {
            //bestBadSidePoint = new Point(n.point.getX(), goal.getY());
            distance = goal.position.getX() - n.room.position.getX();
        } else {
            //bestBadSidePoint = new Point(goal.getX(), n.point.getY());
            distance = goal.position.getY() - n.room.position.getY();
        }

        if (Math.pow(distance, 2) <= best.distance(goal)) {
            best = nearest(badSide, goal, best);
        }
        return best;
    }



    public void add(Room p) {
        if (p == null) {
            throw new IllegalArgumentException("calls add with a null point");
        }
        root = add(root, p, 0);
    }

    private Node add(Node prev, Room r, int depth) {
        Node newNode = new Node(r, depth);
        if (prev == null) {
            //depth += 1;
            return newNode;
        }
        if (depth % 2 == 0) {
            if (r.position.getX() < prev.room.position.getX()) {
                prev.leftChild = add(prev.leftChild, r, depth + 1);
            } else if (r.position.getY() != prev.room.position.getY()) {
                prev.rightChild = add(prev.rightChild, r, depth + 1);
            } else {
                prev.room = r;
                prev.depth = depth;
            }
        }

        if (depth % 2 != 0) {
            if (r.position.getY() < prev.room.position.getY()) {
                prev.leftChild = add(prev.leftChild, r, depth + 1);
            } else if (r.position.getX() != prev.room.position.getX()) {
                prev.rightChild = add(prev.rightChild, r, depth + 1);
            } else {
                prev.room = r;
                prev.depth = depth;
            }
        }
        return prev;

    }

    public static void main(String[] args) {

        Room p1 = new Room(1, 2, 1, 2, 0); // constructs a Point with x = 1, y = 2
        Room p2 = new Room(3, 4, 3, 4, 1);
        Room p3 = new Room(2, 4, 2, 4, 2);

        KDTree nn = new KDTree(List.of(p1, p2, p3));
        Room ret = nn.nearest(3, 4); // returns p2
        System.out.println(ret.position.getX()); // evaluates to 3
        System.out.println(ret.position.getY()); // evaluates to 4


    }
}
