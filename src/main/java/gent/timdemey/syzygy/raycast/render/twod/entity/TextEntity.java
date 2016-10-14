package gent.timdemey.syzygy.raycast.render.twod.entity;

import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderInfo;
import gent.timdemey.syzygy.raycast.world.State;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class TextEntity implements RenderEntity {

    private static final Font FONT_TEXT = Font.decode("Arial 9");

    @Override
    public void render(Graphics2D g, Frame fInfo, RenderInfo rInfo, State sInfo, RC2DRenderInfo rcInfo) {
        g.setColor(new Color(0, 0, 0, 140));
        g.fillRect(10, 10, 100, 80);
        String p_posstr = String.format("POS=%.2f;%.2f", sInfo.player.T[0][2], sInfo.player.T[1][2]);
        String p_rotstr = String.format("ROT=%.2f rad", sInfo.player.rot_angle);
        g.setColor(Color.YELLOW);
        g.setFont(FONT_TEXT);
        g.drawString(fInfo.fps + " FPS", 11, 20);
        g.drawString(p_posstr, 11, 30);
        g.drawString(p_rotstr, 11, 40);
        g.drawString(sInfo.rays[sInfo.raycount / 2].leaps + " grid hits", 11, 50);
        g.drawString("Grid pos = (" + sInfo.player.grid[0] + "," + sInfo.player.grid[1] + ")", 11, 60);
    }

}
