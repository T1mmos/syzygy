package gent.timdemey.syzygy.playgr;


public class ProjectionManager {

    private final Projection[] projections;

    public ProjectionManager() {
        this.projections = new Projection[8];
    }

    public void setProjection(int ref, Projection p) {
        projections[ref] = p;
    }

    public Projection get(int ref) {
        return projections[ref];
    }

    public void clear() {
        for (int i = 0; i < projections.length; i++) {
            projections[i] = null;
        }
    }
}
