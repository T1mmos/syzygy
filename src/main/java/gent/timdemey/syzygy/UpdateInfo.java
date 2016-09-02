package gent.timdemey.syzygy;

public final class UpdateInfo {

    private long prevTime = Long.MIN_VALUE;
    private long currTime = Long.MIN_VALUE;
    private long diffTime = Long.MIN_VALUE;
    private long passedTime = Long.MIN_VALUE;

    private int currFPS = 0;

    private long keymask = 0;

    private UpdateInfo (){
    }

    public long getPreviousTime () {
        return prevTime;
    }

    public long getCurrentTime () {
        return currTime;
    }

    public long getDiffTime () {
        return diffTime;
    }

    public long getPassedTime () {
        return passedTime;
    }

    public int getFPS () {
        return currFPS;
    }

    /**
     * Gets all input abstractions in the current frame as a bitmask.
     * The bit position for each abstraction is defined by the
     * ordinal number of each {@link Input} instance.
     * <p>The method {@link #isInputActive(Input)} is more convenient to use.
     * @return
     */
    public long getInputMask () {
        return keymask;
    }

    /**
     * Checks whether the user's input triggers the specified action, and returns
     * {@code true} when it is.
     * @param input
     * @return
     */
    public boolean isInputActive (Input input) {
        return (getInputMask() & input.getBitMask()) != 0;
    }

    public boolean isInputActive(Input ... inputs) {
        for (Input input : inputs) {
            if (isInputActive(input)) {
                return true;
            }
        }
        return false;
    }

    public static final class Builder {

        private boolean built = false;
        private final UpdateInfo info;

        public Builder () {
            this.info = new UpdateInfo();
        }

        public void setPreviousTime (long time) {
            assert !built;
            info.prevTime = time;
        }

        public void setCurrentTime (long time) {
            assert !built;
            info.currTime = time;
        }

        public void setDiffTime (long time) {
            assert !built;
            info.diffTime = time;
        }

        public void setPassedTime (long time) {
            assert !built;
            info.passedTime = time;
        }

        public void setFPS (int fps) {
            assert !built;
            info.currFPS = fps;
        }

        public void setKeyMask (long mask) {
            assert !built;
            info.keymask = mask;
        }

        public UpdateInfo build () {
            built = true;
            return info;
        }


    }
}
