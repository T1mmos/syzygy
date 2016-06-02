package gent.timmos.mvntry;

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
        case KeyEvent.VK_UP:
            return Input.FORWARD;
        case KeyEvent.VK_DOWN:
            return Input.BACKWARD;
        case KeyEvent.VK_LEFT:
            return Input.STRAFE_LEFT;
        case KeyEvent.VK_RIGHT:
            return Input.STRAFE_RIGHT;
        case KeyEvent.VK_SHIFT:
            return Input.SPEED_BOOSTER;
        default:
            return null;
        }
    }
}
