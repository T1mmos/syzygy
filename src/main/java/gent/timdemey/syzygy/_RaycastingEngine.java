package gent.timdemey.syzygy;

import gent.timdemey.syzygy.math.MathUtils;
import gent.timdemey.syzygy.math.MatrixOps;

import java.awt.Color;
import java.awt.Graphics2D;

public class _RaycastingEngine implements Engine {

    private static final int     WALK_UNITS_PER_SECOND = 1;
    private static final double  TURN_RAD_PER_SECOND   = Math.PI / 2;

    private static final int [][] WALLS = new int [][] {
        {1,2,1,2,1},
        {2,0,0,0,2},
        {1,2,1,2,1}
    };

    private CoorSys              cs;
    // user input, used to calculate other variables (see below)
    private double               rotangle;

    // transformation matrices, to be calculated in updateGame
    private double[][]           T_rot;
    private double[][]           T_trs;

    private WallInfo             wall                  = new WallInfo(0, 0, 0, 0);

    @Override
    public void initialize() {
        cs = new CoorSys(WALLS);
        T_trs = new double[][] { { 1.5 }, { 1.5 } };
        rotangle = 0.0;
        T_rot = new double[][] { { 1.0, 0.0 }, { 0.0, 1.0 } };
    }

    @Override
    public void updateGame(FrameInfo info) {
        double secs = 1.0 * info.diffTime / 1000;

        if (info.isInputActive(Input.LEFT, Input.RIGHT)) {
            double d_angle = secs * TURN_RAD_PER_SECOND;
            rotangle += info.isInputActive(Input.LEFT) ? d_angle : 0;
            rotangle += info.isInputActive(Input.RIGHT) ? -d_angle : 0;

            // recalc rotation matrix
            double cos = Math.cos(rotangle);
            double sin = Math.sin(rotangle);
            T_rot = new double[][] { { cos, -sin }, { sin, cos } };
        }

        if (info.isInputActive(Input.FORWARD, Input.BACKWARD)) {
            // calc translation matrix
            double units = secs * WALK_UNITS_PER_SECOND;
            double multiplier = 0;
            multiplier += info.isInputActive(Input.FORWARD) ? 1 : 0;
            multiplier += info.isInputActive(Input.BACKWARD) ? -1 : 0;
            double actualdist = multiplier * units;
            double[][] distV = new double[][] { { actualdist }, { 0 } };
            double[][] dirV = MatrixOps.multiply(T_rot, distV);
            T_trs = MatrixOps.add(T_trs, dirV);
        }

        double normangle = MathUtils.angle_canonical(rotangle);
        double[] anglevect = MathUtils.normangle2vect(normangle);
        wall = cs.intersect(T_trs, anglevect);

    }

    @Override
    public void renderGame(Graphics2D g, FrameInfo info) {
        g.setColor(Color.white);
        g.fillRect(0, 0, info.width, info.height);

        g.setColor(Color.gray);
        int x = wall.x;
        int y = wall.y;

        int scrx = x;
        int scry = 5 - y;

        int width = info.width / 5;
        int height = info.height / 5;
        int pix_start = scrx * width;
        int pix_end = scry * height;

        g.fillRect(pix_start, pix_end, width, height);
    }
}
