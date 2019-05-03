/**
 * Creates minimal spanning tree using Kruskal's algorithm.
 */
package byow.Delaunay;

import byow.Core.Room;
import byow.Core.WeightedEdge;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Queue;


public class MST {



    private HashMap<Room, WeightedEdge> edgeTo;
    private HashMap<Room, Double> distTo;
    private HashMap<Room, Boolean> marked;
    DoubleMapPQ<Room> pq;

    ArrayList<WeightedEdge> edges;
    ArrayList<Room> rooms;

    private double FLOATING_POINT_EPSILON = 1E-12;

    public MST(ArrayList<Room> rooms, WeightedGraph wdg) {
        this.rooms = wdg.rooms;
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        marked = new HashMap<>();
        pq = new DoubleMapPQ<>();

        for (Room r: wdg.rooms) {
            distTo.put(r, Double.POSITIVE_INFINITY);
            marked.put(r, false);
        }

        for (Room r: wdg.rooms) {
            if (!marked.get(r)) {
                prim(wdg, r);
            }
        }
        assert (check(wdg));
        //sort(edges);
    }

    /**
     * Run prim's
     * @param wdg
     * @param r
     */
    private void prim(WeightedGraph wdg, Room r) {
        distTo.put(r, 0.0);
        pq.add(r, distTo.get(r));
        while (!pq.isEmpty()) {
            Room smallest = pq.removeSmallest();
            scan(wdg, smallest);
        }
    }

    /**
     * Scan's vertex
     */
    private void scan(WeightedGraph wdg, Room r) {
        marked.put(r, true);
        for (WeightedEdge d : wdg.neighbors(r)) {
            Room otherRoom = d.to();
            if (r.equals(otherRoom)) {
                otherRoom = d.from();
            }

            if (marked.get(otherRoom)) {
                continue;
            }
            if (d.weight() < distTo.get(otherRoom)) {
                distTo.put(otherRoom, d.weight());
                edgeTo.put(otherRoom, d);
                if (pq.contains(otherRoom)) {
                    pq.changePriority(otherRoom, distTo.get(otherRoom));
                } else {
                    pq.add(otherRoom, distTo.get(otherRoom));
                }
            }
        }
    }



    /**
     * Return all edges in MST
     * @return edges
     */
    public Iterable<WeightedEdge> edges() {
        Queue<WeightedEdge> mst = new LinkedList<>();
        for (Room r : rooms) {
            WeightedEdge e = edgeTo.get(r);
            if (e != null) {
                mst.add(e);
            }
        }
        return mst;
    }

    /**
    Return sum of edge weights in MST
     **/
    public double weight() {
        double weight = 0.0;
        for (WeightedEdge e : edges()) {
            weight += e.weight();
        }
        return weight;
    }

    public void sort(HashSet<WeightedEdge> e) {
        edges = new ArrayList<>(e);
        Collections.sort(edges);
    }

    public HashSet<WeightedEdge> getMST() {
        HashSet<WeightedEdge> edges1 = new HashSet<>();
        for (WeightedEdge d : edges()) {
            edges1.add(d);
        }
        sort(edges1);
        return edges1;
    }

    /*
    public HashSet<WeightedEdge> getMST() {
        ArrayList<HashSet<Room>> parts = new ArrayList<HashSet<Room>>();
        for (Room r : rooms)  {
            HashSet<Room> set = new HashSet<>();
            set.add(r);
            parts.add(set);
        }

        HashSet<WeightedEdge> mst = new HashSet<>();
        for (int i = 0; i < edges.size(); i++) {
            Room src = edges.get(i).from();
            Room dest = edges.get(i).to();
            int indexOfp = 0;
            int indexOfq = 0;

            for (int j = 0; j < parts.size(); j++) {
                if (parts.get(j).contains(src)) {
                    indexOfp = j;
                }
                if (parts.get(j).contains(dest)) {
                    indexOfq = j;
                }
            }

            if (indexOfp != indexOfq) {
                mst.add(edges.get(i));
                HashSet<Room> mergedSet = parts.get(indexOfp);
                mergedSet.addAll(parts.get(indexOfq));

                if (indexOfq > indexOfp) {
                    parts.remove(indexOfp);
                    parts.remove(indexOfq);
                } else {
                    parts.remove(indexOfp);
                    parts.remove(indexOfq);
                }

                parts.add(mergedSet);

            }
        }
        return mst;
    }
    */

    private boolean check(WeightedGraph wdg) {

        // check weight
        double totalWeight = 0.0;
        for (WeightedEdge e : edges()) {
            totalWeight += e.weight();
        }
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not "
                    + "equal weight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        // check that it is acyclic
        UnionFind uf = new UnionFind(rooms);
        for (WeightedEdge e : edges()) {
            Room src = e.to();
            Room dest = e.from();
            if (uf.connected(src, dest)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(src, dest);
        }

        // check that it is a spanning forest
        for (WeightedEdge e : edges()) {
            Room v = e.to();
            Room w = e.from();
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (WeightedEdge e : edges()) {

            // all edges in MST except e
            uf = new UnionFind(wdg.rooms);
            for (WeightedEdge f : edges()) {
                Room x = f.to();
                Room y = f.from();
                if (!f.equals(e)) {
                    uf.union(x, y);
                }
            }

            // check that e is min weight edge in crossing cut
            for (WeightedEdge f : wdg.allEdges()) {
                Room x = f.to();
                Room y = f.from();
                if (!uf.connected(x, y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }



}
