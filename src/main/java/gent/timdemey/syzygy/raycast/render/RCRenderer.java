package gent.timdemey.syzygy.raycast.render;

import gent.timdemey.syzygy.core.FrameInfo;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.world.RCStateInfo;

import java.awt.Graphics2D;

public interface RCRenderer {

    public void renderAll(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo, RCStateInfo sInfo);
}
