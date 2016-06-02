package gent.timmos.mvntry.math3d;

public class _2DVector {
    
    /** The start point. */
    private final double[][] t_0;
    /** The (denormalized) direction. */
    private final double[][] m;
    
    public _2DVector (double[][] direction, double[][] offset) {
        t_0 = offset;
        m = direction;
    }
    
}
