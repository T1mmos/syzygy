package gent.timmos.mvntry;

public class UpdateInfo {
    public final long prevTime;
    public final long currTime;
    public final long diffTime;
    public final int currFPS;
    public final long passedTime;
    
    UpdateInfo (long prevTime, long currTime, long diffTime, long passedTime, int currFPS){
        this.prevTime = prevTime;
        this.currTime = currTime;
        this.diffTime = diffTime;
        this.passedTime = passedTime;
        this.currFPS = currFPS;
    }
}
