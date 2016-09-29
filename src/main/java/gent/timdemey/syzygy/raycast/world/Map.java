package gent.timdemey.syzygy.raycast.world;


public class Map {

    private final int[][] walls   = new int[][] {
                    {1,2,1,2,1,2,1,2,1,2,1},
                    {2,0,0,0,0,0,0,0,1,0,2},
                    {1,0,1,0,1,0,0,0,1,0,1},
                    {1,0,2,2,1,0,1,0,0,0,2},
                    {1,0,0,0,1,0,0,0,0,0,1},
                    {1,0,0,0,1,1,1,0,1,2,1},
                    {1,0,0,0,0,0,0,0,1,0,1},
                    {1,2,1,2,1,2,0,2,1,0,1},
                    {1,0,0,0,0,0,0,0,0,0,1},
                    {1,2,1,2,1,2,1,2,1,2,1},
    };

    public final int     walls_x = walls[0].length;
    public final int     walls_y = walls.length;

    public int[][] getWalls (){
        return walls;
    }

    public int at(int x, int y) {
        return walls[walls_y - 1 - y][x];
    }

    public boolean isWall(int x, int y) {
        return at(x, y) != 0;
    }
}
