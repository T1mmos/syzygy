package gent.timdemey.syzygy;


public class CoorSys {

    /**
     * Array is to be treated upside down, as in a normal x-y axis system. Higher Y number means upwards, 0 is next to
     * the X axis.
     */
    private final int[][] world;

    public CoorSys(int[][] world) {
        this.world = world;
    }

    /**
     * Searches for a wall intersection.
     * @param pos camera position
     * @param m the normalized casted ray angle
     * @return the wall index
     */
    public WallInfo intersect(double[][] pos, double[] m) {
        // each var described here is to be interpreted as a tuple, so z = (zx, zy)
        // p = player position vector (double)
        // ip = int-casted p (int)
        // m = player view direction vector (double)
        // b = grid boundaries (int)
        // s = step size (-1, 0, 1) (int)
        // h = boundary hit double coordinate (double) e.g. (bx, hy) or (hx, by) is a grid hit
        // dxx, dyx = X-distance to hit (double)
        // dyx, dyy = Y-distance to hit (double)

        double px = pos[0][0];
        double py = pos[1][0];
        double mx = m[0];
        double my = m[1];
        int ipx = (int) px;
        int ipy = (int) py;
        int sx = (int) Math.signum(mx);
        int sy = (int) Math.signum(my);
        int bx = bound(px, ipx, sx);
        int by = bound(py, ipy, sy);
        double hx = -1.0;
        double hy = -1.0;
        double dxx = Double.MAX_VALUE;
        double dxy = Double.MAX_VALUE;
        double dyx = Double.MAX_VALUE;
        double dyy = Double.MAX_VALUE;

        while (true) {
            if (dxx == Double.MAX_VALUE && mx != 0.0) { // look for hit at (bx, hy)
                double u = (bx - px) / mx;
                hy = py + u * my;
                dxx = sx * (bx - px);
                dxy = sy * (hy - py);
            }
            if (dyx == Double.MAX_VALUE && my != 0.0) { // look for hit at (hx, by)
                double u = (by - py) / my;
                hx = px + u * mx;
                dyx = sx * (hx - px);
                dyy = sy * (by - py);
            }


            int idx;
            int idy;
            double dx;
            double dy;
            if (dxx < dyx) { // both points on same line, no Euclid dist needed
                // (bx, hy) wins over (hx, by)
                idx = grid2wall(bx, sx);
                idy = (int) hy;
                dx = dxx;
                dy = dxy;
                dxx = Double.MAX_VALUE;
                bx += sx;
            } else {
                idx = (int) hx;
                idy = grid2wall(by, sy);
                dx = dyx;
                dy = dyy;
                dyx = Double.MAX_VALUE;
                by += sy;
            }
            int wall = wall_at(idx, idy);
            if (wall != 0) {
                double euclid = dist(dx, dy);
                return new WallInfo(wall, euclid, idx, idy);
            }
        }
    }

    public static double dist(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Converts a hitpoint (wall grid point) to a wall index for one axis, taking the
     * incoming angle into account.
     * @param hk
     * @param sk
     * @return
     */
    public static int grid2wall(int hk, int sk) {
        int k;
        if (sk == 1) {
            k = hk;
        } else if (sk == -1) {
            k = hk - 1;
        } else {
            // cannot happen
            k = -1;
        }
        return k;
    }

    /**
     * Returns the next grid line perpendicular to an axis, given a player's position on that axis and direction of
     * travel
     * (= step size = signum of the slope in the axis' direction).
     * @param p player position on some chosen axis
     * @param ip the int-casted position (not calculated in this method for performance)
     * @param s step size (-1, 0 or 1), which should equals sgn(m) with m the slope component of the view direction
     * @return the next grid line coordinate
     */
    public static int bound(double p, int ip, int s) {
        int b;
        if (s == 0) {
            b = -1;
        } else {
            if (ip == p) {
                b = ip + s;
            } else {
                if (s > 0) {
                    b = ip + 1;
                } else {
                    b = ip;
                }
            }
        }
        return b;
    }

    /**
     * Checks the wall number at grid coordinate (x,y).
     * @param x x-coordinate
     * @param y y-coordinate
     * @return the wall index, 0 meaning nothing there
     */
    public int wall_at(int x, int y) {
        return world[world.length - 1 - y][x];
    }
}
