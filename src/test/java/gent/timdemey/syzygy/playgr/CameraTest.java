package gent.timdemey.syzygy.playgr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;



public class CameraTest {

    private final VertexManager     vertexMan;
    private final PointManager      pointMan;

    private final LineManager   lineMan;
    private final ProjectionManager projMan;

    private final _3DPane           pane;

    private final Camera            cam;

    public CameraTest() {
        this.vertexMan = new VertexManager();
        this.lineMan = new LineManager();
        this.projMan = new ProjectionManager();
        this.pointMan = new PointManager();

        this.cam = new Camera();
        this.cam.setFov(Math.toRadians(60));
        this.cam.recalc();
        this.pane = new _3DPane();

        this.pane.addComponentListener(new ComponentListener() {

            @Override
            public void componentShown(ComponentEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void componentResized(ComponentEvent e) {
                cam.setResolution(pane.getSize().width, pane.getSize().height);
                cam.recalc();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void componentHidden(ComponentEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }

    public static void main(String[] args) {
        final CameraTest test = new CameraTest();
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
        JFrame frame = new JFrame ();

        Mouse mouse = new Mouse();
        pane.addMouseMotionListener(mouse);
        pane.addMouseListener(mouse);
        pane.addMouseWheelListener(mouse);

        frame.setContentPane(pane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
        for (int i = 0; i < 12; i++) {
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
            return new Dimension(400, 300);
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

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int cnt = e.getWheelRotation();
            if ((e.getModifiers() & KeyEvent.CTRL_MASK) == 0) {
                double x = cam.getPinhole().get(0, 0);
                cam.getPinhole().set(0, 0, x - cnt * 0.25);
            } else {
                double y = cam.getPinhole().get(0, 1);
                cam.getPinhole().set(0, 1, y - cnt * 0.25);
            }

            cam.recalc();

            snap();
        }
    }
}

