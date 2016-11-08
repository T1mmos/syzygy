package gent.timdemey.syzygy.engine;


public class Vertex {

    private final int p1;
    private final int p2;

    public Vertex(int pref1, int pref2) {
        this.p1 = pref1;
        this.p2 = pref2;
    }

    public int getRef1() {
        return p1;
    }

    public int getRef2() {
        return p2;
    }
}
