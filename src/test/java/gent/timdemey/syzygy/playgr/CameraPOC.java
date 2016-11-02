package gent.timdemey.syzygy.playgr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;



public class CameraPOC {

    private final VertexManager     vertexMan;
    private final PointManager      pointMan;

    private final LineManager   lineMan;
    private final ProjectionManager projMan;

    private final _3DPane           pane;

    private final Camera            cam;

    public CameraPOC() {
        this.vertexMan = new VertexManager();
        this.lineMan = new LineManager();
        this.projMan = new ProjectionManager();
        this.pointMan = new PointManager();

        this.cam = new Camera();
        this.cam.setFov(Math.toRadians(70));
        this.cam.recalc();
        this.pane = new _3DPane();

    }

    public static void main(String[] args) {
        final CameraPOC test = new CameraPOC();
        test.startUI();
    }

    private void startUI() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                doStartUI();
            }
        });
    }

    private void doStartUI() {
        JFrame frame = new JFrame();

        Mouse mouse = new Mouse();
        pane.addMouseMotionListener(mouse);
        pane.addMouseListener(mouse);

        frame.setContentPane(pane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        snap();

        this.pane.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                cam.setResolution(pane.getSize().width, pane.getSize().height);
                cam.recalc();
            }
        });

        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_W == e.getKeyCode()) {
                    cam.addPinhole(+0.25, 0, 0);
                } else if (KeyEvent.VK_S == e.getKeyCode()) {
                    cam.addPinhole(-0.25, 0, 0);
                } else if (KeyEvent.VK_A == e.getKeyCode()) {
                    cam.addPinhole(0, 0, -0.25);
                } else if (KeyEvent.VK_D == e.getKeyCode()) {
                    cam.addPinhole(0, 0, +0.25);
                } else if (KeyEvent.VK_SPACE == e.getKeyCode()) {
                    cam.addPinhole(0, +0.25, 0);
                } else if (KeyEvent.VK_SHIFT == e.getKeyCode()) {
                    cam.addPinhole(0, -0.25, 0);
                } else if (KeyEvent.VK_UP == e.getKeyCode()) {
                    cam.setFov(cam.getFov() + Math.toRadians(5));
                } else if (KeyEvent.VK_DOWN == e.getKeyCode()) {
                    cam.setFov(cam.getFov() + Math.toRadians(-5));
                }
                cam.recalc();
                snap();
            }
        });
    }

    private void projectVertex(int vref) {
        Vertex vertex = vertexMan.get(vref);

        int p1ref = vertex.getRef1();
        int p2ref = vertex.getRef2();

        projectPoint(p1ref);
        projectPoint(p2ref);

        lineMan.addLine(vref, new Line(p1ref, p2ref));
    }

    private void projectPoint(int pref) {
        if (projMan.get(pref) != null) {
            return;
        }
        Matrix point = pointMan.get(pref);
        Matrix proj = cam.snap(point);
        Projection p = new Projection((int) proj.get(0, 0), (int) proj.get(1, 0));
        projMan.setProjection(pref, p);
    }

    private void snap() {
        lineMan.clear();
        projMan.clear();
        for (int i = 0; i < vertexMan.getCount(); i++) {
            projectVertex(i);
        }
        pane.repaint();
    }

    private class _3DPane extends JPanel {

        private _3DPane() {
        }

        @Override
        protected void paintComponent(Graphics g) {

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(Color.WHITE);

            for (int i = 0; i < lineMan.getLineCount(); i++) {
                Line line = lineMan.get(i);
                Projection proj1 = projMan.get(line.getRef1());
                Projection proj2 = projMan.get(line.getRef2());

                g.drawLine(proj1.x, proj1.y, proj2.x, proj2.y);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(300, 200);
        }
    }

    private class Mouse extends MouseAdapter  {

        int previousX;
        int previousY;

        @Override
        public void mousePressed(MouseEvent e) {
            previousX = e.getX();
            previousY = e.getY();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            double yaw = Math.signum(previousX - x) * 0.02;
            previousX = x;

            double pitch = Math.signum(previousY - y) * 0.02 * 200 / 300;
            previousY = y;

            cam.addYaw(yaw);
            cam.addPitch(pitch);
            cam.recalc();

            snap();
        }
    }
}

