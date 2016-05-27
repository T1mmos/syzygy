package gent.timmos.mvntry.math3d;

public class Vector {
    
    /** The start point. */
    private final Matrix t_0;
    /** The (denormalized) direction. */
    private final Matrix m;
    
    public Vector (Point first, Point second) {
        t_0 = first.getMatrix();
        m = first.substractFrom(second).getMatrix();
    }
}
