package byow.Core;

/**
 * Utility class that represents a weighted edge.
 * Created by hug.
 */
public class WeightedEdge implements Comparable<WeightedEdge> {
    private Room p;
    private Room q;
    private double weight;

    public WeightedEdge(Room p, Room q) {
        this.p = p;
        this.q = q;
        this.weight = Position.distance(p.center, q.center);
        //this.weight = Math.abs(p.center.getY() - q.center.getY()) +
        // Math.abs(p.center.getX() - q.center.getX());
    }
    public Room from() {
        return p;
    }
    public Room to() {
        return q;
    }
    public double weight() {
        return weight;
    }

    @Override
    public int compareTo(WeightedEdge edge) {
        int compare = 0;
        if (this.weight > edge.weight) {
            compare = 1;
        } else if (this.weight < edge.weight) {
            compare = -1;
        }

        return compare;
    }

}
