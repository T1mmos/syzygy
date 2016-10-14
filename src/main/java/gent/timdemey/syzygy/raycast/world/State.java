package gent.timdemey.syzygy.raycast.world;

/**
 * Internal state of the world, player position, rotation, wall hits, etc. This is
 * user space as opposed to screen space (which holds values for on screen rendering).
 * @author Timmos
 */
public class State {

    public int          raycount = 64;

    public final Player player;
    public Map          map      = null;

    /** Holds information for each casted ray. */
    public Ray[]        rays     = new Ray[raycount];

    public State() {
        this.player = new Player();
    }

    public void loadMap(Map map) {
        this.map = map;
    }
}
