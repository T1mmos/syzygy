package gent.timmos.mvntry.math3d;

public class _3DVector {
    
    /** The start point. */
    private final double[][] t_0;
    /** The (denormalized) direction. */
    private final double[][] m;
    
    public _3DVector (double[][] direction, double[][] offset) {
        t_0 = offset;
        m = direction;
    }
}
