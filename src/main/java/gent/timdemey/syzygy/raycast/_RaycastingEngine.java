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
import gent.timdemey.syzygy.raycast.world.RCUserSpace;

import java.awt.Graphics2D;

public class _RaycastingEngine implements Engine {

    private final RCUserSpace us;
    private final RCRenderer    renderer;

    private CoorSys              cs;


    public _RaycastingEngine() {
        us = new RCUserSpace();
        renderer = new RC2DRenderer();
    }

    @Override
    public void initialize() {
        cs = new CoorSys(RCUserSpace.WALLS);
        us.T_trs = new double[][] { { 1.5 }, { 1.5 } };
        us.rotangle = MathUtils.PI_HALF + 0.01;

        double cos = Math.cos(us.rotangle);
        double sin = Math.sin(us.rotangle);
        us.T_rot = new double[][] { { cos, -sin }, { sin, cos } };
        us.v_dir[0] = cos;
        us.v_dir[1] = sin;
    }

    @Override
    public void updateState(FrameInfo fInfo) {

        double secs = 1.0 * fInfo.diffTime / 1000;

        if (fInfo.isInputActive(Input.LEFT, Input.RIGHT)) {
            us.d_angle = secs * RCUserSpace.TURN_RAD_PER_SECOND;
            us.rotangle += fInfo.isInputActive(Input.LEFT) ? us.d_angle : 0;
            us.rotangle += fInfo.isInputActive(Input.RIGHT) ? -us.d_angle : 0;
            us.rotangle = MathUtils.angle_canonical(us.rotangle);

            // recalc rotation matrix
            double cos = Math.cos(us.rotangle);
            double sin = Math.sin(us.rotangle);
            us.T_rot = new double[][] { { cos, -sin }, { sin, cos } };
            us.v_dir[0] = cos;
            us.v_dir[1] = sin;
        }



        if (fInfo.isInputActive(Input.FORWARD, Input.BACKWARD)) {
            // calc translation matrix
            us.d_units_base = secs * RCUserSpace.WALK_UNITS_PER_SECOND;
            us.multiplier = 0;
            us.multiplier += fInfo.isInputActive(Input.FORWARD) ? 1 : 0;
            us.multiplier += fInfo.isInputActive(Input.BACKWARD) ? -1 : 0;
            us.multiplier *= fInfo.isInputActive(Input.SPEED_BOOSTER) ? 2 : 1;
            us.d_units_actual = us.multiplier * us.d_units_base;
            double[][] distV = new double[][] { { us.d_units_actual }, { 0 } };
            us.v_offset = MatrixOps.multiply(us.T_rot, distV);

            double currx = us.T_trs[0][0];
            double curry = us.T_trs[1][0]; // haha, spicy
            double stepx = currx + us.v_offset[0][0];
            double stepy = curry + us.v_offset[1][0];

            int icurrx = (int) currx;
            int icurry = (int) curry;
            int istepx = (int) stepx;
            int istepy = (int) stepy;

            if (us.WALLS[us.walls_y - 1 - istepy][icurrx] != 0) {
                us.v_offset[1][0] = 0;
            }
            if (us.WALLS[us.walls_y - 1 - icurry][istepx] != 0) {
                us.v_offset[0][0] = 0;
            }
            // to do: on a wall corner we need to choose a direction

            us.T_trs = MatrixOps.add(us.T_trs, us.v_offset);

        }
        us.grid_curr[0] = (int) us.T_trs[0][0];
        us.grid_curr[1] = (int) us.T_trs[1][0];
        us.wall = cs.intersect(us.T_trs, us.v_dir);
    }

    @Override
    public void renderGame(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo) {
        renderer.renderAll(g, fInfo, rInfo, us);
    }
}
