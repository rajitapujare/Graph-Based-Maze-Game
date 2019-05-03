package byow.Core;

public class Position {

    private int x;
    private int y;

    private double xx;
    private double yy;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.xx = x;
        this.yy = y;
    }

    public Position(double x, double y) {
        this.xx = x;
        this.yy = y;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public double getDoubX() {
        return xx;
    }

    public double getDoubY() {
        return yy;
    }

    /**
     * @source Prof Hug for proj2a
     * Returns the euclidean distance (L2 norm) squared between two points
     * (x1, y1) and (x2, y2). Note: This is the square of the Euclidean distance,
     * i.e. there's no square root.
     */
    private static double distance(double x1, double x2, double y1, double y2) {
        return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
    }

    /**
     * @source Prof Hug for 2a
     * Returns the euclidean distance (L2 norm) squared between two points.
     * Note: This is the square of the Euclidean distance, i.e.
     * there's no square root.
     */
    public static double distance(Position p1, Position p2) {
        return distance(p1.getX(), p2.getX(), p1.getY(), p2.getY());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Position otherPoint = (Position) other;
        return getX() == otherPoint.getX() && getY() == otherPoint.getY();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y);
    }

    @Override
    public String toString() {
        return new String("X1: " + this.x + ", Y1: " + this.y);
    }
}




