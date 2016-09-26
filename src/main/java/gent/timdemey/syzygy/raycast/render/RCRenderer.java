package gent.timdemey.syzygy.raycast.render;

import gent.timdemey.syzygy.core.FrameInfo;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.world.RCUserSpace;

import java.awt.Graphics2D;

/**
 * Renders the internal state of the raycasting internal user space to something on screen.
 * @author Timmos
 */
public interface RCRenderer {

    public void renderAll(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo, RCUserSpace sInfo);
}
