package gent.timdemey.syzygy;

import gent.timdemey.syzygy.math3d._2DVector;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * A simple 2D engine that is just a showcase for what is going on in the 3D version. 
 * This is merely just a tryout in 2D space to get a better understanding of the 
 * math and do easier visualization/rendering, before going to 3D space.
 * @author Timmos
 */
public class _2DEngine implements Engine {
    
    private static final int PLAYFIELD_WIDTH_UNITS = 1000;
    private static final int PLAYFIELD_HEIGHT_UNITS = 1000;
    private static final int UNITS_PER_SECOND = 50;
    
    private double[][] position = new double[][] {{0},{0}};
    private double[][] direction = new double[][] {{1},{0}};
    
    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateGame(UpdateInfo info) {
        boolean forward = info.isInputActive(Input.FORWARD);
    }

    @Override
    public void renderGame(Graphics2D g, RenderInfo info) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillRect(0, 0, info.width, info.height);
        
        int whalf = info.width / 2;
        int hhalf = info.height / 2;
        
        g.setColor(Color.white);
        g.fillOval(x, y, dimx, dimy);
    }

}
