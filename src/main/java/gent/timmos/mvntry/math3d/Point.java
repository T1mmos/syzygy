package gent.timmos.mvntry.math3d;

public class Point {
    
    private final Matrix t;
    
    public Point (double x, double y, double z) {
        double[][] values = new double[][] {{x},{y},{z}};
        this.t = new Matrix(values);
    }
    
    public Matrix getMatrix () {
        return t;
    }
    
    public Point substractFrom (Point other) {
        double diff_x = other.t.valueAt(0, 0) - this.t.valueAt(0, 0);
        double diff_y = other.t.valueAt(1, 0) - this.t.valueAt(1, 0);
        double diff_z = other.t.valueAt(2, 0) - this.t.valueAt(2, 0);
        
        return new Point(diff_x, diff_y, diff_z);
    }
}
