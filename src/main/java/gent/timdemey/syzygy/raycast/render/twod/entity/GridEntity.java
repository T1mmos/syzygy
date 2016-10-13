package gent.timdemey.syzygy.raycast.render.twod.entity;

import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.G;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderInfo;
import gent.timdemey.syzygy.raycast.world.GameState;

import java.awt.Color;

/**
 * Renders the outline of walls.
 * @author Timmos
 */
public class GridEntity implements RenderEntity {

    @Override
    public void render(Frame fInfo, RenderInfo rInfo, GameState sInfo, RC2DRenderInfo rcInfo) {
        G.NORMAL.setColor(Color.white);
        for (int i = 0; i < sInfo.map.walls_x; i++) {
            for (int j = 0; j < sInfo.map.walls_y; j++) {
                if (!sInfo.map.isWall(i, j)) {
                    continue;
                }
                // can be way more efficient, we're checking nearly all grid lines 2 times here.
                boolean drawleft = i - 1 < 0 || !sInfo.map.isWall(i - 1, j);
                if (drawleft) {
                    G.NORMAL.drawLine(i, j, i, j + 1);
                }
                boolean drawright = i + 1 > sInfo.map.walls_x - 1 || !sInfo.map.isWall(i + 1, j);
                if (drawright) {
                    G.NORMAL.drawLine(x + rcInfo.wallW, y, x + rcInfo.wallW, y + rcInfo.wallH);
                }
                boolean drawbottom = j - 1 < 0 || !sInfo.map.isWall(i, j - 1);
                if (drawbottom) {
                    G.NORMAL.drawLine(x, y, x + rcInfo.wallW, y);
                }
                boolean drawtop = j + 1 > sInfo.map.walls_y - 1 || !sInfo.map.isWall(i, j + 1);
                if (drawtop) {
                    G.NORMAL.drawLine(x, y + rcInfo.wallH, x + rcInfo.wallW, y + rcInfo.wallH);
                }
            }
        }

    }

}
