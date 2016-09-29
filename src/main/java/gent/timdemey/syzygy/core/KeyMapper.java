package gent.timdemey.syzygy.core;

import java.awt.event.KeyEvent;

/**
 * Maps actual physical keys onto their abstracted function ({@link Action}). 
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
    public Action getInput (int keycode) {
        switch (keycode) {
        case KeyEvent.VK_W:
            return Action.FORWARD;
        case KeyEvent.VK_S:
            return Action.BACKWARD;
        case KeyEvent.VK_A:
            return Action.LEFT;
        case KeyEvent.VK_D:
            return Action.RIGHT;
        case KeyEvent.VK_SHIFT:
            return Action.SPEED_BOOSTER;
        case KeyEvent.VK_SPACE:
            return Action.JUMP;
        case KeyEvent.VK_CONTROL:
            return Action.CROUCH;
        default:
            return null;
        }
    }
}
