package gent.timdemey.syzygy.playgr;


public class Matrix {

    private final double[] nrs;
    private final int      ver, hor;

    public Matrix(int ver, int hor, double ... nrs) {
        if (nrs.length == 0 || nrs.length != hor * ver) {
            throw new IllegalArgumentException("Incorrect matrix size: " + hor + "x" + ver + ", got " + nrs.length);
        }
        this.ver = ver;
        this.hor = hor;
        this.nrs = nrs;
    }

    // used to create an unfilled matrix
    private Matrix(int ver, int hor) {
        this.ver = ver;
        this.hor = hor;
        this.nrs = new double[hor * ver];
    }

    public static Matrix createVector(double ... nrs) {
        return new Matrix(nrs.length, 1, nrs);
    }

    public Matrix multiply(Matrix rightof) {
        if (this.hor != rightof.ver) {
            throw new IllegalArgumentException("Cannot multiply: matrices do not align");
        }
        Matrix result = new Matrix(this.ver, rightof.hor);
        for (int i = 0; i < this.ver; i++) {
            for (int j = 0; j < rightof.hor; j++) {
                for (int k = 0; k < this.hor; k++) {
                    result.nrs[rightof.hor * i + j] += nrs[this.hor * i + k] * rightof.nrs[rightof.hor * k + j];
                }
            }
        }
        return result;
    }

    private void print() {
        for (int i = 0; i < ver; i++) {
            for (int j = 0; j < hor; j++) {
                System.out.print(nrs[i * hor + j] + ",");
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        Matrix m1 = new Matrix(2, 2, 1, -1, -1, 1);
        Matrix m2 = new Matrix(2, 2, 2, 3, 5, 1);

        Matrix result = m1.multiply(m2);
        result.print();
    }
}
