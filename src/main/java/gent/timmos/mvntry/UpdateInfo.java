package gent.timmos.mvntry;

public class UpdateInfo {
    public final long prevTime;
    public final long currTime;
    public final long diffTime;
    public final int currFPS;
    
    UpdateInfo (long prevTime, long currTime, long diffTime, int currFPS){
        this.prevTime = prevTime;
        this.currTime = currTime;
        this.diffTime = diffTime;
        this.currFPS = currFPS;
    }
}
