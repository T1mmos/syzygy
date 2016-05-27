package gent.timmos.mvntry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class PongEngine implements Engine {

    private static class Point3D {
        public final double x, y, z;
        public Point3D (double x, double y, double z) {
            this.x=x;
            this.y=y;
            this.z=z;
        }
    }
    
    private double alpha = 0.0;
    
    private Point3D [] cube_projected = null;
    private long ms_since_start = 0L;
    
    public PongEngine (){
        
    }

    @Override
    public void initialize() {
        
    }
    
    @Override
    public void updateGame(UpdateInfo info) {
        // just a static image for now, no updates based on ticks
        
        alpha += 2*Math.PI * info.diffTime / 2000;
        double cos = 1.5 * Math.cos(alpha);
        double sin = 1.5 * Math.sin(alpha);
        
        double x0 = 4.5 + cos;
        double x1 = 4.5 - cos;
        double z0 = 0 + sin;
        double z1 = 0 - sin;
                
        cube_projected = new Point3D [8];
        for (int i = 0; i < 8; i++) {
            
            double y = (i / 2) % 2 == 0 ? 3 : -3;
            double dist_nofisheye = orig.x;
            double y_2D = y / dist_nofisheye;
            double x_2D = - orig.z / dist_nofisheye;
            
            Point3D conv = new Point3D (100 + 100 * x_2D, 100 + 100 * y_2D, 0);
            cube_projected [i] = conv;
        }
        ms_since_start = info.passedTime;
    }

    @Override
    public void renderGame(Graphics2D g, RenderInfo info) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, info.width, info.height);
       
        g.setColor(Color.WHITE);
        
        if (cube_projected == null) {
            return;
        }
        
        g.drawRect(100, 100, 100, 100);
        
        // front square
        PongEngine.render(g, cube_projected[0], cube_projected[1]);
        PongEngine.render(g, cube_projected[1], cube_projected[2]);
        PongEngine.render(g, cube_projected[2], cube_projected[3]);
        PongEngine.render(g, cube_projected[3], cube_projected[0]);
        
        // back square
        PongEngine.render(g, cube_projected[4], cube_projected[5]);
        PongEngine.render(g, cube_projected[5], cube_projected[6]);
        PongEngine.render(g, cube_projected[6], cube_projected[7]);
        PongEngine.render(g, cube_projected[7], cube_projected[4]);
        
        // lines from front to back
        PongEngine.render(g, cube_projected[0], cube_projected[4]);
        PongEngine.render(g, cube_projected[1], cube_projected[5]);
        PongEngine.render(g, cube_projected[2], cube_projected[6]);
        PongEngine.render(g, cube_projected[3], cube_projected[7]);
        
        g.drawString(ms_since_start + " ms", 100, 100);
    }
    
    private static void render (Graphics2D g, Point3D first, Point3D second) {
        g.drawLine((int) first.x, (int) first.y, (int) second.x, (int) second.y);
    }
}
