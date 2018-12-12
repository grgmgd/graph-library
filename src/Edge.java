public class Edge {

    protected String _strUniqueID, _strData;
    protected int _nEdgeCost;
    protected String _strLabel;

    protected Vertex _vertex1;
    protected Vertex _vertex2;

    public Edge(String _strUniqueID, String _strEdgeData, int _nEdgeCost) {
        this._strUniqueID = _strUniqueID;
        this._strData = _strEdgeData;
        this._nEdgeCost = _nEdgeCost;
        this._strLabel = "UNEXPLORED";
    }

    public void connectVertex1(Vertex v) {
        this._vertex1 = v;
    }

    public void connectVertex2(Vertex v) {
        this._vertex2 = v;
    }

    public Vertex getVertex1() {
        return _vertex1;
    }

    public Vertex getVertex2() {
        return _vertex2;
    }

    public Vertex getOpposite(Vertex v) {
        if(v.getUniqueID().equals(_vertex1.getUniqueID()))
            return _vertex2;
        if(v.getUniqueID().equals(_vertex2.getUniqueID()))
            return _vertex1;
        else 
            return null;
    }

    public void disconnectVertex1() {
        this._vertex1 = null;
    }

    public void disconnectVertex2() {
        this._vertex2 = null;
    }

    public String getUniqueID() {
        return _strUniqueID;
    }

    public String getData() {
        return _strData;
    }

    public int getCost() {
        return _nEdgeCost;
    }
    
    public String getLabel() {
		return _strLabel;
	}

	public void setLabel(String _strLabel) {
		this._strLabel = _strLabel;
	}
}
