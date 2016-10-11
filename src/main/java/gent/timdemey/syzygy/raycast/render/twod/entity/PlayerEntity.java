package gent.timdemey.syzygy.raycast.render.twod.entity;

import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.twod.RC2DRenderInfo;
import gent.timdemey.syzygy.raycast.world.GameState;

import java.awt.Color;
import java.awt.Graphics2D;

public class PlayerEntity implements RenderEntity {

    @Override
    public void render(Graphics2D g, Frame fInfo, RenderInfo rInfo, GameState sInfo, RC2DRenderInfo rcInfo) {
        g.setColor(Color.green);

        // user space
        double us_x = sInfo.player.T[0][2];
        double us_y = sInfo.player.T[1][2];

        // screen space, 1 unit => wall width/height
        RenderUtils.renderDot(g, rInfo, rcInfo, us_x, us_y);

        double cosl = Math.cos(sInfo.player.fov_angle_left);
        double sinl = Math.sin(sInfo.player.fov_angle_left);
        double cosr = Math.cos(sInfo.player.fov_angle_right);
        double sinr = Math.sin(sInfo.player.fov_angle_right);

        double xl = cosl + sInfo.player.T[0][2];
        double yl = sinl + sInfo.player.T[1][2];
        double xr = cosr + sInfo.player.T[0][2];
        double yr = sinr + sInfo.player.T[1][2];
        g.setColor(Color.white);
        RenderUtils.renderDot(g, rInfo, rcInfo, xl, yl);
        RenderUtils.renderDot(g, rInfo, rcInfo, xr, yr);

        /*
                double us_dx = us_x + us_vx;
                double us_dy = us_y + us_vy;

                int scr_x = (int) (us_x * rcInfo.wallW);
                int scr_y = (int) (us_y * rcInfo.wallH);
                int scr_dx = (int) (us_dx * rcInfo.wallW);
                int scr_dy = (int) (us_dy * rcInfo.wallH);

                g.setColor(Color.red);
                G.NORMAL.drawLine(g, rInfo, scr_x, scr_y, scr_dx, scr_dy);*/
    }

}
