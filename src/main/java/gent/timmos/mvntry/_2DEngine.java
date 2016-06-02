package gent.timmos.mvntry;

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
    
    private static final int PIXELS_PER_SECOND = 50;
    
    private double x_real = 0.0;
    private int x = 0;
    
    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateGame(UpdateInfo info) {
        boolean goForward = info.isInputActive(Input.FORWARD); 
        boolean goBackward = info.isInputActive(Input.BACKWARD);
        boolean speedBoost = info.isInputActive(Input.SPEED_BOOSTER);
        int multiplier = 0;
        multiplier += goForward ? 1 : 0;
        multiplier += goBackward ? -1 : 0;
        multiplier *= speedBoost ? 2 : 1;
        x_real += 1.0 * multiplier * PIXELS_PER_SECOND * info.getDiffTime() / 1000;
        x = (int) x_real;
    }

    @Override
    public void renderGame(Graphics2D g, RenderInfo info) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillRect(0, 0, info.width, info.height);
        
        int whalf = info.width / 2;
        int hhalf = info.height / 2;
        
        
        g.setColor(Color.lightGray);
        g.drawLine(0, hhalf, info.width, hhalf);
        g.drawLine(whalf, 0, whalf, info.height);
        
        g.fillOval(x, hhalf - 20, 10, 10);
    }

}
