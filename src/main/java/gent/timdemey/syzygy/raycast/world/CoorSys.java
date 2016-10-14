package gent.timdemey.syzygy.raycast.world;



public class CoorSys {

    /**
     * Array is to be treated upside down, as in a normal x-y axis system. Higher Y number means upwards, 0 is next to
     * the X axis.
     */
    private final Map map;

    public CoorSys(Map map) {
        this.map = map;
    }

    /**
     * Casts a ray from the given starting point in the given direction and returns all intersections with the
     * grid. The last intersection is the result of hitting a solid wall.
     * @param pos starting point
     * @param m the direction vector
     * @return the wall index
     */
    public Ray intersect(double[] pos, double[] m) {
        // each var described here is to be interpreted as a tuple, so z = (zx, zy)
        // p = player position vector (double)
        // ip = int-casted p (int)
        // m = player view direction vector (double)
        // b = grid boundaries (int)
        // s = step size (-1, 0, 1) (int)
        // h = boundary hit double coordinate (double) e.g. (bx, hy) or (hx, by) is a grid hit
        // dx = taxi distances to hit with vertical grid line (double)
        // dy = taxi distances to hit with horizontal grid line (double)
        // i = index of the wall that was hit
        // d = distance between p and h

        double px = pos[0];
        double py = pos[1];
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
        int ix = -1;
        int iy = -1;
        double dx = 0.0;
        double dy = 0.0;

        // wall color
        Wall wall = null;
        double[][] gridhits = new double[32][2];
        int leaps = 0;
        while (wall == null) {
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

            if (dxx < dyx) { // collinear points, no need for Euclid dist
                // (bx, hy) wins over (hx, by)
                gridhits[leaps][0] = bx;
                gridhits[leaps][1] = hy;
                ix = grid2wall(bx, sx);
                iy = (int) hy;
                dx = dxx;
                dy = dxy;
                dxx = Double.MAX_VALUE;
                bx += sx;
            } else {
                gridhits[leaps][0] = hx;
                gridhits[leaps][1] = by;
                ix = (int) hx;
                iy = grid2wall(by, sy);
                dx = dyx;
                dy = dyy;
                dyx = Double.MAX_VALUE;
                by += sy;
            }
            leaps++;
            wall = map.at(ix, iy);
        }

        double euclid = dist(dx, dy);
        return new Ray(wall, euclid, gridhits, leaps, ix, iy);
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
}
