package gent.timdemey.syzygy.core;

public enum Input {
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
    
    public long getBitMask () {
        return 1 << ordinal();
    }
}
