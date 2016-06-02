package gent.timmos.mvntry;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InternalKeyListener implements KeyListener {
    
    private final KeyMapper mapper;
    
    private volatile long keymask = 0;
    
    InternalKeyListener (KeyMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Input input = mapper.getInput(e.getKeyCode());
        if (input == null) {
            return;
        }
        keymask |= input.getBitMask();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Input input = mapper.getInput(e.getKeyCode());
        if (input == null) {
            return;
        }
        keymask &= ~input.getBitMask();
    }
    
    /**
     * Fills in the currently pressed keys. Keys events are processed on the 
     * EDT, and this method should be called only from the inner engine's looping
     * thread, so there is a little overhead in synchronization.
     * @param builder
     */
    void setPressedKeys (UpdateInfo.Builder builder) {
        builder.setKeyMask(keymask);
    }
}
