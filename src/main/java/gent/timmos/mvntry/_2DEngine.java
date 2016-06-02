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
    
    private static final int PLAYFIELD_WIDTH_UNITS = 200;
    private static final int PLAYFIELD_HEIGHT_UNITS = 100;
    private static final int BALL_DIM_UNITS = 10;
    private static final int UNITS_PER_SECOND = 50;
    
    private double x_real = 0.0;
    private double y_real = 0.0;
    
    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateGame(UpdateInfo info) {
        // inputs
        boolean up = info.isInputActive(Input.UP); 
        boolean down = info.isInputActive(Input.DOWN);
        boolean left = info.isInputActive(Input.LEFT);
        boolean right = info.isInputActive(Input.RIGHT);
        boolean speedBoost = info.isInputActive(Input.SPEED_BOOSTER);
        
        // position calculation
        int multiplier_x = 0;
        multiplier_x += right ? 1 : 0;
        multiplier_x += left ? -1 : 0;
        multiplier_x *= speedBoost ? 2 : 1;
        int multiplier_y = 0;
        multiplier_y += up ? -1 : 0;
        multiplier_y += down ? 1 : 0;
        multiplier_y *= speedBoost ? 2 : 1;
        
        // units to (potentially) travel in this frame
        double units = 1.0 * UNITS_PER_SECOND * info.getDiffTime() / 1000;
        
        // collision detection
        double __x_real = x_real + multiplier_x * units;
        if (0 < __x_real && __x_real + BALL_DIM_UNITS < PLAYFIELD_WIDTH_UNITS) {
            x_real = __x_real;
        }
        double __y_real = y_real + multiplier_y * units;
        if (0 < __y_real && __y_real + BALL_DIM_UNITS < PLAYFIELD_HEIGHT_UNITS) {
            y_real = __y_real;
        }
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
        
        int x = (int) (info.width * x_real / PLAYFIELD_WIDTH_UNITS);
        int y = (int) (info.height * y_real / PLAYFIELD_HEIGHT_UNITS);
        int dim = BALL_DIM_UNITS * info.width / PLAYFIELD_WIDTH_UNITS;
        
        g.fillOval(x, y, dim, dim);
    }

}
