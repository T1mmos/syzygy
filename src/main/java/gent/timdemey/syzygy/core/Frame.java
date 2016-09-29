package gent.timdemey.syzygy.core;

/**
 * Timings, current frame duration, FPS, current keymask, ...
 * @author Timmos
 */
public class Frame {

    public long t_prev   = Long.MIN_VALUE;
    public long t_curr   = Long.MIN_VALUE;
    public long t_diff   = Long.MIN_VALUE;
    public long t_passed = Long.MIN_VALUE;
    public long t_start  = Long.MIN_VALUE;

    public int  fps    = 0;

    public long keymask    = 0;

    /**
     * Checks whether the user's inputs triggers the action, and returns {@code true} when it is.
     * @param action
     * @return
     */
    public boolean isActive(Action action) {
        return (keymask & action.getBitMask()) != 0;
    }

    /**
     * Checks whether at least one of the actions is active.
     * @param actions
     * @return
     */

    public boolean isActive(Action ... actions) {
        for (Action input : actions) {
            if (isActive(input)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOneActive(Action ... actions) {
        boolean found = false;
        for (Action input : actions) {
            if (isActive(input)) {
                if (found) {
                    return false; // already had one active
                }
                found = true;
            }
        }
        return found;
    }
}
