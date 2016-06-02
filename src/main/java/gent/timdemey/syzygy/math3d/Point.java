package gent.timdemey.syzygy.math3d;

public class Point {
    
    private final double[][] t;
    
    public Point (double x, double y, double z) {
        this.t = new double[][] {{x},{y},{z}};
    }
    
    public double[][] getMatrix () {
        return t;
    }
    
    public Point substractFrom (Point other) {
        double diff_x = other.t[0][0] - this.t[0][0];
        double diff_y = other.t[1][0] - this.t[1][0];
        double diff_z = other.t[2][0] - this.t[2][0];
        
        return new Point(diff_x, diff_y, diff_z);
    }
}
