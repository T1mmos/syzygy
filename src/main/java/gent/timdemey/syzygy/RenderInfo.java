package gent.timdemey.syzygy;

public final class RenderInfo {
    
    public final int width;
    public final int height;
    
    public final UpdateInfo updateInfo;
    
    RenderInfo (int width, int height, UpdateInfo info){
        this.width = width;
        this.height = height;
        this.updateInfo = info;
    }
}
