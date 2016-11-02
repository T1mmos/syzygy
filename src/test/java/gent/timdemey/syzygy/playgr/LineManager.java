package gent.timdemey.syzygy.playgr;


public class LineManager {

    private final Line[] lines;
    private int          cnt;

    public LineManager() {
        this.lines = new Line[12];
    }

    public void addLine(int ref, Line line) {
        lines[ref] = line;
        cnt++;
    }

    public int getLineCount() {
        return cnt;
    }

    public Line get(int ref) {
        return lines[ref];
    }

    public void clear() {
        for (int i = 0; i < lines.length; i++) {
            lines[i] = null;
        }
        cnt = 0;
    }
}
