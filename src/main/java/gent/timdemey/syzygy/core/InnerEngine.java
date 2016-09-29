package gent.timdemey.syzygy.core;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

public final class InnerEngine {

    private final Canvas canvas;
    private final BufferStrategy strategy;
    private final Engine              engine;

    private final InternalKeyListener keyL;

    private Thread thread = null;

    public InnerEngine (Canvas canvas, BufferStrategy strategy, Engine engine){
        this.canvas = canvas;
        this.strategy = strategy;
        this.engine = engine;

        this.keyL = new InternalKeyListener(KeyMapper.INSTANCE);
    }

    public synchronized void start (){
        if (thread != null){
            throw new IllegalStateException("Engine already running");
        }

        canvas.addKeyListener(keyL);
        thread = new Thread (new InternalEngine(), "Engine");
        thread.start();
    }

    public synchronized void stop (){
        if (thread != null){
            thread.interrupt();
            thread = null;
        }
        canvas.removeKeyListener(keyL);
    }

    private class InternalEngine implements Runnable {

        @Override
        public void run() {
            RenderInfo renderInfo = new RenderInfo();
            Frame frameInfo = new Frame();

            frameInfo.t_prev = System.currentTimeMillis();
            frameInfo.t_curr = frameInfo.t_prev;
            frameInfo.t_start = frameInfo.t_prev;

            engine.initialize();

            while (!Thread.currentThread().isInterrupted()) {
                do {
                    renderInfo.resx = canvas.getWidth();
                    renderInfo.resy = canvas.getHeight();

                    Graphics2D bg = (Graphics2D) strategy.getDrawGraphics();
                    engine.renderGame(bg, frameInfo, renderInfo);
                    bg.dispose();
                    bg = null;
                } while (!updateScreen());

                try {
                    Thread.sleep(0, 10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                {
                    long currtime = System.currentTimeMillis();
                    long dt = currtime - frameInfo.t_prev;
                    int fps = (int) (dt == 0 ? 1000 : 1000 / dt);

                    frameInfo.t_prev = frameInfo.t_curr;
                    frameInfo.t_curr = currtime;
                    frameInfo.t_diff = dt;
                    frameInfo.t_passed = currtime - frameInfo.t_start;
                    frameInfo.fps = fps;
                    frameInfo.keymask = keyL.getKeyMask();
                }

                engine.updateState(frameInfo);
            }
        }
    }

    private boolean updateScreen() {
        try {
            strategy.show();
            Toolkit.getDefaultToolkit().sync();
            return !strategy.contentsLost();
        } catch (NullPointerException e) {
            return true;
        } catch (IllegalStateException e) {
            return true;
        }
    }
}
