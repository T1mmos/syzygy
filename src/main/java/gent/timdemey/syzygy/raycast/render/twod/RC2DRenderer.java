package gent.timdemey.syzygy.raycast.render.twod;

import gent.timdemey.syzygy.core.FrameInfo;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.RCRenderer;
import gent.timdemey.syzygy.raycast.world.RCUserSpace;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * Renders the internal world in a topdown view. This can be used a
 * @author Timmos
 */
public class RC2DRenderer implements RCRenderer {

    private static final Font    FONT_TEXT = Font.decode("Arial 9");

    private final RC2DRenderInfo ri;

    public RC2DRenderer() {
        ri = new RC2DRenderInfo();
    }

    @Override
    public void renderAll(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo, RCUserSpace sInfo) {
        calc(fInfo, rInfo, sInfo);

        //        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        render(g, fInfo, rInfo, sInfo);
    }

    private void calc(FrameInfo fInfo, RenderInfo rInfo, RCUserSpace sInfo) {
        ri.wallW = rInfo.resx / sInfo.walls_x;
        ri.wallH = rInfo.resy / sInfo.walls_y;
        ri.gridW = ri.wallW * sInfo.walls_x;
        ri.gridH = ri.wallH * sInfo.walls_y;
    }

    private void render(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo, RCUserSpace sInfo) {
        renderBackground(newg(g), fInfo, rInfo, sInfo);
        renderGrid(newflipg(g, rInfo), fInfo, rInfo, sInfo);
        renderWalls(newflipg(g, rInfo), fInfo, rInfo, sInfo);
        renderHitWall(newflipg(g, rInfo), fInfo, rInfo, sInfo);
        renderPlayer(newflipg(g, rInfo), fInfo, rInfo, sInfo);
        renderWallpoints(newflipg(g, rInfo), fInfo, rInfo, sInfo);
        renderText(newg(g), fInfo, rInfo, sInfo);
    }

    private static Graphics2D newg(Graphics g) {
        return (Graphics2D) g.create();
    }

    private static Graphics2D newflipg(Graphics g, RenderInfo rInfo) {
        Graphics2D gg = newg(g);
        gg.translate(0, rInfo.resy - 1);
        gg.scale(1.0, -1.0);

        return gg;
    }

    private static void renderBackground(Graphics2D g, FrameInfo fInfo, RenderInfo info, RCUserSpace sInfo) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, info.resx, info.resy);
    }

    private void renderGrid(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo, RCUserSpace sInfo) {
        g.setColor(Color.darkGray);
        for (int i = 1; i < sInfo.walls_x; i++) {
            int x = i * ri.wallW;
            g.drawLine(x, 0, x, ri.gridH);
        }
        for (int j = 1; j < sInfo.walls_y; j++) {
            int y = j * ri.wallH;
            g.drawLine(0, y, ri.gridW, y);
        }
    }

    private void renderPlayer(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo, RCUserSpace sInfo) {
        g.setColor(Color.green);

        double length = 1;
        double us_vx = length * sInfo.v_dir[0];
        double us_vy = length * sInfo.v_dir[1];

        // user space
        double us_x = sInfo.T_trs[0][0];
        double us_y = sInfo.T_trs[1][0];

        // screen space, 1 unit => wall width/height
        renderDot(g, rInfo, us_x, us_y);

        double us_dx = us_x + us_vx;
        double us_dy = us_y + us_vy;

        int scr_x = (int) (us_x * ri.wallW);
        int scr_y = (int) (us_y * ri.wallH);
        int scr_dx = (int) (us_dx * ri.wallW);
        int scr_dy = (int) (us_dy * ri.wallH);

        g.setColor(Color.red);
        g.draw(new Line2D.Double(scr_x, scr_y, scr_dx, scr_dy));

        g.drawLine(0, -500, 0, 500);
    }

    private void renderDot (Graphics2D g, RenderInfo rInfo, double ux, double uy){
        // screen space, 1 unit => wall width/height
        int scr_x = (int) (ux * ri.wallW);
        int scr_y = (int) (uy * ri.wallH);

        // dot size
        int sizex = ri.wallW / 4 | 1; // make it odd
        int sizey = ri.wallH / 4 | 1;

        Shape oval = new Ellipse2D.Double(scr_x - sizex / 2, scr_y - sizey / 2, sizex, sizey);
        g.fill(oval);
    }

    private void renderWalls(Graphics2D g, FrameInfo fInfo, RenderInfo info, RCUserSpace sInfo) {
        g.setColor(Color.BLUE.darker().darker().darker());
        for (int k = 0; k < sInfo.walls_x; k++) {
            for (int l = 0; l < sInfo.walls_y; l++) {
                int nr = sInfo.WALLS[sInfo.walls_y - 1 - l][k];
                if (nr == 0) {
                    continue;
                }
                int scr_x = k * ri.wallW + 1;
                int scr_y = l * ri.wallH + 1;
                g.fillRect(scr_x, scr_y, ri.wallW - 2, ri.wallH - 2);
            }
        }
    }

    private void renderHitWall(Graphics2D g, FrameInfo fInfo, RenderInfo info, RCUserSpace sInfo) {
        g.setColor(Color.ORANGE.darker());

        double ux = sInfo.wall.x + 0.5;
        double uy = sInfo.wall.y +0.5;
        renderDot(g, info, ux, uy);
    }

    private void renderWallpoints(Graphics2D g, FrameInfo fInfo, RenderInfo info, RCUserSpace sInfo) {
        g.setColor(Color.red);
        double[][] gridhits = sInfo.wall.gridhits;

        for (int k = 0; k < sInfo.wall.leaps; k++) {
            renderDot(g, info, gridhits[k][0],gridhits[k][1]);
        }
    }

    private static void renderText(Graphics2D g, FrameInfo fInfo, RenderInfo info, RCUserSpace sInfo) {
        String p_posstr = String.format("POS=%.2f;%.2f", sInfo.T_trs[0][0], sInfo.T_trs[1][0]);
        String p_rotstr = String.format("ROT=%.2f rad", sInfo.rotangle);
        g.setColor(Color.YELLOW);
        g.setFont(FONT_TEXT);
        g.drawString(fInfo.currFPS + " FPS", 1, 10);
        g.drawString(p_posstr, 1, 30);
        g.drawString(p_rotstr, 1, 40);
        g.drawString(sInfo.wall.leaps + " grid hits", 1, 50);
        g.drawString("Grid pos = (" + sInfo.grid_curr[0] + "," + sInfo.grid_curr[1] + ")", 1, 60);
    }
}
