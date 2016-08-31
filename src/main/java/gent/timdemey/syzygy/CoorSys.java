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
    public int intersect(double[][] pos, double[] m) {
        double cx = pos[0][0];
        double cy = pos[1][0];
        double mx = m[0];
        double my = m[1];

        int wallnr;
        while (true) {
            // grid boundaries around pos in dir m
            int floorx = (int) cx;
            double remx = cx - floorx;
            int gridx;
            if (remx == 0.0) {
                if (mx > 0) {
                    gridx = floorx + 1;
                } else if (mx < 0) {
                    gridx = floorx - 1;
                } else {
                    gridx = floorx;
                }
            } else {
                if (mx > 0) {
                    gridx = floorx + 1;
                } else if (mx < 0) {
                    gridx = floorx;
                } else {
                    gridx = -1; // parallel with Y axis
                }
            }
            int floory = (int) cy;
            double remy = cy - floory;
            int gridy;
            if (remy == 0.0) {
                if (my > 0) {
                    gridy = floory + 1;
                } else if (my < 0) {
                    gridy = floory - 1;
                } else {
                    gridy = floory;
                }
            } else {
                if (my > 0) {
                    gridy = floory + 1;
                } else if (my < 0) {
                    gridy = floory;
                } else {
                    gridy = -1; // parallel with X axis
                }
            }

            // try 2 intersections with grid, find closest point
            double kx = -1.0;
            if (gridx != -1) { // gridx = cx + kx * mx
                kx = (gridx - cx) / mx;
            }
            double ky = -1.0;
            if (gridy != -1) {
                ky = (gridy - cy) / my;
            }
            double k;
            if (kx != -1.0 && ky != -1.0 && kx < ky) {
                k = kx;
            } else if (kx != -1.0) {
                k = kx;
            } else {
                k = ky;
            }
            cx = cx + k * mx;
            cy = cy + k * my;

            // determine the block adjacent to this point in this direction
            int idxx = -1;

            if (cx == (int) cx) {
                if (mx > 0) {
                    idxx = (int) cx;
                } else if (mx < 0) {
                    idxx = (int) cx - 1;
                } else {
                    idxx = (int) cx;
                }
            } else {
                idxx = (int) cx;
            }
            int idxy = -1;
            if (cy == (int) cy) {
                if (my > 0) {
                    idxy = (int) cy;
                } else if (my < 0) {
                    idxy = (int) cy - 1;
                } else {
                    idxy = (int) cy;
                }
            } else {
                idxy = (int) cy;
            }

            wallnr = wall_at(idxx, idxy);
            if (wallnr != 0) {
                break;
            }
        }
        return wallnr;

    }


    public int wall_at(int x, int y) {
        return world[world.length - 1 - y][x];
    }

    public static void main(String[] args) {
        double test = -2.0;
        double floor = (int) test;
        System.out.println(floor);
        System.out.println(test - floor);
    }
}
