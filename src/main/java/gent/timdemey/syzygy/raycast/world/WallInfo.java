package gent.timdemey.syzygy.raycast.world;


public class WallInfo {

    public final int    color;
    public final double dist;
    public final double[][] gridhits;
    public final int        leaps;
    public final int    x;
    public final int    y;

    public WallInfo(int color, double dist, double[][] gridhits, int leaps, int x, int y) {
        this.color = color;
        this.dist = dist;
        this.gridhits = gridhits;
        this.leaps = leaps;
        this.x = x;
        this.y = y;
    }
}
