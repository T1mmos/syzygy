package gent.timdemey.syzygy.math3d;

import gent.timdemey.syzygy.CoorSys;
import gent.timdemey.syzygy.WallInfo;
import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.Test;


public class CoorSysTest extends TestCase {

    private static final int [][] WALLS = new int [][] {
        {1,2,1,2,1},
        {2,0,0,0,2},
        {1,0,1,1,1},
        {1,2,1,0,0}
    };

    private CoorSys              cs;

    @Override
    protected void setUp() throws Exception {
        cs = new CoorSys(WALLS);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void testWallAt() {
        // at y = 0 (= adjacent w/ x-axis)
        assertTrue(cs.wall_at(0, 0) > 0);
        assertTrue(cs.wall_at(1, 0) > 0);
        assertTrue(cs.wall_at(2, 0) > 0);
        assertTrue(cs.wall_at(3, 0) == 0);
        assertTrue(cs.wall_at(4, 0) == 0);

        // at y = 1
        assertTrue(cs.wall_at(0, 1) > 0);
        assertTrue(cs.wall_at(1, 1) == 0);
        assertTrue(cs.wall_at(2, 1) > 0);
        assertTrue(cs.wall_at(3, 1) > 0);
        assertTrue(cs.wall_at(4, 1) > 0);

        // at y = 2
        assertTrue(cs.wall_at(0, 2) > 0);
        assertTrue(cs.wall_at(1, 2) == 0);
        assertTrue(cs.wall_at(2, 2) == 0);
        assertTrue(cs.wall_at(3, 2) == 0);
        assertTrue(cs.wall_at(4, 2) > 0);

        // at y = 3
        assertTrue(cs.wall_at(0, 3) > 0);
        assertTrue(cs.wall_at(1, 3) > 0);
        assertTrue(cs.wall_at(2, 3) > 0);
        assertTrue(cs.wall_at(3, 3) > 0);
        assertTrue(cs.wall_at(4, 3) > 0);
    }

    @Test
    public void testBounds() {
        assertEquals(2, CoorSys.bound(1.7, 1, 1));
        assertEquals(3, CoorSys.bound(2.7, 2, 1));
        assertEquals(1, CoorSys.bound(1.7, 1, -1));
        assertEquals(2, CoorSys.bound(2.7, 2, -1));
        assertEquals(2, CoorSys.bound(1.0, 1, 1));
        assertEquals(0, CoorSys.bound(1.0, 1, -1));
        assertEquals(-1, CoorSys.bound(1.0, 1, 0));
        assertEquals(-1, CoorSys.bound(1.7, 1, 0));
    }

    @Test
    public void testGrid2wall() {
        assertEquals(5, CoorSys.grid2wall(5, 1));
        assertEquals(4, CoorSys.grid2wall(5, -1));
        assertEquals(1, CoorSys.grid2wall(1, 1));
        assertEquals(0, CoorSys.grid2wall(1, -1));
    }

    @Test
    public void testIntersect() {
        // corner cases first:
        testIntersectPriv(1.5, 1.5, 0.0, 1.0, 2); // looking north
        testIntersectPriv(1.5, 1.5, 1.0, 0.0, 1); // east
        testIntersectPriv(1.5, 1.5, 0.0, -1.0, 2); // south
        testIntersectPriv(1.5, 1.5, -1.0, 0, 1); // west

        // between the compass points, hitting north or south
        testIntersectPriv(1.5, 2.75, 1.0, 1.0, 2); // north-east
        testIntersectPriv(1.5, 2.75, -1.0, 1.0, 2); // north-west
        testIntersectPriv(1.5, 1.25, 1.0, -1.0, 2); // south-east
        testIntersectPriv(1.5, 1.25, -1.0, -1.0, 2); // south-west

        // between the compass points, hitting east or west
        testIntersectPriv(1.25, 1.5, -1.0, 1.0, 1); // north-west
        testIntersectPriv(1.25, 1.5, -1.0, -1.0, 1); // south-west
        testIntersectPriv(1.75, 1.5, 1.0, 1.0, 1); // north-east
        testIntersectPriv(1.75, 1.5, 1.0, -1.0, 1); // south-east

        // going up 1 unit and to the right 0.5, should hit bottom border of wall (2,3)
        testIntersectPriv(1.5, 1.5, 0.5, 1.0, 1); // south-east
        // same, but to the left, should hit right border of wall (0,2)
        testIntersectPriv(1.5, 1.5, -0.5, 1.0, 2); // south-east

        // some test values that resulted in exceptions
        double[][] trs = new double[][] { { 1.5007869686361113 }, { 2.000999381916119 } };
        double[] angle = new double[] { 0.001570795680858598, 0.9999987662997035 };
        testIntersectPriv(trs[0][0], trs[1][0], angle[0], angle[1], 2);
    }

    private void testIntersectPriv(double px, double py, double mx, double my, int expwall) {
        double[][] pos = new double[][] { { px }, { py } };
        double[] m = new double[] { mx, my };
        WallInfo wall = cs.intersect(pos, m);
        assertEquals(expwall, wall.color);
    }
}
