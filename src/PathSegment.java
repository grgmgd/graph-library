public class PathSegment {
    protected Vertex _vertex;
    protected Edge _edge;


    public PathSegment() {
        
    }

    public PathSegment(Vertex v, Edge e) {
        this._vertex = v;
        this._edge = e;
    }

    public void setEdge(Edge e) {
        this._edge = e;
    }

    public Vertex getVertex() {
        return _vertex;
    }

    public Edge getEdge() {
        return _edge;
    }
}