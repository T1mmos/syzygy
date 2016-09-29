package gent.timdemey.syzygy.raycast.render.twod.entity;

import gent.timdemey.syzygy.core.G;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderInfo;

import java.awt.Graphics2D;

public class RenderUtils {

    public static void renderDot(Graphics2D g, RenderInfo rInfo, RC2DRenderInfo rcInfo, double ux, double uy) {
        // screen space, 1 unit => wall width/height
        int scr_x = (int) (ux * rcInfo.wallW);
        int scr_y = (int) (uy * rcInfo.wallH);

        // dot size
        int sizex = rcInfo.wallW / 4 | 1; // make it odd
        int sizey = rcInfo.wallH / 4 | 1;

        G.NORMAL.fillOval(g, rInfo, scr_x, scr_y, sizex, sizey);
    }
}
