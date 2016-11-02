package gent.timdemey.syzygy.playgr;


public class PointManager {

    private final Matrix[] points;

    public PointManager() {
        this.points = new Matrix[16];

        // cube
        points[0] = new Matrix(4, 1, 4.0, -1.0, -1, 1);
        points[1] = new Matrix(4, 1, 4.0, -1.0, +1, 1);
        points[2] = new Matrix(4, 1, 6.0, -1.0, -1, 1);
        points[3] = new Matrix(4, 1, 6.0, -1.0, +1, 1);
        points[4] = new Matrix(4, 1, 4.0, +1.0, -1, 1);
        points[5] = new Matrix(4, 1, 4.0, +1.0, +1, 1);
        points[6] = new Matrix(4, 1, 6.0, +1.0, -1, 1);
        points[7] = new Matrix(4, 1, 6.0, +1.0, +1, 1);

        points[8] = new Matrix(4, 1, 4.0, 3.0, -1, 1);
        points[9] = new Matrix(4, 1, 4.0, 3.0, +1, 1);
        points[10] = new Matrix(4, 1, 6.0, 3.0, -1, 1);
        points[11] = new Matrix(4, 1, 6.0, 3.0, +1, 1);
        points[12] = new Matrix(4, 1, 4.0, 5.0, -1, 1);
        points[13] = new Matrix(4, 1, 4.0, 5.0, +1, 1);
        points[14] = new Matrix(4, 1, 6.0, 5.0, -1, 1);
        points[15] = new Matrix(4, 1, 6.0, 5.0, +1, 1);
    }

    public Matrix get(int nr) {
        return points[nr];
    }
}
