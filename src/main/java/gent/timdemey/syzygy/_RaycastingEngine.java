package gent.timdemey.syzygy;

import gent.timdemey.syzygy.math.MathUtils;
import gent.timdemey.syzygy.math.MatrixOps;

import java.awt.Graphics2D;

public class _RaycastingEngine implements Engine {

    private static final int     WALK_UNITS_PER_SECOND = 250;
    private static final double  TURN_RAD_PER_SECOND   = Math.PI / 2;

    private static final int [][] WALLS = new int [][] {
        {1,2,1,2,1},
        {2,0,0,0,2},
        {1,2,1,2,1}
    };

    private double[][]           position;
    private CoorSys              cs;
    // user input, used to calculate other variables (see below)
    private double               rotangle = 0.0;

    // transformation matrices, to be calculated in updateGame
    private double[][]           T_rot    = new double[][] { { 1, 0 }, { 0, 1 } };
    private double[][]           T_trs    = new double[][] { { 0 }, { 0 } };

    @Override
    public void initialize() {
        position = new double[][] { { 1.5 }, { 1.5 } };
        cs = new CoorSys(WALLS);
    }

    @Override
    public void updateGame(UpdateInfo info) {
        double secs = 1.0 * info.getDiffTime() / 1000;

        // calc rotation matrix
        double angle = secs * TURN_RAD_PER_SECOND;
        rotangle += info.isInputActive(Input.LEFT) ? angle : 0;
        rotangle += info.isInputActive(Input.RIGHT) ? -angle : 0;
        double cos = Math.cos(rotangle);
        double sin = Math.sin(rotangle);
        T_rot = new double[][] {{cos, -sin},{sin, cos}};

        // calc translation matrix
        double trsdist = secs * WALK_UNITS_PER_SECOND;
        double dirmult = 0;
        dirmult += info.isInputActive(Input.FORWARD) ? 1 : 0;
        dirmult += info.isInputActive(Input.BACKWARD) ? -1 : 0;

        double actualdist = dirmult * trsdist;
        double[][] distV = new double[][] {{actualdist},{0}};
        double[][] walkV = MatrixOps.multiply(T_rot, distV);
        T_trs = MatrixOps.add(T_trs, walkV);
        // TODO collision detection

        position = MatrixOps.multiply(T_rot, position);
        position = MatrixOps.add(position, T_trs);

        double normangle = MathUtils.angle_canonical(rotangle);
        double[] anglevect = MathUtils.normangle2vect(normangle);
        int wall = cs.intersect(position, anglevect);
    }

    @Override
    public void renderGame(Graphics2D g, RenderInfo info) {

    }
}
