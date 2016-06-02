package gent.timmos.mvntry.math3d;


public class MatrixUtils {
    public static void print (double[][] matrix) {
        int height = matrix.length;
        int width = matrix[0].length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            builder.append("[");
            for (int j = 0; j < width; j++) {
                builder.append(matrix[i][j]);
                if (j != width - 1) {
                    builder.append(" ");
                }
            }
            builder.append("]\n");            
        }
    }
}
