package gent.timdemey.syzygy.math3d;

public class _3DPlane {
    /** The start point. */
    private final double[][] t_0;
    /** The (denormalized) first direction. */
    private final double[][] m_0;
    /** The (denormalized) second direction. */
    private final double[][] m_1;
    
    public _3DPlane (double[][] direction1, double[][] direction2, double[][] offset) {
        t_0 = offset;
        m_0 = direction1;
        m_1 = direction2;
    }
}
