package gent.timdemey.syzygy.engine;

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
 * The camera transforms 3D coordinates by applying transformations in a particular order known as "TYPSY":
 * <ul>
 * <li>T: translation (actual camera location)
 * <li>Y: rotation around Y axis a.k.a. yaw
 * <li>P: rotation around Z axis a.k.a. pitch
 * <li>SY: switches X and Z axis, so exchanges X and Z coordinates in order to get to the WHD format: width, height,
 * depth.
 * </ul>
 * The third 3D rotation, "roll", is not supported. From the transformed point, additional 2D transformations are
 * executed known as "RSOC":
 * <ul>
 * <li>R: the point is homogenized by multiplying all coordinates with the reciprocal of the depth, and the 3rd
 * dimension is stripped off. The resulting 2D point is now in the homogenized format (px, py, 1).
 * <li>S: scales the coordinate to screen resolution and reverses the Y axis (as 2D graphics mostly use a downwards Y).
 * <li>O: translates the axis system to the upper left corner of the screen (as 2D graphics mostly have their origin
 * there).
 * <li>C: makes a Carthesian coordinate from the homogeneous coordinate. The coordinates are now "screen ready".
 * </ul>
 * @author Timmos
 */
public final class Camera {

    private static final double RAD_360   = Math.toRadians(360);
    private static final double RAD_90    = Math.toRadians(90);
    private static final double RAD_60    = Math.toRadians(60);

    private static final double MAX_PITCH = RAD_90;
    private static final double FOV_MIN   = RAD_60;
    private static final double FOV_MAX   = RAD_90;

    // unit vector X
    private static final Matrix UX        = new Matrix(4, 1, 1, 0, 0, 1);
    // unit vector Y
    private static final Matrix UY        = new Matrix(4, 1, 0, 1, 0, 1);
    // unit vector Z
    private static final Matrix UZ        = new Matrix(4, 1, 0, 0, 1, 1);

    // unit vector X after yaw and pitch
    private Matrix              RX        = null;
    // unit vector Y after yaw and pitch
    private Matrix              RY        = null;
    // unit vector Z after yaw and pitch
    private Matrix              RZ        = null;

    // FOV in radians
    private double              fov       = RAD_60;

    // camera pinhole location
    private Matrix              L         = Matrix.createVector(0, 0, 0);

    // yaw and pitch angles, in radians
    private double              yaw       = 0;
    private double              pitch     = 0;

    // resolution
    private int                 resx      = 300, resy = 200;

    // derived matrices
    // 3D: "TYPSY"
    // translation 3D
    private Matrix              T         = null;
    // rotation 3D around y axis a.k.a. "yaw"
    private Matrix              Y         = null;
    // rotation 3D around Z axis a.k.a. "pitch"
    private Matrix              P         = null;
    // Switches axes. X-D, Y-H, Z-W, so DHW, but we must have WHD, so switch axis 0 and 2
    private static final Matrix SY        = CameraUtils.createAxisSwitch(2, 1, 0);
    // combined 3D transformation
    private Matrix              M3D       = null;

    // 2D: "SOC"
    // rescales to resolution / screen coordinates
    private Matrix              S         = null;
    // translation to move the origin to the upper left of the projection plane
    private Matrix              O         = null;
    // removes the 3rd coordinate from a 2D point: from 2D homogeneous to 2D carthesian
    private static final Matrix C         = CameraUtils.createCarthMatrix();
    // combined 2D transformation
    private Matrix              M2D       = null;

    /** Creates a camera with default parameters. */
    public Camera() {
        calc();
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
        Y = CameraUtils.createYaw(yaw);
        P = CameraUtils.createPitch(pitch);

        double px = L.get(0, 0);
        double py = L.get(1, 0);
        double pz = L.get(2, 0);
        T = CameraUtils.createTranslation(px, py, pz);

        M3D = SY.multiply(P).multiply(Y).multiply(T);

        // we take ||u|| = 1, then half the camera plane width in WHD equals tan(fov / 2) in XYZ
        double tanfov = Math.tan(fov / 2);
        double scale = 2 * tanfov / resx;

        // pixels are square, and XYZ has 1-1-1 ratio, but 2D graphics have downwards Y.
        S = CameraUtils.createScale(scale, -scale);
        O = CameraUtils.createTranslation(-resx / 2, -resy / 2);
        // S2D = CameraUtils.createScale(1, -1);
        M2D = C.multiply(O).multiply(S); // .multiply(S2D)

        // now express in XYZ space the unit vectors after rotations
        Matrix camY = VectorUtils.createYaw(yaw);
        Matrix camP = VectorUtils.createPitch(pitch);
        Matrix comb = camY.multiply(camP);        
        RX = comb.multiply(UX);
        RY = comb.multiply(UY);
        RZ = comb.multiply(UZ);
    }

