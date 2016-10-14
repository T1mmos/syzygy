package gent.timdemey.syzygy.raycast.world;


public class Map {

    public final int      wallcntx;
    public final int      wallcnty;

    public final Wall[][] walls;

    private Map(Wall[][] walls) {
        this.walls = walls;
        this.wallcntx = walls.length;
        this.wallcnty = walls[0].length;
    }

    public Wall at(int x, int y) {
        return walls[wallcnty - 1 - y][x];
    }

    public boolean isWall(int x, int y) {
        return at(x, y) != null;
    }

    public static Map create(int[][] raw) {
        Wall[][] walls = new Wall[raw.length][raw[0].length];
        for (int i = 0; i < raw.length; i++) {
            for (int j = 0; j < raw[0].length; j++) {
                walls[i][j] = raw[i][j] != 0 ? new Wall(raw[i][j], i, j) : null;
            }
        }
        return new Map(walls);
    }
}
