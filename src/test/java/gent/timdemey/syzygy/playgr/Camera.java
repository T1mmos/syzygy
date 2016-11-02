package gent.timdemey.syzygy.playgr;

/**
 * A camera, converting points in XYZ space into WHD space (Width-Height-Depth). The
 * camera assumes a right-hand axis convention where Y is pointing upwards.
 * <p>
 * The camera is parameterized by:
 * <ul>
 * <li>its location in XYZ space (pinhole): influences the translation transformation;
 * <li>field of view and resolution: influences the length of the H and W axis;
 * <li>pitch: influences the rotation around the Z axis (looking up/down);
 * <li>yaw: influences the rotation around the Y axis (looking left/right);
 * </ul>
 * The camera transforms coordinates by applying transformations in this order:
 * <ul>
 * </li>translation (actual camera location) </li>scaling (Y and Z are rescaled to express in resolution units)
 * </li>rotation (looking up / down, a.k.a. pitch) </li>rotation (looking left / right, a.k.a. yaw)
 * </ul>
 * @author Timmos
 */
public class Camera {

    private static final double FOV_MIN = Math.PI / 3;
    private static final double FOV_MAX = Math.PI / 2;

    // Switches axes. X-D, Y-H, Z-W, so DHW, but we must have WHD, so switch axis 0 and 2
    private static final Matrix V       = CameraUtils.createAxisSwitch(2, 1, 0);

    // removes the 3rd coordinate: from 2D homogeneous to 2D carthesian space
    private static Matrix       C       = CameraUtils.createCarthMatrix();

    // FOV in radians
    private double              fov     = Math.toRadians(75);

    // camera translation, so the point where the pinhole is.
    private Matrix              P       = Matrix.createVector(0, 0, 0);

    // yaw and pitch angles, in radians
    private double              yaw     = 0;
    private double              pitch   = 0;

    // resolution
    private int                 resx    = 300, resy = 200;

    // derived properties
    // translation 3D
    private Matrix              T       = null;
    // scaling 3D
    private Matrix              S       = null;
    // rotation 3D around Z axis a.k.a. "pitch"
    private Matrix              Rp      = null;
    // rotation 3D around y axis a.k.a. "yaw"
    private Matrix              Ry      = null;
    // combined 3D transformation
    private Matrix              M       = null;


    // translation 2D to move the origin to the upper left of the projection plane
    private Matrix              T2D     = null;
    // scale to flip the Height axis (Y on screen), so it points downwards
    private Matrix              S2D     = null;
    // combined 2D transformation
    private Matrix              M2D     = null;



    /** Creates a camera with default parameters. */
    public Camera() {
        calc();
    }

    /**
     * Creates a camera with pinhole at P (translation). Other parameters are defaulted.
     * @param P
     * @param fov
     */
    public Camera(Matrix P) {
        this.P = P;
    }

    /**
     * Recalculates the camera system. This method should be called after calling one or more setter methods to update
     * internal rotation matrixes and other stuff.
     */
    public void recalc(){
        calc();
    }

    // calculate derived vectors: s, u, v
    private void calc (){
        Ry = CameraUtils.createYaw(yaw);
        Rp = CameraUtils.createPitch(pitch);

        // we take ||u|| = 1, then half the camera plane width in WHD equals tan(fov / 2) in XYZ
        double tanfov = Math.tan(fov / 2);
        double sx = 2 * tanfov / resx;
        // double sy = 2 * tanfov / resy;
        S = CameraUtils.createScale(sx, sx);

        double px = P.get(0, 0);
        double py = P.get(1, 0);
        double pz = P.get(2, 0);
        T = CameraUtils.createTranslation(px, py, pz);

        M = V.multiply(Ry).multiply(Rp).multiply(T);

        T2D = CameraUtils.createTranslation(-resx / 2, +resy / 2);
        S2D = CameraUtils.createScale(1, -1);
        M2D = C.multiply(S2D).multiply(T2D).multiply(S);
    }

    public void setPinhole(Matrix P){
        this.P = P;
    }

    public void setFov(double fov){
        if (!(FOV_MIN <= fov && fov <= FOV_MAX)){
            throw new IllegalArgumentException("FOV not allowed: " + fov);
        }
        this.fov = fov;
    }

    public void setYaw(double rot) {
        this.yaw = rot;
    }

    public void addYaw(double rot) {
        this.yaw += rot;
    }

    public void setPitch(double tilt) {
        this.pitch = tilt;
    }

    public void addPitch(double tilt) {
        this.pitch += tilt;
    }

    // getters
    public double getFov (){
        return fov;
    }

    /**
     * Gets the coordinates in XYZ space of the pinhole of this camera.
     * @return
     */
    public Matrix getPinhole() {
        return P;
    }

    /**
     * Gets the yaw, or the rotation around the Y axis of this camera. According to the right-hand axis
     * convention, a positive yaw means that the camera rotated to the left.
     * @return
     */
    public double getYaw (){
        return yaw;
    }

    public void setResolution(int resx, int resy) {
        this.resx = resx;
        this.resy = resy;
    }

    public Matrix snap(Matrix point) {
        V.multiply(Ry.multiply(Rp.multiply(T.multiply(point))));
        Matrix proj = M.multiply(point);

        double reciprocal = 1 / (proj.get(2, 0) + 0);
        Matrix F1 = CameraUtils.create3Dto2DMatrix(reciprocal);
        return C.multiply(S2D.multiply(T2D.multiply(S.multiply(F1.multiply(proj)))));
    }

    /**
     * Gets the pitch, or the rotation around the Z axis of this camera. According to the right-hand axis
     * convention, a (strictly) positive pitch means that the camera is looking up.
     * @return
     */
    public double getPitch() {
        return pitch;
    }

    public static void main(String[] args) {
        Camera cam = new Camera();
        cam.setYaw(Math.toRadians(45));
        cam.setPitch(Math.toRadians(30));
        cam.calc();
        Matrix point = new Matrix(4, 1, 2, 0, 0, 1);

        Matrix result = cam.snap(point);

        System.out.println(result.toStringInt());
    }
}
