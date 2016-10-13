package gent.timdemey.syzygy.raycast.render.twod.entity;

import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.G;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderInfo;
import gent.timdemey.syzygy.raycast.world.GameState;

import java.awt.Color;

public class BackgroundEntity implements RenderEntity {

    @Override
    public void render(Frame fInfo, RenderInfo rInfo, GameState sInfo, RC2DRenderInfo rcInfo) {
        G.NORMAL.setColor(Color.black);
        G.NORMAL.fillRect(0, 0, sInfo.map.walls_x, sInfo.map.walls_y);
    }

}
