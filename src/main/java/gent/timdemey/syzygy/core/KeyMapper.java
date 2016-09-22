package gent.timdemey.syzygy.core;

import java.awt.event.KeyEvent;

/**
 * Maps actual physical keys onto their abstracted function ({@link Input}). 
 * This allows a user to reconfigure key bindings.
 * @author Timmos
 */
public enum KeyMapper {
    
    INSTANCE;
    
    // possibly read from config file. hardcoded for now.
    
    /**
     * Gets the game input abstraction bound to the given physical key.
     * This method returns null if the key isn't mapped.
     * @param keycode
     * @return
     */
    public Input getInput (int keycode) {
        switch (keycode) {
        case KeyEvent.VK_W:
            return Input.FORWARD;
        case KeyEvent.VK_S:
            return Input.BACKWARD;
        case KeyEvent.VK_A:
            return Input.LEFT;
        case KeyEvent.VK_D:
            return Input.RIGHT;
        case KeyEvent.VK_SHIFT:
            return Input.SPEED_BOOSTER;
        case KeyEvent.VK_SPACE:
            return Input.JUMP;
        case KeyEvent.VK_CONTROL:
            return Input.CROUCH;
        default:
            return null;
        }
    }
}
