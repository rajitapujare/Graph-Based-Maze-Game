package byow.Delaunay;

import java.util.ArrayList;
import java.util.HashMap;

import byow.Core.Room;

public class UnionFind {

    public class SetElement {
        SetElement parent;
        Room room;
        int size;

        public SetElement(Room room) {
            this.room = room;
            parent = this;
            size = 1;
        }

        @Override
        public String toString() {
            return String.format("{0}, size:{1}", room, size);
        }
    }

    HashMap<Room, SetElement> dict;

    public UnionFind(ArrayList<Room> rms) {
        dict = new HashMap<Room, SetElement>();
        for (Room r : rms) {
            makeSet(r);
        }
    }

    public SetElement makeSet(Room r1) {
        SetElement element = new SetElement(r1);
        dict.put(r1, element);
        return element;
    }

    public SetElement find(Room r1) {
        SetElement element = dict.get(r1);
        SetElement root = element;
        while (!root.parent.equals(root)) {
            root = root.parent;
        }
        SetElement z = element;
        while (!z.parent.equals(z)) {
            SetElement temp = z;
            z = z.parent;
            temp.parent = root;
        }
        return root;
    }

    public SetElement union(SetElement root1, SetElement root2) {
        if (root2.size > root1.size) {
            root2.size += root1.size;
            root1.parent = root2;
            return root2;
        } else {
            root1.size += root2.size;
            root2.parent = root1;
            return root1;
        }
    }

    public SetElement union(Room room1, Room room2) {
        SetElement s1 = dict.get(room1);
        SetElement s2 = dict.get(room2);
        return union(s1, s2);

    }

    public boolean connected(Room v1, Room v2) {
        SetElement root1 = find(v1);
        SetElement root2 = find(v2);
        return (root1.equals(root2));
    }




}
