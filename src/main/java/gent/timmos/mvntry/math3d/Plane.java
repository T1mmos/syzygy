package gent.timmos.mvntry.math3d;

public class Plane {
    /** The start point. */
    private final Matrix t_0;
    /** The (denormalized) first direction. */
    private final Matrix m_0;
    /** The (denormalized) second direction. */
    private final Matrix m_1;
    
    public Plane (Point first, Point second, Point third) {
        t_0 = first.getMatrix();
        m_0 = first.substractFrom(second).getMatrix();
        m_1 = first.substractFrom(third).getMatrix();
    }
}
