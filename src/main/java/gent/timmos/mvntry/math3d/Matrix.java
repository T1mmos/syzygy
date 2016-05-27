package gent.timmos.mvntry.math3d;


public class Matrix {
    
    private final int height, width;
    private final double [][] A;
    
    /**
     * Creates a new matrix with the given numbers. The given array should consist 
     * of arrays of rows, so the 2-dimensional array has primary dimension of the number of rows,
     * and a secondary dimension that equals the number of columns.
     * @param nrs
     */
    public Matrix (double[][] nrs) {
        this.height = nrs.length;        
        if (height == 0) {
            throw new IllegalArgumentException("Matrix height must be greater than 0");
        }
        this.width = nrs[0].length;
        if (width == 0) {
            throw new IllegalArgumentException("Matrix width must be greater than 0");
        }
        
        this.A = new double[height][width];
        for (int i = 0; i < height; i++) {
            System.arraycopy(nrs[i], 0, A[i], 0, width);
        }
    }
      
    public double valueAt (int row, int col) {
        return A[row][col];
    }
        
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            builder.append("[");
            for (int j = 0; j < width; j++) {
                builder.append(A[i][j]);
                if (j != width - 1) {
                    builder.append(" ");
                }
            }
            builder.append("]\n");            
        }
        return builder.toString();
    }
}
