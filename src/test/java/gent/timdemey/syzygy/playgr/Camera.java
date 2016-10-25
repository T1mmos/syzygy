package gent.timdemey.syzygy.playgr;

public class Camera {
    
    private static final double FOV_MIN = Math.PI / 3;
    private static final double FOV_MAX = Math.PI / 2;
    
    // FOV in radians
    private double fov   = Math.toRadians(75);

    // camera translation, so the point where the pinhole is.
    private Matrix P     = Matrix.createVector(0, 0, 0);

    // rotation angle
    private double alpha = 0;

    // tilt angle
    private double beta  = 0;
    
    // resolution
    private int resx = 300, resy = 200;
    
    // derived properties
    // rotation
    private Matrix Ry = null;
    // tilt
    private Matrix Rz = null;
    // combined rotation
    private Matrix R = null;
    // inverse rotation - for expressing xyz coordinates in uvs
    private Matrix Rr = null;
    // s normalized
    private static final Matrix Sn = Matrix.createVector(1,0,0);
    // u normalized
    private Matrix Un = null;
    // v normalized
    private Matrix Vn = null;
    // s
    private Matrix S = null;
    // u
    private Matrix U = null;
    // v
    private Matrix V = null;
    
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
        Ry = Matrix.createRotationY(alpha);
        Rz = Matrix.createRotationZ(beta);
        R = Ry.multiply(Rz);
        Rr = Rz.transpose().multiply(Ry.transpose());
        
        double ulen = Math.tan(fov / 2);
        double vlen = ulen * resy / resx;
        
        Un = Matrix.createVector(0,0,ulen);
        Vn = Matrix.createVector(0,vlen,0);
        
        S = R.multiply(Sn);
        U = R.multiply(Un);
        V = R.multiply(Vn);
        
        // P + S + U + V is the "top left" of the projection plane screen area. S, U and V are perpendicular and P is pinhole location (= translation of camera). 
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
    
    public void setRotation(double rot){
        this.alpha = rot;
    }
    
    public void setTilt (double tilt){
        this.beta = tilt;
    }
    
    // getters
    public double getFov (){
        return fov;
    }
    
    public Matrix getPinhole(){
        return P;
    }
    
    public double getRotation (){
        return alpha;
    }
    
    public double getTilt (){
        return beta;
    }
    
    public static void main(String[] args) {
        Camera c = new Camera();
        c.setFov(Math.PI / 2); //
        c.setRotation(Math.PI / 4);
        c.setTilt(Math.PI / 4);
        c.recalc();
        Matrix test = Matrix.createVector(1,0,0);
        Matrix uvs = c.Rr.multiply(test);
        System.out.println("asdf");
    }
}
