package gent.timdemey.syzygy.engine;

public final class VectorUtils {
	
	/**
     * Creates an vector pitch transformation matrix given the angle. The returned matrix is conform
     * with the right-hand axis convention.
     * @param angle
     * @return
     */
	public static Matrix createPitch(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        return new Matrix(4, 4,
                        cos,  -sin,  0, 0,
                        sin, cos,  0, 0,
                        0,    0,    1, 0,
                        0,    0,    0, 1);
    }
	
    /**
     * Creates an object yaw transformation matrix given the angle. The returned matrix is conform
     * with the right-hand axis convention.
     * @param angle
     * @return
     */
    public static Matrix createYaw(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        return new Matrix(4, 4,
                        cos, 0, +sin, 0,
                        0,   1, 0,    0,
                        -sin, 0, cos,  0,
                        0,   0, 0,    1);
    }
}