    /**
     * Sets the camera location (translation).
     * @param P camera location vector matrix
     */
    public void setPinhole(Matrix P){
        this.L = P;
    }

    /**
     * Sets the field of view.
     * @param fov the field of view, in radians
     */
    public void setFov(double fov){
        if (!(FOV_MIN <= fov && fov <= FOV_MAX)){
            return;
        }
        this.fov = fov;
    }

    /**
     * Sets the angle of horizontal rotation a.k.a. "yaw" (looking sideways).
     * @param angle the yaw angle
     */
    public void setYaw(double angle) {
        this.yaw = angle;
        if (yaw >= RAD_360) {
            yaw -= RAD_360;
        }
        if (yaw < 0) {
            yaw += RAD_360;
        }
    }

    /**
     * Adds the given angle to the current yaw angle.
     * @param rot the angle to add to the current yaw angle
     */
    public void addYaw(double rot) {
        setYaw(yaw + rot);
    }

    /**
     * Sets the angle of vertical rotation a.k.a. "pitch" (looking up/down).
     * @param angle the pitch angle
     */
    public void setPitch(double angle) {
        this.pitch = angle;
        if (this.pitch > MAX_PITCH) {
            this.pitch = MAX_PITCH;
        }
    }

    /**
     * Adds the given angle to the current pitch angle.
     * @param angle the angle to add to the current pitch angle
     */
    public void addPitch(double angle) {
        setPitch(this.pitch + angle);
    }

    /**
     * Gets the field of view angle.
     * @return the field of view, in radians
     */
    public double getFov (){
        return fov;
    }

    /**
     * Gets the coordinates in XYZ space of the pinhole.
     * @return the pinhole location vector, in XYZ space
     */
    public Matrix getPinhole() {
        return L;
    }

    /**
     * Adds, in XYZ space, the given offsets to the current pinhole location.
     * @param x X offset
     * @param y Y offset
     * @param z Z offset
     */
    public void addPinhole(double x, double y, double z) {
        addPinhole(0, x);
        addPinhole(1, y);
        addPinhole(2, z);
    }

    private void addPinhole(int idx, double val) {
        L.set(idx, 0, L.get(idx, 0) + val);
    }

    /**
     * Gets the yaw angle in XYZ space. According to the right-hand axis
     * convention, a positive yaw means that the camera rotated to the left.
     * @return the yaw angle
     */
    public double getYaw (){
        return yaw;
    }

    /**
     * Gets the pitch angle in XYZ space. According to the right-hand axis
     * convention, a (strictly) positive pitch means that the camera is looking up.
     * @return the pitch angle
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * Gets the Z axis unit vector after applying yaw and pitch in XYZ space. This is useful to step sideways as this
     * is the horizontal direction vector perpendicular to the view direction.
     * @return
     */
    public Matrix getW() {
        return RZ;
    }

    /**
     * Gets the X axis unit vector after applying yaw and pitch in XYZ space. This is useful to step forwards /
     * backwards as this is the view direction.
     * @return
     */
    public Matrix getD() {
        return RX;
    }

    /**
     * Sets this camera's resolution.
     * @param resx horizontal resolution
     * @param resy vertical resolution
     */
    public void setResolution(int resx, int resy) {
        this.resx = resx;
        this.resy = resy;
    }

    /**
     * Converts the given 3D point in XYZ space into a 2D point in screen space.
     * @param point the 3D point in XYZ space
     * @return the 2D point in screen space
     */
    public Matrix snap(Matrix point) {
        M3D.multiply(point);
        Matrix proj = M3D.multiply(point);

        double reciprocal = 1 / (proj.get(2, 0) + 0);
        Matrix F1 = CameraUtils.create3Dto2DMatrix(reciprocal);
        return M2D.multiply(F1).multiply(proj);
    }
}
