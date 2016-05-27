package gent.timmos.mvntry.math3d;

import junit.framework.TestCase;

public class PointTest extends TestCase {

    protected static void tearDownAfterClass() throws Exception {
    }

    public void testApp()
    {
        Point point = new Point(3.0, 4.0, 5.0);
        
        assertEquals(3.0, point.getMatrix().valueAt(0, 0), 0.0001);
        assertEquals(4.0, point.getMatrix().valueAt(1, 0), 0.0001);
        assertEquals(5.0, point.getMatrix().valueAt(2, 0), 0.0001);
    }
}
