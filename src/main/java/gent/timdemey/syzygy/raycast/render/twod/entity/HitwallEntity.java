package gent.timdemey.syzygy.raycast.render.twod.entity;

import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderInfo;
import gent.timdemey.syzygy.raycast.world.GameState;

import java.awt.Color;
import java.awt.Graphics2D;


public class HitwallEntity implements RenderEntity {

    @Override
    public void render(Graphics2D g, Frame fInfo, RenderInfo rInfo, GameState sInfo, RC2DRenderInfo rcInfo) {
        g.setColor(Color.ORANGE.darker());

        double ux = sInfo.wall[sInfo.raycount / 2].x + 0.5;
        double uy = sInfo.wall[sInfo.raycount / 2].y + 0.5;
        RenderUtils.renderDot(g, rInfo, rcInfo, ux, uy);
    }

}
