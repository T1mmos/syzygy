package gent.timdemey.syzygy.playgr;

/**
 * Utility operations for a {@link Camera}. For each transformation, the matrix returned is the
 * matrix that applies on the axis system (the camera), not on an object in a particular axis space.
 * For example, in 2D, when translating the XY axis system by (1, 2), the matrix that graps this will hold
 * -1 and -2, because translating the axis system by (tx, ty) is equivalent to translating
 * each point with (-tx, -ty) while keeping the current axis system fixed.
 * @author Timmos
 */
public class CameraUtils {

    /**
     * Creates a camera pitch transformation matrix given the angle. The returned matrix is conform
     * with the right-hand axis convention.
     * @param angle
     * @return
     */
    public static Matrix createPitch(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        return new Matrix(4, 4,
                        cos,  sin,  0, 0,
                        -sin, cos,  0, 0,
                        0,    0,    1, 0,
                        0,    0,    0, 1);
    }

    /**
     * Creates a yaw transformation matrix given the angle. The returned matrix is conform
     * with the right-hand axis convention.
     * @param angle
     * @return
     */
    public static Matrix createYaw(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        return new Matrix(4, 4,
                        cos, 0, -sin, 0,
                        0,   1, 0,    0,
                        sin, 0, cos,  0,
                        0,   0, 0,    1);
    }

    /**
     * Creates a scale transformation matrix, using the given scale factors. Each factor
     * indicates the size of the new axis compared to the old axis.
     * @param angle
     * @return
     */
    public static Matrix createScale(double sx, double sy, double sz) {
        return new Matrix(4, 4,
                        1 / sx, 0,      0,      0,
                        0,      1 / sy, 0,      0,
                        0,      0,      1 / sz, 0,
                        0,      0,      0,      1);
    }

    public static Matrix createScale(double sx, double sy) {
        return new Matrix(3, 3,
                        1 / sx, 0,      0,
                        0,      1 / sy, 0,
                        0,      0,      1);
    }

    /**
     * Creates a scale transformation matrix, using the given scale factors. Each factor
     * indicates the size of the new axis compared to the old axis.
     * @param angle
     * @return
     */
    public static Matrix createTranslation(double tx, double ty, double tz) {
        return new Matrix(4, 4,
                        1, 0, 0, -tx,
                        0, 1, 0, -ty,
                        0, 0, 1, -tz,
                        0, 0, 0, 1);
    }

    public static Matrix createTranslation(double tx, double ty) {
        return new Matrix(3, 3,
                        1, 0, -tx,
                        0, 1, -ty,
                        0, 0, 1);
    }

    /**
     * Creates a 4x4 matrix that switches axis, e.g. z becomes x. For example, the
     * matrix that switches X and Z is given by
     *
     * <pre>
     * <code>
     * [ 0 0 1 0
     *   0 1 0 0
     *   1 0 0 0
     *   0 0 0 1 ]
     * </code>
     * </pre>
     *
     * To get this matrix, this method should be called with parameters 2, 1 and 0.
     * The high level result of multiplying with a vector is that coordinates of that vector are put in a different
     * order.
     * @param x the index of the axis that should replace x
     * @param y the index of the axis that should replace y
     * @param z the index of the axis that should replace z
     * @return
     */
    public static Matrix createAxisSwitch(int x, int y, int z) {
        Matrix m = new Matrix(4, 4);
        m.set(0, x, 1);
        m.set(1, y, 1);
        m.set(2, z, 1);
        m.set(3, 3, 1);
        return m;
    }

    /**
     * Creates an 3x4 matrix that takes away the Depth dimension of a 4x1 3D vector
     * in the WHD1 format.
     * Homogenizes the result by multiplying all coordinates with the given value (that should
     * be the reciprocal of the depth).
     * @param mult the factor in the matrix
     * @return
     */
    public static Matrix create3Dto2DMatrix(double mult) {
        Matrix flat = new Matrix(3, 4);

        flat.set(0, 0, mult);
        flat.set(1, 1, mult);
        flat.set(2, 3, 1);

        return flat;
    }

    public static Matrix createCarthMatrix() {
        Matrix carth = new Matrix(2, 3);

        carth.set(0, 0, 1);
        carth.set(1, 1, 1);

        return carth;
    }
}
