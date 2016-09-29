package gent.timdemey.syzygy.core;

/**
 * Enumerates the different actions. Keys are mapped to actions by a {@link KeyMapper}.
 * @author Timmos
 */
public enum Action {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    FORWARD,
    BACKWARD,
    STRAFE_LEFT,
    STRAFE_RIGHT,
    SPEED_BOOSTER,
    JUMP,
    CROUCH;

    /**
     * Gets the bitmask associated to this input command.
     * @return
     */
    public long getBitMask () {
        return 1 << ordinal();
    }
}
