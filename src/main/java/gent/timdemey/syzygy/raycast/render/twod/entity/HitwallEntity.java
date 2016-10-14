package gent.timdemey.syzygy.raycast.render.twod.entity;

import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderInfo;
import gent.timdemey.syzygy.raycast.world.State;

import java.awt.Color;
import java.awt.Graphics2D;


public class HitwallEntity implements RenderEntity {

    @Override
    public void render(Frame fInfo, RenderInfo rInfo, State sInfo, RC2DRenderInfo rcInfo) {
        g.setColor(Color.ORANGE.darker());

        double ux = sInfo.rays[sInfo.raycount / 2].x + 0.5;
        double uy = sInfo.rays[sInfo.raycount / 2].y + 0.5;
        RenderUtils.renderDot(rInfo, rcInfo, ux, uy);
    }

}
