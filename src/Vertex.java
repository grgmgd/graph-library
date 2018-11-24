import java.util.HashMap;
import java.util.Vector;

public class Vertex {

    protected String _strUniqueID, _strData;
    protected int _nX, _nY;

    protected HashMap<String, Edge> incidentEdges;
    
    public Vertex(String _strUniqueID, String _strData, int _nX, int _nY){
        this._strData = _strData;
        this._strUniqueID = _strUniqueID;
        this._nY = _nY;
        this._nX = _nX;
        this.incidentEdges = new HashMap<String, Edge>();
    }

    public void insertEdge(Edge edge) {
        this.incidentEdges.put(edge._strUniqueID, edge);
    }

    public void removeEdge(String strEdgeUniqueID) {
        this.incidentEdges.remove(strEdgeUniqueID);
    }

    public Edge getEdge(String strEdgeUniqueID) {
        return this.incidentEdges.get(strEdgeUniqueID);
    }

    public Vector<Edge> getIncidentEdges() {
        Vector<Edge> edges= new Vector<Edge>();

        for(Edge e: incidentEdges.values()) {
            edges.add(e);
        }

        return edges;
    }

    public String getUniqueID() {
        return _strUniqueID;
    }

    public String getData() {
        return _strData;
    }

    public int getX() {
        return _nX;
    }

    public int getY() {
        return _nY;
    }

}