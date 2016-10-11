package gent.timdemey.syzygy.raycast.world;

/**
 * Internal state of the world, player position, rotation, wall hits, etc. This is
 * user space as opposed to screen space (which holds values for on screen rendering).
 * @author Timmos
 */
public class GameState {

    public int          raycount = 64;

    public final Player player;
    public final Map    map;

    /** Holds wall information for each casted ray. */
    public WallInfo[]   wall     = new WallInfo[raycount];

    public GameState() {
        this.player = new Player();
        this.map = new Map();

        for (int i = 0; i < raycount; i++) {
            wall[i] = new WallInfo(0, 0, new double[][] { {} }, 0, 0, 0);
        }
    }





}
