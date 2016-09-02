package gent.timdemey.syzygy.math;

public class Plane {
    /** The start point. */
    private final double[][] t_0;
    /** The (denormalized) first direction. */
    private final double[][] m_0;
    /** The (denormalized) second direction. */
    private final double[][] m_1;
    
    public Plane (Point first, Point second, Point third) {
        t_0 = first.getMatrix();
        m_0 = first.substractFrom(second).getMatrix();
        m_1 = first.substractFrom(third).getMatrix();
    }
}
