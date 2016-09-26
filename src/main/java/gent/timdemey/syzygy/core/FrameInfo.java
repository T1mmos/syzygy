package gent.timdemey.syzygy.core;

/**
 * Timings, current frame duration, FPS, current keymask, ...
 * @author Timmos
 */
public class FrameInfo {

    public long prevTime   = Long.MIN_VALUE;
    public long currTime   = Long.MIN_VALUE;
    public long diffTime   = Long.MIN_VALUE;
    public long passedTime = Long.MIN_VALUE;
    public long startTime  = Long.MIN_VALUE;

    public int  currFPS    = 0;

    public long keymask    = 0;

    /**
     * Checks whether the user's input triggers the specified action, and returns {@code true} when it is.
     * @param input
     * @return
     */
    public boolean isInputActive(Input input) {
        return (keymask & input.getBitMask()) != 0;
    }

    public boolean isInputActive(Input ... inputs) {
        for (Input input : inputs) {
            if (isInputActive(input)) {
                return true;
            }
        }
        return false;
    }
}
