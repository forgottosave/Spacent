package general;

import entity.Entity;

/**
 * Simple class to store a Vector.
 */
public class Vector {
    private double x;
    private double y;

    /**
     * Simple class to store a Vector.
     * @param x coordinate 1
     * @param y coordinate 2
     */
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Change coordinates
    public void shiftX(int distance) {
        this.x += distance;
    }
    public void shiftY(int distance) {
        this.y += distance;
    }

    // Get coordinates
    public int x() {
        return (int) x;
    }
    public int y() {
        return (int) y;
    }

    /**
     * Calculates a vector from Entity 1 to Entity 2
     * @param e1 Entity 1
     * @param e2 Entity 2
     * @return Vector from 1 to 2
     */
    public static Vector vectorBetweenEntities(Entity e1, Entity e2) {
        return new Vector( e2.getPosition().x() - e1.getPosition().x(), e2.getPosition().y() - e1.getPosition().y());
    }

    public static boolean areEqual(Vector v1, Vector v2) {
        return v1.x() == v2.x() && v1.y() == v2.y();
    }

    public static Vector abs(Vector v) {
        return new Vector(Math.abs(v.x), Math.abs(v.y));
    }

    public  static boolean areClose(Vector v1, Vector v2, int precisionOffset){
        return length(sub(v2,v1)) < precisionOffset;
    }

    // Operations on vectors
    public static double length(Vector v) {
        return Math.sqrt(v.x * v.x + v.y * v.y);
    }

    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector sub(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vector scalarMul(Vector v, double val) {
        return new Vector(v.x * val, v.y * val);
    }

    public static Vector norm(Vector v) {
        return scalarMul(v, 1/length(v));
    }

    @Override
    public String toString() {
        return "("+x()+","+y()+")";
    }
    public static Vector toVector(String s) {
        String[] values = s.substring(1,s.length() - 1).split(",");
        return new Vector(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
    }
}
