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

    /**
     * Creates a zero matrix.
     * @param ver
     * @param hor
     */
    public Matrix(int ver, int hor) {
        this.ver = ver;
        this.hor = hor;
        this.nrs = new double[hor * ver];
    }

    public double get(int ver, int hor) {
        return nrs[ver * this.hor + hor];
    }

    public int getHeight() {
        return ver;
    }

    public int getWidth() {
        return hor;
    }

    public void set(int ver, int hor, double value) {
        nrs[ver * this.hor + hor] = value;
    }

    public static Matrix createVector(double ... nrs) {
        return new Matrix(nrs.length, 1, nrs);
    }
    
    public Matrix transpose (){
        Matrix m = new Matrix(hor, ver);

        for (int i = 0; i < ver; i++){
            for (int j = 0; j < hor; j++){
                m.nrs[j * hor + i] = nrs[i * ver + j];
            }
        }
        return m;
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

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < ver; i++) {
            b.append("[");
            for (int j = 0; j < hor; j++) {
                double nr = nrs[i * hor + j];
                String format = nr > 0 ? " %-5.2f" : "%-5.2f";
                String nice = String.format(format, nr);
                b.append(nice + (j < hor - 1 ? "," : ""));
            }
            b.append("]\n");
        }
        return b.toString();
    }
    
    public String toStringInt() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < ver; i++) {
            b.append("[");
            for (int j = 0; j < hor; j++) {
                String nice = String.format("%-5d", (int) nrs[i * hor + j]);
                b.append(nice + (j < hor - 1 ? "," : ""));
            }
            b.append("]\n");
        }
        return b.toString();
    }
}
