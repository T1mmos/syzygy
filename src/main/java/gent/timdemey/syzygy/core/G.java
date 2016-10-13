package gent.timdemey.syzygy.core;

import java.awt.Graphics2D;
import java.awt.Paint;

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

    private Graphics2D    g = null;
    private RenderInfo    ri = null;

    private G(boolean x_right, boolean y_up) {
        this.x_r = x_right;
        this.y_up = y_up;
    }

    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    public void setRenderInfo(RenderInfo ri) {
        this.ri = ri;
    }

    public void setColor(Paint p) {
        g.setPaint(p);
    }

    /**
     * Draws in screen space the line specified with user space coordinates.
     * @param ux1
     * @param uy1
     * @param ux2
     * @param uy2
     */
    public void drawLine(double ux1, double uy1, double ux2, double uy2) {
        drawLine(ux1, 0, uy1, 0, ux2, 0, uy2, 0);
    }

    /**
     * Draws in screen space the line specified with user space coordinates. The {@code mx} and {@code my} coordinates
     * are transformed into screen space and then the {@code qx} and {@code qy} are added as offsets in screen space,
     * respectively.
     * @param mx1 reference coordinate in user space
     * @param qx1 offset for scr(mx1), so in screen space
     * @param my1
     * @param qy1
     * @param mx2
     * @param qx2
     * @param qy2
     * @param my2
     */
    public void drawLine(double mx1, int qx1, double my1, int qy1, double mx2, int qx2, double qy2, int my2) {
        int sx1 = getX(mx1) + qx1;
        int sy1 = getY(my1) + qy1;
        int sx2 = getX(mx2) + qx2;
        int sy2 = getY(my2) + qy2;

        g.drawLine(sx1, sy1, sx2, sy2);
    }

    /**
     * Converts user space x-coordinate to a screen space coordinate in the current axis system.
     * @param x
     * @return
     */
    private int us2ss_x(double x) {
        return axify_x((int) (ri.mx * x));
    }

    private int us2ss_y(double y) {
        return (int) (ri.my * y);
    }

    private int axify_x(int x) {
        if (x_r) {
            return x;
        }
        return ri.resx - 1 - x;
    }

    /**
     * Calculates the start point for a shape that must be defined with a leftmost X coordinate.
     * @param info
     * @param x
     * @param w
     * @return
     */
    private int axify_x(int x, int w) {
        if (x_r) {
            return x;
        }
        return ri.resx - x - w;
    }

    private int axify_y(int y) {
        if (y_up) {
            return ri.resy - 1 - y;
        }
        return y;
    }

    private int axify_y(int y, int h) {
        if (y_up) {
            return ri.resy - y - h;
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
    public void fillOval(int x, int y, int dx, int dy) {
        int nx = getX(x);
        int ny = getY(y);
        int startx = nx - dx / 2;
        int starty = ny - dy / 2;

        g.fillOval(startx, starty, dx, dy);
    }

    public void fillRect(int x, int y, int w, int h) {
        int nx = getX(x, w);
        int ny = getY(y, h);
        g.fillRect(nx, ny, w, h);
    }
}
