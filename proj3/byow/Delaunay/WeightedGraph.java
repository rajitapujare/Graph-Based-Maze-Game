package byow.Delaunay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import byow.Core.Room;
import byow.Core.WeightedEdge;

public class WeightedGraph {

    //int vertices;
    ArrayList<Room> rooms;
    HashMap<Room, ArrayList<WeightedEdge>> adjacencyLists;
    ArrayList<Room> addedRooms;

    public WeightedGraph(ArrayList<Room> rooms) {
        this.rooms = rooms;
        this.addedRooms = new ArrayList<>();
        this.adjacencyLists = new HashMap<>();
        for (Room r : rooms) {
            adjacencyLists.put(r, new ArrayList<>());
            for (Room roomtoAdd : rooms) {
                if (!roomtoAdd.equals(r)) {
                    addEdge(r, roomtoAdd);
                }
            }

        }

    }

    public void addEdge(Room source, Room dest) {
        WeightedEdge edge = new WeightedEdge(source, dest);
        adjacencyLists.get(source).add(edge);
        addedRooms.add(source);
        addedRooms.add(dest);
    }


    public ArrayList<WeightedEdge> neighbors(Room r) {
        return adjacencyLists.get(r);
    }

    public HashSet<WeightedEdge> allEdges() {

        HashSet<WeightedEdge> allTotalEdges = new HashSet<>();
        for (Room r : rooms) {
            ArrayList<WeightedEdge> edges = adjacencyLists.get(r);
            for (WeightedEdge w : edges) {
                allTotalEdges.add(w);
            }
        }

        return allTotalEdges;

    }



}
