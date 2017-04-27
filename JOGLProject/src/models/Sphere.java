package models;

/**
 *
 * @author Gabriel Huertas
 */
public class Sphere {

    private float x;
    private float y;
    private float z;
    private float radius;
    private double angularAcceleration;
    private double xVel;
    private double yVel;
    private double xAccel;
    private double yAccel;
    private double mass;

    /**
     *
     * Creates a Ball in an initial position given the 3D-Vector (x,y,z) from
     * the origin, a mass (in Kg) and a radius (in m)
     *
     * @param x
     * @param y
     * @param z
     * @param radius
     * @param mass
     */
    public Sphere(float x, float y, float z, float radius, double mass) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

}
