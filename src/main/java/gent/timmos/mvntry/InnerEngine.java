package gent.timmos.mvntry;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public final class InnerEngine {
    
    private final JFrame frame;
    private final BufferStrategy strategy;
    private final Engine engine;
    
    private final InternalKeyListener keyL;
    
    private Thread thread = null;
    
    public InnerEngine (JFrame frame, BufferStrategy strategy, Engine engine){
        this.frame = frame;
        this.strategy = strategy;
        this.engine = engine;
        
        this.keyL = new InternalKeyListener(KeyMapper.INSTANCE);
    }
    
    public synchronized void start (){
        if (thread != null){
            throw new IllegalStateException("Engine already running");
        }
        
        frame.addKeyListener(keyL);
        thread = new Thread (new InternalEngine(), "Engine");
        thread.start();
    }
    
    public synchronized void stop (){
        if (thread != null){
            thread.interrupt();
            thread = null;
        }
        frame.removeKeyListener(keyL);
    }
    
    private class InternalEngine implements Runnable {

        @Override
        public void run() {
            
            long prevtime = System.currentTimeMillis();
            final long time_start = prevtime;
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
                long currtime = System.currentTimeMillis();
                long dt = currtime - prevtime;
                int fps = (int) (dt == 0 ? 1000 : 1000 / dt);
                
                UpdateInfo.Builder builder = new UpdateInfo.Builder();
                {
                    builder.setPreviousTime(prevtime);
                    builder.setCurrentTime(currtime);
                    builder.setDiffTime(dt);
                    builder.setPassedTime(currtime - time_start);
                    builder.setFPS(fps);
                    keyL.setPressedKeys(builder);
                }
                UpdateInfo info = builder.build();
                prevtime = currtime;
                
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
