package gent.timdemey.syzygy;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

public final class InnerEngine {

    private final Canvas canvas;
    private final BufferStrategy strategy;
    private final Engine engine;

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
            FrameInfo info = new FrameInfo();

            info.prevTime = System.currentTimeMillis();
            info.currTime = info.prevTime;
            info.startTime = info.prevTime;

            engine.initialize();

            while (!Thread.currentThread().isInterrupted()) {
                do {
                    Graphics2D bg = (Graphics2D) strategy.getDrawGraphics();


                    engine.renderGame(bg, info);
                    bg.dispose();
                    bg = null;
                } while (!updateScreen());

                // update frame info
                {
                    long currtime = System.currentTimeMillis();
                    long dt = currtime - info.prevTime;
                    int fps = (int) (dt == 0 ? 1000 : 1000 / dt);

                    info.prevTime = info.currTime;
                    info.currTime = currtime;
                    info.diffTime = dt;
                    info.passedTime = currtime - info.startTime;
                    info.currFPS = fps;
                    info.keymask = keyL.getKeyMask();
                    info.width = canvas.getWidth();
                    info.height = canvas.getHeight();
                }

                engine.updateGame(info);
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
