package gent.timdemey.syzygy.raycast;

import gent.timdemey.syzygy.core.Engine;
import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.RCRenderer;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderer;
import gent.timdemey.syzygy.raycast.world.CoorSys;
import gent.timdemey.syzygy.raycast.world.GameState;
import gent.timdemey.syzygy.raycast.world.WallInfo;

import java.awt.Graphics2D;

public class _RaycastingEngine implements Engine {

    private final GameState state;
    private final RCRenderer  renderer;

    private CoorSys           cs;

    public _RaycastingEngine() {
        state = new GameState();
        renderer = new RC2DRenderer();
    }

    @Override
    public void initialize() {
        cs = new CoorSys(state.map.getWalls());
    }

    @Override
    public void updateState(Frame fInfo) {
        state.player.readFrame(fInfo);
        state.player.rotate(fInfo);
        state.player.calcOffset(fInfo);

        // collision detection
        double[][] T = state.player.getMatrix();

        double currx = T[0][2];
        double curry = T[1][2];
        double nextx = state.player.getNextPosition()[0];
        double nexty = state.player.getNextPosition()[1];

        int icurrx = state.player.getGridPosition()[0];
        int icurry = state.player.getGridPosition()[1];
        int inextx = state.player.getGridNextPosition()[0];
        int inexty = state.player.getGridNextPosition()[1];

        if (state.map.isWall(icurrx, inexty)) {
            nexty = curry;
        }
        if (state.map.isWall(inextx, icurry)) {
            nextx = currx;
        }
        state.player.teleport(nextx, nexty);

        double[] pos = new double[] { nextx, nexty };
        double angle_step = state.player.fov / (state.raycount - 1);
        double angle = state.player.fov_angle_left;

        // cast rays
        for (int i = 0; i < state.raycount; i++) {
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);
            double[] m = new double[] { cos, sin };
            WallInfo wallInfo = cs.intersect(pos, m);
            state.wall[i] = wallInfo;
            angle -= angle_step;
        }
    }

    @Override
    public void renderGame(Graphics2D g, Frame fInfo, RenderInfo rInfo) {
        Graphics2D gg = g;
        renderer.renderAll(gg, fInfo, rInfo, state);
        gg.dispose();
    }

}
