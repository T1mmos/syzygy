package gent.timdemey.syzygy.raycast.world;

import gent.timdemey.syzygy.core.Action;
import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.math.MathUtils;

/**
 * Represents a player.
 * @author Timmos
 */
public class Player {

    public static final double WALK_UNITS_PER_MS = 1.0 / 1000;
    public static final double TURN_RAD_PER_MS   = Math.PI / 2 / 1000;

    // user input, used to calculate other variables (see below)
    public double              rot_angle;

    public double              d_trs_base;
    public double              d_trs_mult;
    // transformation matrices, to be calculated in updateGame
    public final double[][]          T;

    /** Normalized direction vector. */

    public int[]               grid         = new int[2];

    // parameters directly based on user input (readFrame)
    public double              f_angle;
    public double              f_mult                = 1;
    public int                 f_walk                = 0;

    // Volatile data, to be adapted based on conditionals
    public final double[]      v_offset;
    public final int[]         v_offset_grid;
    public final double[]      v_nextpos;
    public final int[]         v_nextpos_grid;

    public Player() {
        rot_angle = MathUtils.PI_HALF;

        double cos = 0;
        double sin = 1;
        double tx = 1.5;
        double ty = 1.5;

        T = new double [][] {
                        { cos, -sin, tx },
                        { sin, cos, ty }
        };

        v_offset = new double[2];
        v_offset_grid = new int[2];
        v_nextpos = new double[2];
        v_nextpos_grid = new int[2];
    }

    /**
     * Calculates the location where this player would arrive when walking, based on current frame info and player
     * info.
     * @param f
     * @return
     */
    public void calcOffset(Frame f) {
        if (f.isOneActive(Action.FORWARD, Action.BACKWARD)) {
            d_trs_base = f_walk * f.t_diff * Player.WALK_UNITS_PER_MS;
            d_trs_mult = f_mult * d_trs_base;
            v_offset[0] = d_trs_mult * T[0][0];
            v_offset[1] = d_trs_mult * T[1][0];
        } else {
            d_trs_base = 0;
            d_trs_mult = 0;
            v_offset[0] = v_offset[1] = 0;
        }

        v_nextpos[0] = T[0][2] + v_offset[0];
        v_nextpos[1] = T[1][2] + v_offset[1];
        v_nextpos_grid[0] = (int) v_nextpos[0];
        v_nextpos_grid[1] = (int) v_nextpos[1];
        v_offset_grid[0] = v_nextpos_grid[0] - grid[0];
        v_offset_grid[1] = v_nextpos_grid[1] - grid[1];
    }

    public double[] getOffset() {
        return v_offset;
    }

    public int[] getGridOffset() {
        return v_offset_grid;
    }

    public int[] getGridPosition() {
        return grid;
    }

    public double[] getNextPosition() {
        return v_nextpos;
    }

    public int[] getGridNextPosition() {
        return v_nextpos_grid;
    }

    /**
     * Returns the rotation/translation matrix (2 by 3).
     * @return
     */
    public double[][] getMatrix() {
        return T;
    }

    public void teleport(double x, double y) {
        T[0][2] = x;
        T[1][2] = y;

        grid[0] = (int) x;
        grid[1] = (int) y;
    }

    public void readFrame(Frame f) {
        f_angle = 0;
        if (f.isOneActive(Action.LEFT, Action.RIGHT)){
            int sgn = f.isActive(Action.LEFT) ? 1 : -1;
            f_angle += sgn * (f.t_diff * Player.TURN_RAD_PER_MS);
        }
        if (f.isOneActive(Action.FORWARD, Action.BACKWARD)) {
            f_walk = f.isActive(Action.FORWARD) ? 1 : -1;
        } else {
            f_walk = 0;
        }
        f_mult = f.isActive(Action.SPEED_BOOSTER) ? 2 : 1;
    }

    /**
     * Rotate based on current frame info and player info.
     * @param f
     */
    public void rotate(Frame f) {
        rot_angle += f_mult * f_angle;
        rot_angle = MathUtils.angle_canonical(rot_angle);

        // recalc rotation matrix
        double cos = Math.cos(rot_angle);
        double sin = Math.sin(rot_angle);
        T[0][0] = cos;
        T[0][1] = -sin;
        T[1][0] = sin;
        T[1][1] = cos;
    }
}
