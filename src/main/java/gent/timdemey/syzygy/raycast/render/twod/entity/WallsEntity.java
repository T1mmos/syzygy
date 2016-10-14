package gent.timdemey.syzygy.raycast.render.twod.entity;

import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.G;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderInfo;
import gent.timdemey.syzygy.raycast.world.State;

import java.awt.Color;
import java.awt.Graphics2D;

public class WallsEntity implements RenderEntity {

    @Override
    public void render(Graphics2D g, Frame fInfo, RenderInfo rInfo, State sInfo, RC2DRenderInfo rcInfo) {
        g.setColor(Color.BLUE.darker().darker().darker());
        for (int k = 0; k < sInfo.map.wallcntx; k++) {
            for (int l = 0; l < sInfo.map.walls_y; l++) {
                if (!sInfo.map.isWall(k, l)) {
                    continue;
                }
                int scr_x = k * rcInfo.wallW + 1;
                int scr_y = l * rcInfo.wallH + 1;
                G.NORMAL.fillRect(rInfo, scr_x, scr_y, rcInfo.wallW - 1, rcInfo.wallH - 1);
            }
        }
    }

}
