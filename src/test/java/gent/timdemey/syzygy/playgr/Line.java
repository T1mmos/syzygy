package gent.timdemey.syzygy.playgr;


public class Line {

    public final int projnr1, projnr2;

    public Line (int projnr1, int projnr2) {
        this.projnr1 = projnr1;
        this.projnr2 = projnr2;
    }

    public int getRef1() {
        return projnr1;
    }

    public int getRef2() {
        return projnr2;
    }
}
