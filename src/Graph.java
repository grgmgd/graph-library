import java.util.HashMap;
import java.util.Vector;

public class Graph {

	String _strName;
	String _strVersion;

	HashMap<String, Vertex> vertices;
	HashMap<String, Edge> edges;

	public Graph(String _strName, String _strVersion) {
		this._strName = _strName;
		this._strVersion = _strVersion;
		this.vertices = new HashMap<String, Vertex>();
		this.edges = new HashMap<String, Edge>();
	}

	public String getLibraryName() {
		return _strName;
	}

	public String getLibraryVersion() {
		return _strVersion;
	}

	public void insertVertex(String strUniqueID, String strData, int nX, int nY) throws GraphException {
		Vertex v = new Vertex(strUniqueID, strData, nX, nY);
		this.vertices.put(v.getUniqueID(), v);
	}

	// ME
	public void insertVertex(String strUniqueID, String strData) throws GraphException {
		Vertex v = new Vertex(strUniqueID, strData);
		this.vertices.put(v.getUniqueID(), v);
	}

	public void insertEdge(String strVertex1UniqueID, String strVertex2UniqueID, String strEdgeUniqueID,
			String strEdgeData, int nEdgeCost) throws GraphException {
		Edge e = new Edge(strEdgeUniqueID, strEdgeData, nEdgeCost);
		this.edges.put(e.getUniqueID(), e);

		Vertex v1 = this.vertices.get(strVertex1UniqueID);
		Vertex v2 = this.vertices.get(strVertex2UniqueID);

		v1.insertEdge(e);
		v2.insertEdge(e);

		e.connectVertex1(v1);
		e.connectVertex2(v2);
	}

	public void removeVertex(String strVertexUniqueID) throws GraphException {

		Vertex v = this.vertices.get(strVertexUniqueID);
		Vector<Edge> edges = v.getIncidentEdges();

		for (Edge e : edges) {
			removeEdge(e._strUniqueID);
		}

		this.vertices.remove(strVertexUniqueID);
	}

	public void removeEdge(String strEdgeUniqueID) throws GraphException {
		edges.get(strEdgeUniqueID).getVertex1().removeEdge(strEdgeUniqueID);
		edges.get(strEdgeUniqueID).getVertex2().removeEdge(strEdgeUniqueID);
		edges.remove(strEdgeUniqueID);
	}

	public Vector<Edge> incidentEdges(String strVertexUniqueID) throws GraphException {
		return vertices.get(strVertexUniqueID).getIncidentEdges();
	}

	public Vector<Vertex> vertices() throws GraphException {
		Vector<Vertex> vertices = new Vector<Vertex>();

		for (Vertex v : this.vertices.values()) {
			vertices.add(v);
		}

		return vertices;
	}

	public Vector<Edge> edges() throws GraphException {
		Vector<Edge> edges = new Vector<Edge>();

		for (Edge v : this.edges.values()) {
			edges.add(v);
		}

		return edges;
	}

	public Vertex[] endVertices(String strEdgeUniqueID) throws GraphException {
		Edge e = this.edges.get(strEdgeUniqueID);

		Vertex[] vertices = new Vertex[2];
		vertices[0] = e.getVertex1();
		vertices[1] = e.getVertex2();

		return vertices;
	}

	public Vertex opposite(String strVertexUniqueID, String strEdgeUniqueID) throws GraphException {
		Vertex v = this.vertices.get(strVertexUniqueID);
		Edge e = v.getEdge(strEdgeUniqueID);

		return (e.getVertex1().getUniqueID().equals(strVertexUniqueID)) ? e.getVertex2() : e.getVertex1();
	}

	// ZEYAD
	public void dfs(String strStartVertexUniqueID, Visitor visitor) throws GraphException {
		Vertex v = this.vertices.get(strStartVertexUniqueID);
		v.setLabel("VISITED");
		visitor.visit(v);
		for (Edge e : v.getIncidentEdges()) {
			if (e.getLabel().equals("UNEXPLORED")) {
				visitor.visit(e);
				Vertex w = opposite(v.getUniqueID(), e.getUniqueID());
				if (w.getLabel().equals("UNEXPLORED")) {
					e.setLabel("DISCOVERY");
					dfs(w.getUniqueID(), visitor);
				} else {
					e.setLabel("BACK");
				}
			}
		}
	}

	public static void main(String[] args) {

	}
}