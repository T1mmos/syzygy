package gent.timdemey.syzygy.math;


public class MathUtils {

    public static final double PI_2     = 2 * Math.PI;
    public static final double PI_HALF  = Math.PI / 2;
    public static final double PI_3HALF = 3 * PI_HALF;

    /**
     * Returns an angle between 0 (incl.) and 2*PI (excl.).
     * @param angle the angle.
     * @return
     */
    public static double angle_canonical(double angle) {
        if (0.0 <= angle && angle < PI_2) {
            return angle;
        }

        double add;
        if (angle < 0) {
            add = PI_2;
        } else {
            add = -PI_2;
        }

        double corr = angle;
        while (corr < 0 || corr >= PI_2) {
            corr += add;
        }
        return corr;
    }

    /**
     * Converts an normalized angle (0 <= angle < 2*PI) to a vector with length 1.
     * @param angle angle between 0 (incl.) and 2*PI (excl.)
     * @return the vector for the given angle, size 1
     */
    public static double[] normangle2vect(double angle) {
        if (angle == PI_HALF) {
            return new double[] { 0, 1 };
        } else if (angle == PI_3HALF) {
            return new double[] { 0, -1 };
        } else {
            double tan = Math.tan(angle); // matches ||x|| = 1
            return normvect(1, tan);
        }
    }

    /**
     * Returns a normalized vector (= size 1) with given X and Y components.
     * @param x
     * @param y
     * @return
     */
    public static double[] normvect(double x, double y) {
        double size = vectsize(x, y);
        return new double[] { x / size, y / size };
    }

    /**
     * Returns sqrt(x^2 + y^2).
     * @param x
     * @param y
     * @return
     */
    public static double vectsize(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    public static void main(String[] args) {
        printvect(normangle2vect(PI_HALF));
        printvect(normangle2vect(PI_HALF / 2));

    }

    public static void printvect(double[] vect) {
        System.out.println("[" + vect[0] + "," + vect[1] + "]");
    }
}
