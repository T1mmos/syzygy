package gent.timdemey.syzygy.raycast.world;

import gent.timdemey.syzygy.core.StateInfo;

public class RCStateInfo extends StateInfo {

    public static final int     WALK_UNITS_PER_SECOND = 1;
    public static final double  TURN_RAD_PER_SECOND   = Math.PI / 2;

    public static final int[][] WALLS = new int[][] {
        {1,2,1,2,1,2,1,2,1,2,1},
        {2,0,0,0,0,0,0,0,1,0,2},
        {1,0,1,0,1,0,0,0,1,0,1},
        {1,0,2,2,1,0,0,0,0,0,2},
        {1,0,0,0,1,0,0,1,0,0,1},
        {1,2,1,2,1,2,1,2,1,2,1}
    };

    public final int            walls_x               = WALLS[0].length;
    public final int            walls_y               = WALLS.length;

    public WallInfo             wall                  = new WallInfo(0, 0, new double[0][0], 0, 0, 0);

    // user input, used to calculate other variables (see below)
    public double               rotangle;
    // transformation matrices, to be calculated in updateGame
    public double[][]           T_rot;
    public double[][]           T_trs;


}
