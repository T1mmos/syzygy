package gent.timdemey.syzygy.math;

import org.junit.Assert;

import gent.timdemey.syzygy.math.MatrixOps;
import junit.framework.TestCase;

public class MatrixOpsTest extends TestCase {

    private static double[][] input = new double[][] {
        {2,3,0},
        {1,2,0},
        {0,2,1}
    };
    
    private static void assert2Darray (double[][] exp, double[][] ret) {
        assertEquals(exp.length, ret.length);
        assertEquals(exp[0].length, ret[0].length);
        for (int i = 0; i < ret.length; i++) {
            Assert.assertArrayEquals(exp[i], ret[i], 0.00001);
        }
    }
    
    public void testMultiply() throws Exception {
        double[][] ret = MatrixOps.multiply(input, input);
        double[][] exp = new double[][] {
                {7.0, 12.0,  0.0},
                {4.0,  7.0,  0.0},
                {2.0,  6.0,  1.0}};
        
        assert2Darray(exp, ret);
    }
    
    
    public void testAdd() throws Exception {       
        double[][] ret = MatrixOps.add(input, input);
        double[][] exp = new double[][] {
                {4, 6, 0},
                {2, 4, 0},
                {0, 4, 2}};
        
        assert2Darray(exp, ret);
    }
    
    public void testInvert() throws Exception {
        double[][] ret = MatrixOps.invert(input);
        double[][] exp = new double[][] {
                {2, -3, 0},
                {-1, 2, 0},
                {2, -4, 1}
        };
        assert2Darray(exp, ret);
    }
}
