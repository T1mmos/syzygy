package gent.timmos.mvntry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class PongEngine implements Engine {

    private int x100 = 0;
    
    private static final int SPEED = 20; // pixels per second
    
    public PongEngine (){
        
    }

    @Override
    public void initialize() {
        
    }
    
    @Override
    public void updateGame(UpdateInfo info) {
        int pixels = (int) (info.diffTime * SPEED / 10);
        x100 += pixels;
        
    }

    @Override
    public void renderGame(Graphics2D g, RenderInfo info) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, info.width, info.height);
        g.setColor(Color.WHITE);
        g.fillOval(x100/100, x100/100, 100, 100);
    }
}
