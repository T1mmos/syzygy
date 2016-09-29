package gent.timdemey.syzygy.core;

import java.awt.Graphics2D;

/**
 * Helper class for working in different axis setup. Although one may use the
 * {@link Graphics2D#transform(java.awt.geom.AffineTransform)} and other related methods, a negative scale (e.g. -1.0 in
 * combination with a translate, to invert an axis) results unwanted pixelshifts
 * when drawing unfilled shapes. (For example, the methods drawLine and drawRect have wrong results). Using an
 * untransformed buffered image
 * which is then drawn mirrored on the destination graphics results in lower performance. Therefore we use this helper
 * class that calculates
 * correct transformations on given coordinates and draws directly on the destination graphics.
 * @author Timmos
 */
public enum G {

    /** X-axis pointing to the right, Y-axis pointing upwards. */
    NORMAL(true,true),
    /** X-axis pointing to the right, Y-axis pointing downwards. This matches Java 2D's axis setup. */
    G_INVERT_Y(true,false),
    /** X-axis pointing to the left, Y-axis pointing upwards. */
    G_INVERT_X(false,true),
    /** X-axis pointing to the left, Y-axis pointing downwards. */
    G_INVERT_BOTH(false,false);


    private final boolean x_r;
    private final boolean y_up;

    private G(boolean x_right, boolean y_up) {
        this.x_r = x_right;
        this.y_up = y_up;
    }

    public void drawLine(Graphics2D g, RenderInfo info, int x1, int y1, int x2, int y2) {
        int nx1 = getX(info, x1);
        int ny1 = getY(info, y1);
        int nx2 = getX(info, x2);
        int ny2 = getY(info, y2);

        g.drawLine(nx1, ny1, nx2, ny2);
    }


    private int getX(RenderInfo info, int x) {
        if (x_r) {
            return x;
        }
        return info.resx - 1 - x;
    }

    /**
     * Calculates the start point for a shape that must be defined with a leftmost X coordinate.
     * @param info
     * @param x
     * @param w
     * @return
     */
    private int getX(RenderInfo info, int x, int w) {
        if (x_r) {
            return x;
        }
        return info.resx - x - w;
    }

    private int getY(RenderInfo info, int y) {
        if (y_up) {
            return info.resy - 1 - y;
        }
        return y;
    }

    private int getY(RenderInfo info, int y, int h) {
        if (y_up) {
            return info.resy - y - h;
        }
        return y;
    }

    /**
     * Paints a dot with center at given x and y, using the given diameters.
     * @param g
     * @param info
     * @param x
     * @param y
     * @param d
     */
    public void fillOval(Graphics2D g, RenderInfo info, int x, int y, int dx, int dy) {
        int nx = getX(info, x);
        int ny = getY(info, y);
        int startx = nx - dx / 2;
        int starty = ny - dy / 2;

        g.fillOval(startx, starty, dx, dy);
    }

    public void fillRect(Graphics2D g, RenderInfo info, int x, int y, int w, int h) {
        int nx = getX(info, x, w);
        int ny = getY(info,y, h);
        g.fillRect(nx, ny, w, h);
    }
}
