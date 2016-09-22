package gent.timdemey.syzygy.raycast;

import gent.timdemey.syzygy.core.Engine;
import gent.timdemey.syzygy.core.FrameInfo;
import gent.timdemey.syzygy.core.Input;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.math.MathUtils;
import gent.timdemey.syzygy.math.MatrixOps;
import gent.timdemey.syzygy.raycast.render.RCRenderer;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderer;
import gent.timdemey.syzygy.raycast.world.CoorSys;
import gent.timdemey.syzygy.raycast.world.RCStateInfo;

import java.awt.Graphics2D;

public class _RaycastingEngine implements Engine {

    private final RCStateInfo   stateInfo;
    private final RCRenderer    renderer;

    private CoorSys              cs;


    public _RaycastingEngine() {
        stateInfo = new RCStateInfo();
        renderer = new RC2DRenderer();
    }

    @Override
    public void initialize() {
        cs = new CoorSys(RCStateInfo.WALLS);
        stateInfo.T_trs = new double[][] { { 1.5 }, { 1.5 } };
        stateInfo.rotangle = MathUtils.PI_HALF + 0.01;

        double cos = Math.cos(stateInfo.rotangle);
        double sin = Math.sin(stateInfo.rotangle);
        stateInfo.T_rot = new double[][] { { cos, -sin }, { sin, cos } };
    }

    @Override
    public void updateState(FrameInfo fInfo) {

        double secs = 1.0 * fInfo.diffTime / 1000;

        if (fInfo.isInputActive(Input.LEFT, Input.RIGHT)) {
            double d_angle = secs * RCStateInfo.TURN_RAD_PER_SECOND;
            stateInfo.rotangle += fInfo.isInputActive(Input.LEFT) ? d_angle : 0;
            stateInfo.rotangle += fInfo.isInputActive(Input.RIGHT) ? -d_angle : 0;

            // recalc rotation matrix
            double cos = Math.cos(stateInfo.rotangle);
            double sin = Math.sin(stateInfo.rotangle);
            stateInfo.T_rot = new double[][] { { cos, -sin }, { sin, cos } };
        }

        if (fInfo.isInputActive(Input.FORWARD, Input.BACKWARD)) {
            // calc translation matrix
            double units = secs * RCStateInfo.WALK_UNITS_PER_SECOND;
            double multiplier = 0;
            multiplier += fInfo.isInputActive(Input.FORWARD) ? 1 : 0;
            multiplier += fInfo.isInputActive(Input.BACKWARD) ? -1 : 0;
            double actualdist = multiplier * units;
            double[][] distV = new double[][] { { actualdist }, { 0 } };
            double[][] dirV = MatrixOps.multiply(stateInfo.T_rot, distV);
            stateInfo.T_trs = MatrixOps.add(stateInfo.T_trs, dirV);
        }

        double normangle = MathUtils.angle_canonical(stateInfo.rotangle);
        double[] anglevect = MathUtils.normangle2vect(normangle);
        stateInfo.wall = cs.intersect(stateInfo.T_trs, anglevect);
    }

    @Override
    public void renderGame(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo) {
        renderer.renderAll(g, fInfo, rInfo, stateInfo);
    }
}
