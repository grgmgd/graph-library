import java.util.HashMap;
import java.util.Vector;

public class Vertex {

    protected String _strUniqueID, _strData;
    protected int _nX, _nY;
    protected String _strLabel;
    protected HashMap<String, Edge> incidentEdges;
    
    public Vertex(String _strUniqueID, String _strData, int _nX, int _nY){
        this._strData = _strData;
        this._strUniqueID = _strUniqueID;
        this._nY = _nY;
        this._nX = _nX;
        this.incidentEdges = new HashMap<String, Edge>();
        this._strLabel = "UNEXPLORED";
    }
    
    // ZEYAD
 	public Vertex(String _strUniqueID, String _strData) {
 		this._strData = _strData;
 		this._strUniqueID = _strUniqueID;
 		this.incidentEdges = new HashMap<String, Edge>();
 		this._strLabel = "UNEXPLORED";
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
    
    public String getLabel() {
		return _strLabel;
	}

	public void setLabel(String _strLabel) {
		this._strLabel = _strLabel;
	}

}