package gent.timdemey.syzygy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;



public class Camera {

    private static int diameter = 15;
    private static final _3DPoint[] points = new _3DPoint[8];
    private static double planeX = 1;

    static {
        points[0] = new _3DPoint(30, diameter, 0);
        points[1] = new _3DPoint(30, 3*diameter, 0);
        points[2] = new _3DPoint(30,3*diameter,30);
        points[3] = new _3DPoint(30,diameter,30);
        points[4] = new _3DPoint(60,diameter,30);
        points[5] = new _3DPoint(60,3*diameter,30);
        points[6] = new _3DPoint(60,diameter,00);
        points[7] = new _3DPoint(60,3*diameter,00);
    }

    public static void main(String[] args) {
        final _3DPane pane = new _3DPane();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Camera().start(pane);
            }
        });
        new Thread(new Runnable() {
            public void run() {
                animate(pane);
            }
        },"animation").start();
    }

    private static void animate(_3DPane pane){
        long time = System.currentTimeMillis();
        double rot = Math.PI / 4;
        while(true){
            long time2 = System.currentTimeMillis();
            long diff = time2 - time;            
            time = time2;
            
            for (int i = 0; i < 4; i++){
                double angle = rot + i * Math.PI / 2;
                double cos= diameter + diameter*Math.cos(angle) * Math.sqrt(2);
                double sin = diameter*Math.sin(angle) * Math.sqrt(2);
                
                double x = 60 + cos;
                double z = 0 + sin; // central on screen
                
                points[2*i+1].x =points[2*i].x  = x; 
                points[2*i+1].z =points[2*i].z = z;
            }
            rot += Math.PI * diff / 1000 / 2;
            pane.repaint();
        }
    }
    
    private void start(_3DPane pane) {
        JFrame frame = new JFrame ();

        Mouse mouse = new Mouse(pane);
        pane.addMouseMotionListener(mouse);
        pane.addMouseListener(mouse);
        pane.addMouseWheelListener(mouse);

        frame.setContentPane(pane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private class Mouse extends MouseAdapter  {

        private final _3DPane pane;
        int previousX;
        int                   previousY;

        Mouse(_3DPane pane) {
            this.pane = pane;
        }
        @Override
        public void mousePressed(MouseEvent e) {
            previousX = e.getX();
            previousY = e.getY();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            double addx = 0.0;
            if (x < previousX) {
                addx = +0.1;
            } else if (x > previousX) {
                addx = -0.1;
            }
            previousX = x;
            double addy = 0.0;
            if (y < previousY) {
                addy = +0.1;
            } else if (y > previousY) {
                addy = -0.1;
            }
            previousY = y;

            for (_3DPoint p3d : points) {
                p3d.z += addx;
                p3d.y += addy;
            }
            pane.repaint();
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            planeX += 1.0 * e.getWheelRotation();
        }
        
    }

    private static class _3DPoint {

        private double x, y, z;

        private _3DPoint(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static class _2DPoint {

        private final double x, y;

        private _2DPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class _3DPane extends JPanel {


        private final _2DPoint[] projections = new _2DPoint[points.length];

        private _3DPane() {
        }

        private void project() {
            for (int i = 0; i < points.length; i++) {
                _3DPoint p3d = points[i];
                double x_proj = 1; // we project onto planeX
                double y_proj = (p3d.y + 1) / (p3d.x - planeX);
                double z_proj = (p3d.z + 1) / (p3d.x - planeX);

                // convert into screen space coordinate system
                double x_screen = -100 * z_proj;
                double y_screen = -100 * y_proj + 100;

                projections[i] = new _2DPoint(x_screen, y_screen);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {

            project();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            g.setColor(Color.WHITE);
            for (int i = 0; i < points.length; i++) {
                _2DPoint p1 = projections[i];
                for (int j = 0; j < points.length; j++) {
                    if (i == j){
                        continue;
                    }
                    int cntdiff = 0;
                    cntdiff += points[i].x != points[j].x ? 1 : 0;
                    cntdiff += points[i].y != points[j].y ? 1 : 0;
                    cntdiff += points[i].z != points[j].z ? 1 : 0;
                    if (cntdiff > 2){
                        continue;
                    }
                    _2DPoint p2 = projections[j];
                    int x1 = (int) p1.x;
                    int y1 = (int) p1.y;
                    int x2 = (int)  p2.x;
                    int y2 = (int)  p2.y;
                    
                    
                    g.drawLine(x1+100,y1,x2+100,y2);
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }
    }
}

