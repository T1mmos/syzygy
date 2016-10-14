package gent.timdemey.syzygy.raycast.world;


public class Ray {

    public final Wall       wall;
    public final double dist;
    public final double[][] gridhits;
    public final int        leaps;

    public Ray(Wall wall, double dist, double[][] gridhits, int leaps, int x, int y) {
        this.wall = wall;
        this.dist = dist;
        this.gridhits = gridhits;
        this.leaps = leaps;
    }
}
