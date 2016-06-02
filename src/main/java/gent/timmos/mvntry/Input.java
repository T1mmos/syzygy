package gent.timmos.mvntry;

public enum Input {
    FORWARD,
    BACKWARD,
    STRAFE_RIGHT,
    STRAFE_LEFT,
    SPEED_BOOSTER;
    
    public long getBitMask () {
        return 1 << ordinal();
    }
}
