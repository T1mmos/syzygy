package gent.timmos.mvntry;

public enum Input {
    UP, 
    DOWN,
    LEFT,
    RIGHT,
    FORWARD,
    BACKWARD,
    STRAFE_LEFT,
    STRAFE_RIGHT,
    SPEED_BOOSTER;
    
    public long getBitMask () {
        return 1 << ordinal();
    }
}
