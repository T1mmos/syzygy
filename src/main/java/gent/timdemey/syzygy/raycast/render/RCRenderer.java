package gent.timdemey.syzygy.raycast.render;

import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.world.GameState;

import java.awt.Graphics2D;

/**
 * Renders the internal state of the raycasting internal user space to something on screen.
 * @author Timmos
 */
public interface RCRenderer {

    public void renderAll(Graphics2D g, Frame fInfo, RenderInfo rInfo, GameState sInfo);
}
