package gent.timmos.mvntry.math3d;

public final class MatrixOps {
    
    private MatrixOps () {
        
    }
    
    public static double[][] invert (double[][] input) {
        if (input.length != input[0].length) {
            throw new IllegalArgumentException("Only a square matrix is allowed for inversion");
        }
        
        int dim = input.length;
        double[][] nrs = new double [dim][2*dim];
        
        // prepare augmented matrix [A|I]
        for (int row = 0; row < dim; row++) {
            for (int col = 0; col < dim; col++) {
                nrs[row][col] = input[row][col];
            }
            nrs[row][dim + row] = 1;
        }
        
        for (int pivot = 0; pivot < dim; pivot++) {
            for (int row = 0; row < dim; row++) {
                if (row == pivot) { // row of pivot is unchanged
                    continue;
                }
                double pivotVal = nrs[pivot][pivot];
               
                if (pivotVal == 0) {
                    int rowexch = pivot + 1;
                    while (nrs[rowexch][pivot] == 0 && rowexch++ < dim);
                    if (rowexch == dim) {
                        throw new IllegalArgumentException("Matrix not invertible");
                    }
                    // ok we found nonzero entry on pivot column, so exchange row "pivot" with "rowexch"
                    double[] tmp = nrs[rowexch];
                    nrs[rowexch] = nrs[pivot];
                    nrs[pivot] = tmp;
                    pivotVal = nrs[pivot][pivot]; // and continue as if nothing happened
                }
                double rowmult = nrs[row][pivot];
                for (int col = 0; col < 2*dim; col++) {
                    if (col == pivot) {
                        nrs[row][col] = 0; // all numbers above/below pivot are set to 0
                    } else {
                        double cross1 = nrs[pivot][pivot] * nrs[row][col];
                        double cross2 = nrs[pivot][col] * rowmult;
                        nrs[row][col] = cross1 - cross2;
                    }
                }
            }
        }
        
        double[][] splitted = new double[dim][dim];
        for (int row = 0; row < dim; row++) {
            double pivot = nrs[row][row];
            
                for (int col = 0; col < dim; col++) {
                    splitted[row][col] = nrs[row][dim+col] / pivot;
                }
                        
        }
        return splitted;
    }
    
    public static double[][] multiply (double[][] m1, double[][] m2) {
        double[][] out = new double[m1.length][m2[0].length];
        
        for (int row = 0; row < m1.length; row++) {
            for (int col = 0; col < m2[0].length; col++) {
                double sum = 0.0;
                for (int i = 0; i < m1[0].length; i++) {
                    sum += m1[row][i] * m2[i][col];
                }
                out[row][col] = sum;
            }
        }
        return out;
    }
    
    public static double[][] multiply (double[][] m1, double m2) {
        double[][] out = new double[m1.length][m1[0].length];
        
        for (int row = 0; row < m1.length; row++) {
            for (int col = 0; col < m1[0].length; col++) {
                out[row][col] = m1[row][col]  * m2;
            }
        }
        return out;
    }
    
    public static double[][] add (double[][] m1, double[][] m2) {
        double[][] out = new double[m1.length][m1[0].length];
        
        for (int row = 0; row < m1.length; row++) {
            for (int col = 0; col < m1[0].length; col++) {
                out[row][col] = m1[row][col] + m2[row][col];
            }
        }
        return out;
    }
}
