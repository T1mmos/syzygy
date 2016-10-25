package gent.timdemey.syzygy.raycast.world;

import gent.timdemey.syzygy.core.Frame;

/**
 * Something that is part of the world, so it has bounds, can participate in collision detection, ...
 * @author Timmos
 */
public interface Thing {
    public boolean isColliding (double x, double y);
    
    public void updateState (Frame f);
}
