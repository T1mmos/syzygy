package gent.timdemey.syzygy.playgr;


public class VertexManager {

    private final Vertex[] vertices;

    public VertexManager() {
        this.vertices = new Vertex[24];

        // cube (12 vertices)

        // bottom
        vertices[0] = new Vertex(0, 1);
        vertices[1] = new Vertex(0, 2);
        vertices[2] = new Vertex(1, 3);
        vertices[3] = new Vertex(2, 3);

        // top
        vertices[4] = new Vertex(4, 5);
        vertices[5] = new Vertex(4, 6);
        vertices[6] = new Vertex(5, 7);
        vertices[7] = new Vertex(6, 7);

        // verticals
        vertices[8] = new Vertex(0, 4);
        vertices[9] = new Vertex(1, 5);
        vertices[10] = new Vertex(2, 6);
        vertices[11] = new Vertex(3, 7);

        vertices[12] = new Vertex(8, 9);
        vertices[13] = new Vertex(8, 10);
        vertices[14] = new Vertex(9, 11);
        vertices[15] = new Vertex(10, 11);

        // top
        vertices[16] = new Vertex(12, 13);
        vertices[17] = new Vertex(12, 14);
        vertices[18] = new Vertex(13, 15);
        vertices[19] = new Vertex(14, 15);

        // verticals
        vertices[20] = new Vertex(8, 12);
        vertices[21] = new Vertex(9, 13);
        vertices[22] = new Vertex(10, 14);
        vertices[23] = new Vertex(11, 15);

    }

    public Vertex get(int nr) {
        return vertices[nr];
    }

    public int getCount() {
        return vertices.length;
    }
}
