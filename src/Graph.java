import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class Graph {

	String _strName;
	String _strVersion;
	Stack helper = new Stack<PathSegment>();
	Vector vivi = new Vector<PathSegment>();
	boolean flag = false;

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

	public void bfs(String strStartVertexUniqueID, Visitor visitor) throws GraphException {
		LinkedList<Vertex> vertices = new LinkedList<Vertex>();
		Vertex vertex = this.vertices.get(strStartVertexUniqueID);
		_bfs(strStartVertexUniqueID, visitor, vertex, vertices);
		while (!vertices.isEmpty()) {
			Vertex v = vertices.removeFirst();
			if (v.getLabel().equals("UNEXPLORED")) {
				_bfs(v.getUniqueID(), visitor, v, vertices);
			}
		}
	}

	private void _bfs(String strVertexUniqueID, Visitor visitor, Vertex v, LinkedList<Vertex> vertices)
			throws GraphException {
		visitor.visit(v);
		v.setLabel("VISITED");
		for (Edge e : v.getIncidentEdges()) {
			if (e.getLabel().equals("UNEXPLORED")) {
				visitor.visit(e);
				vertices.add(this.opposite(strVertexUniqueID, e.getUniqueID()));
			}
		}
	}

	public Vertex[] closestPair() throws GraphException {
		Vertex[] vertices = this.vertices.values().toArray(new Vertex[this.vertices.size()]);
		return closePair(vertices, 0, vertices.length);
	}

	public Vertex[] closePair(Vertex[] vertices, int low, int high) {

		if (high - low <= 3) {
			return closePairBrute(vertices, low, high);
		}

		int mid = (low + high) / 2;

		Vertex[] left = closePair(vertices, low, mid);
		Vertex[] right = closePair(vertices, mid + 1, high);

		double minDistance = minDistance(left, right);

		Vertex midPoint = vertices[mid];
		Vertex[] strip = new Vertex[high - low];
		int stripIndex = 0;
		for (int i = low; i < high; i++)
			if (Math.abs(vertices[i].getX() - midPoint.getX()) < minDistance) {
				strip[stripIndex] = vertices[i];
				stripIndex++;
			}

		return compareVertices(compareVertices(left, right), stripClosest(strip, minDistance, stripIndex));
	}

	static Vertex[] stripClosest(Vertex[] strip, double minDistance, int size) {
		double min = minDistance;
		Vertex[] result = new Vertex[2];

		for (int i = 0; i < size; ++i)
			for (int j = i + 1; j < size && (strip[j].getY() - strip[i].getY()) < min; ++j) {

				double distance = (distance(strip[i]._nX, strip[i]._nY, strip[j]._nX, strip[j]._nY));

				if (distance < min) {
					min = distance;
					result[0] = strip[i];
					result[1] = strip[j];
				}
			}

		return result;
	}

	static Vertex[] closePairBrute(Vertex[] vertices, int low, int high) {
		double min = Integer.MAX_VALUE;
		Vertex[] result = new Vertex[2];

		for (int i = low; i < high; ++i) {
			for (int j = i + 1; j < high; ++j) {
				double test = distance(vertices[i]._nX, vertices[i]._nY, vertices[j]._nX, vertices[j]._nY);
				if (test < min) {
					min = test;
					result[0] = vertices[i];
					result[1] = vertices[j];
				}
			}
		}

		return result;
	}

	static double minDistance(Vertex[] v1, Vertex[] v2) {

		if (v1[0] == null && v2[0] != null) {
			return distance(v2[0]._nX, v2[0]._nY, v2[1]._nX, v2[1]._nY);
		} else if (v2[0] == null) {
			return distance(v1[0]._nX, v1[0]._nY, v1[1]._nX, v1[1]._nY);
		}

		double distance1 = distance(v1[0]._nX, v1[0]._nY, v1[1]._nX, v1[1]._nY);
		double distance2 = distance(v2[0]._nX, v2[0]._nY, v2[1]._nX, v2[1]._nY);

		return distance1 > distance2 ? distance2 : distance1;
	}

	static Vertex[] compareVertices(Vertex[] v1, Vertex[] v2) {

		if (v1[0] == null && v2[0] != null) {
			return v2;
		} else if (v2[0] == null) {
			return v1;
		}

		double distance1 = distance(v1[0]._nX, v1[0]._nY, v1[1]._nX, v1[1]._nY);
		double distance2 = distance(v2[0]._nX, v2[0]._nY, v2[1]._nX, v2[1]._nY);

		return distance1 > distance2 ? v2 : v1;
	}

	static double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
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

	public Vector<PathSegment> pathDFS(String strStartVertexUniqueID, String strEndVertexUniqueID)
			throws GraphException {

		Vertex v = this.vertices.get(strStartVertexUniqueID);
		v.setLabel("VISTED");
		helper.push(v);

		if (strStartVertexUniqueID == strEndVertexUniqueID) {
			flag = true;
			while (!(helper.isEmpty())) {
				if (helper.peek() instanceof Vertex) {
					System.out.println(((Vertex) (helper.peek()))._strUniqueID + " vertex ");
				} else
					System.out.println(((Edge) (helper.peek())).getCost() + "  edge ");
				vivi.add(helper.pop());
			}

		}

		for (Edge e : v.getIncidentEdges()) {
			if (e.getLabel() == "UNEXPLORED") {
				Vertex w = opposite(v.getUniqueID(), e.getUniqueID());
				if (w.getLabel() == "UNEXPLORED") {
					e.setLabel("DISCOVERY");
					helper.push(e);
					pathDFS(w.getUniqueID(), strEndVertexUniqueID);
					// if (!helper.isEmpty() && !flag)
					// helper.pop();
				} else {
					e.setLabel("BACK");
				}
			}
			if (!helper.isEmpty() && !flag) {
				// helper.pop();
			}
		}

		return vivi;
	}

	public Vector<Vector<PathSegment>> findAllShortestPathsFW() throws GraphException, CloneNotSupportedException {
		Vertex[] vertices = this.vertices.values().toArray(new Vertex[this.vertices.size()]);

		WeightedPath[][] matrix = new WeightedPath[vertices.length][vertices.length];

		Vector<Vector<PathSegment>> resultMatrix = new Vector<Vector<PathSegment>>();

		//add the direct connections and vertices zeros first!
		for(int i = 0; i < vertices.length; i++) {

			WeightedPath step = new WeightedPath();
			step.pushStep(new PathSegment(vertices[i], null));
			matrix[i][i] = step;

			for(int j = 0; j < vertices.length; j++) {
				Vector<Edge> edges = vertices[i].getIncidentEdges();
				for(int k = 0; k < edges.size(); k++) {
					if(edges.get(k).getOpposite(vertices[i]).getUniqueID().equals(vertices[j].getUniqueID())) {
						WeightedPath path = new WeightedPath();
						PathSegment s1 = new PathSegment(vertices[i], edges.get(k));
						PathSegment s2 = new PathSegment(vertices[j], null);

						path.pushStep(s1);
						path.pushStep(s2);

						matrix[i][j] = path;
					}
				}
			}
		}
		
		for(int k = 0; k < vertices.length; k++) {
			for (int i = 0; i < vertices.length; i++) {
				for (int j = 0; j < vertices.length; j++) {

					if(matrix[i][k] != null && matrix[k][j] != null) {

						if(matrix[i][j] == null) {
							WeightedPath p1 = (WeightedPath) matrix[i][k];
							WeightedPath p2 = (WeightedPath) matrix[k][j];

							WeightedPath path = new WeightedPath(p1.getPath(), p2.getPath());

							matrix[i][j] = path;
						} else {
						if (matrix[i][j] != null
									&& matrix[i][k].getWeight() + matrix[k][j].getWeight() < matrix[i][j].getWeight()) {
								WeightedPath p1 = (WeightedPath) matrix[i][k];
								WeightedPath p2 = (WeightedPath) matrix[k][j];

								WeightedPath path = new WeightedPath(p1.getPath(), p2.getPath());

								matrix[i][j] = path;
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) { 
				resultMatrix.add(matrix[i][j].getPath());
			}
		}

		return resultMatrix;
	}

	public Vector<Vector<PathSegment>> findShortestPathBF(String strStartVertexUniqueID) throws GraphException {
		int verticesCount = this.vertices.size();
		Vertex src = this.vertices.get(strStartVertexUniqueID);
		Hashtable<String, WeightedPath> segmentTable = new Hashtable<String, WeightedPath>();
		for (Vertex x : this.vertices.values()) {
			segmentTable.put(x.getUniqueID(), new WeightedPath(Integer.MAX_VALUE));
		}
		segmentTable.remove(strStartVertexUniqueID);
		segmentTable.put(strStartVertexUniqueID, new WeightedPath());

		LinkedList<Vertex> vertices = new LinkedList<Vertex>();
		for (Edge e : src.getIncidentEdges()) {
			vertices.add(this.opposite(strStartVertexUniqueID, e.getUniqueID()));
		}
		int weight = 0;
		for (Vertex vt : this.vertices.values()) {
			for (Edge e : vt.getIncidentEdges()) {
				Vertex u = vt;
				Vertex v = opposite(u.getUniqueID(), e.getUniqueID());
				int weightTemp = e.getCost();
				if (segmentTable.get(u.getUniqueID()).getWeight() != Integer.MAX_VALUE
						&& segmentTable.get(u.getUniqueID()).getWeight() + weightTemp < segmentTable
								.get(v.getUniqueID()).getWeight()) {
					segmentTable.replace(v.getUniqueID(), new WeightedPath(segmentTable.get(u.getUniqueID()).getPath(),
							segmentTable.get(v.getUniqueID()).getPath()));
					segmentTable.get(v.getUniqueID()).addStep(new PathSegment(u, e));
					segmentTable.get(v.getUniqueID())
							.setWeight(segmentTable.get(u.getUniqueID()).getWeight() + weightTemp);
				}
			}
		}
		Set<String> keys = segmentTable.keySet();
		Vector<Vector<PathSegment>> shortestPath = new Vector<Vector<PathSegment>>();
		for (String key : keys) {
			shortestPath.add(segmentTable.get(key).getPath());
		}
		return shortestPath;
	}

    public static void main(String[] args) throws GraphException, CloneNotSupportedException {

        Graph g = new Graph("George", "1.0");
		g.insertVertex("1", "1");
		g.insertVertex("2", "2");
		g.insertVertex("3", "3");
		g.insertVertex("4", "4");
		g.insertVertex("5", "5");

		g.insertEdge("1", "4", "a", "a", 5);
		g.insertEdge("1", "2", "b", "b", 2);
		g.insertEdge("2", "3", "c", "c", 14);
		g.insertEdge("2", "4", "d", "d", 5);
		g.insertEdge("2", "5", "e", "e", 4);
		g.insertEdge("4", "5", "f", "f", 58);
		g.insertEdge("3", "5", "g", "g", 34);

		Vector<Vector<PathSegment>> paths = g.findAllShortestPathsFW();
    }
}