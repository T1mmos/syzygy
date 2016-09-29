package gent.timdemey.syzygy.raycast;

import gent.timdemey.syzygy.core.Engine;
import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.RCRenderer;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderer;
import gent.timdemey.syzygy.raycast.world.CoorSys;
import gent.timdemey.syzygy.raycast.world.GameState;

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

        // cast
        state.wall = cs.intersect(new double[] { nextx, nexty }, new double[] { state.player.T[0][0],
                        state.player.T[1][0] });
    }

    @Override
    public void renderGame(Graphics2D g, Frame fInfo, RenderInfo rInfo) {
        Graphics2D gg = g;
        renderer.renderAll(gg, fInfo, rInfo, state);
        gg.dispose();
    }

}
