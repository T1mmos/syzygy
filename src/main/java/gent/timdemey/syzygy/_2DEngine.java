package gent.timdemey.syzygy;

import gent.timdemey.syzygy.math.MatrixOps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * A simple 2D engine that is just a showcase for what is going on in the 3D version.
 * This is merely just a tryout in 2D space to get a better understanding of the
 * math and do easier visualization/rendering, before going to 3D space.
 * @author Timmos
 */
public class _2DEngine implements Engine {

    private static final int PLAYFIELD_WIDTH_UNITS = 1000;
    private static final int PLAYFIELD_HEIGHT_UNITS = 1000;

    private static final int ARROW_LENGTH_UNITS = 100;

    private static final int WALK_UNITS_PER_SECOND = 250;
    private static final double TURN_RAD_PER_SECOND = Math.PI / 2;

    // user input, used to calculate other variables (see below)
    private double rotangle = 0.0;

    // transformation matrices, to be calculated in updateGame
    private double[][] T_rot = new double[][] {{1,0},{0,1}};
    private double[][] T_trs = new double[][] {{0},{0}};

    // line defs of the player arrow
    private static final double[][][][] lines = new double[][][][] {
        {{{0},{0}}, {{100},{-100}}},
        {{{100},{-100}}, {{100},{100}}},
        {{{100},{100}}, {{0},{0}}}
    };

    // line defs of the world (for now, a square)
    private static final double[][][][] world = new double[][][][] {
        {{{300},{300}}, {{600},{300}}},
        {{{600},{300}}, {{600},{600}}},
        {{{600},{600}}, {{300},{600}}},
        {{{300},{600}}, {{300},{300}}},
    };

    private static double[][][][] transforms = new double[][][][] {};

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateGame(FrameInfo info) {
        double secs = 1.0 * info.diffTime / 1000;
        double speedmult = info.isInputActive(Input.SPEED_BOOSTER) ? 2 : 1;

        // calc rotation matrix
        double angle = speedmult * secs * TURN_RAD_PER_SECOND;
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

        double actualdist = dirmult * speedmult * trsdist;
        double[][] distV = new double[][] {{actualdist},{0}};
        double[][] walkV = MatrixOps.multiply(T_rot, distV);
        T_trs = MatrixOps.add(T_trs, walkV);
        // TODO collision detection
        transform();
    }

    private void transform () {
        transforms = new double[lines.length][][][];
        int j = 0;
        for (double[][][] line : lines) {
            double[][][] trsline = new double[2][][];
            int i = 0;
            for (double[][] point : line) {
                double[][] rotP = MatrixOps.multiply(T_rot, point);
                double[][] trsP = MatrixOps.add(rotP, T_trs);
                trsline[i++] = trsP;
            }
            transforms[j++] = trsline;
        }
    }

    @Override
    public void renderGame(Graphics2D g, FrameInfo info) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillRect(0, 0, info.width, info.height);

        g.setColor(Color.white);

        for (double[][][] line : transforms) {
            int posX1 = (int) (line[0][0][0] * info.width / PLAYFIELD_WIDTH_UNITS);
            int posY1 = (int) (line[0][1][0] * info.height / PLAYFIELD_HEIGHT_UNITS);
            int posX2 = (int) (line[1][0][0] * info.width / PLAYFIELD_WIDTH_UNITS);
            int posY2 = (int) (line[1][1][0] * info.height / PLAYFIELD_HEIGHT_UNITS);

            g.drawLine(posX1, info.height - 1 - posY1, posX2, info.height - 1 - posY2);
        }

        g.drawString("FPS: " + info.currFPS, 0, 20);
    }

}
