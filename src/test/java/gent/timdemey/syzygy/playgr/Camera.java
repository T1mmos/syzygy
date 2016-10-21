package gent.timdemey.syzygy.playgr;

public class Camera {

    // FOV in radians
    private double fov   = 75 * Math.PI / 180;

    // camera translation, so the point where the pinhole is.
    private Matrix P     = Matrix.createVector(0, 0, 0);

    // rotation angle
    private double alpha = 0;

    // tilt angle
    private double beta  = 0;

    /** Creates a camera with default parameters. */
    public Camera() {
    }

    /**
     * Creates a camera with pinhole at P (translation). Other parameters are defaulted.
     * @param P
     * @param fov
     */
    public Camera(Matrix P) {
        this.P = P;
    }


}
