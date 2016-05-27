package gent.timmos.mvntry;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public final class InnerEngine {
    
    private final JFrame frame;
    private final BufferStrategy strategy;
    private final Engine engine;
    
    private Thread thread = null;
    
    public InnerEngine (JFrame frame, BufferStrategy strategy, Engine engine){
        this.frame = frame;
        this.strategy = strategy;
        this.engine = engine;
    }
    
    public synchronized void start (){
        if (thread != null){
            throw new IllegalStateException("Engine already running");
        }
        thread = new Thread (new InternalEngine(), "Engine");
        thread.start();
    }
    
    public synchronized void stop (){
        if (thread != null){
            thread.interrupt();
            thread = null;
        }
    }
    
    private class InternalEngine implements Runnable {

        @Override
        public void run() {
            
            long time = System.currentTimeMillis();
            final long time_start = time;
            engine.initialize();
            while (!Thread.currentThread().isInterrupted()) {
                do {
                    Graphics2D bg = (Graphics2D) strategy.getDrawGraphics();
                    int width = frame.getWidth();
                    int height = frame.getHeight();
                    RenderInfo info = new RenderInfo (width, height);
                    
                    engine.renderGame(bg, info);
                    bg.dispose();
                    bg = null;
                } while (!updateScreen());

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    break;
                }
                long time2 = System.currentTimeMillis();
                long dt = time2 - time;
                int fps = (int) (dt == 0 ? 1000 : 1000 / dt);
                UpdateInfo info = new UpdateInfo(time, time2, dt, time2 - time_start, fps);
                
                time = time2;
                
                engine.updateGame(info);
            }
        }
    }
    
    private boolean updateScreen() {
        try {
            strategy.show();
            Toolkit.getDefaultToolkit().sync();
            return (!strategy.contentsLost());
        } catch (NullPointerException e) {
            return true;
        } catch (IllegalStateException e) {
            return true;
        }
    }
}
