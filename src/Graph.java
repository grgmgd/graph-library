import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
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
//					if (!helper.isEmpty() && !flag)
//					 helper.pop();       
				} else {
					e.setLabel("BACK");
				}
			}
			if (!helper.isEmpty() && !flag) {
//				helper.pop();
			}
		}

		return vivi;

	}

//	public void _minSpanningTree(Vector<PathSegment> result) throws GraphException {
//
//		boolean removeEdge = true;
//		PathSegment p = new PathSegment();
//		boolean running = false;
//
//		int min = 2147483647; // Maximum integer value..
//		String id = "";
//
//		System.out.println(edges.values().size());
//		for (Edge e : edges.values()) {
//			if (e.getLabel() != "TAKEN" && e.getCost() <= min) {
//				min = e.getCost();
//				id = e.getUniqueID();
//				running = true;
//			}
//		}
//
//		if (running) {
//			if (this.edges.get(id).getVertex1().getLabel() != "TAKEN") {
//				this.edges.get(id).getVertex1().setLabel("TAKEN");
//				removeEdge = false;
//			System.out.println("V= " + this.edges.get(id).getVertex1().getUniqueID());
//			}
//
//			if (this.edges.get(id).getVertex2().getLabel() != "TAKEN") {
//				this.edges.get(id).getVertex2().setLabel("TAKEN");
//				removeEdge = false;
//			System.out.println("V= " + this.edges.get(id).getVertex1().getUniqueID());
//			}
//
//			if (!removeEdge) {
//				this.edges.get(id).setLabel("TAKEN");
//				p._edge = this.edges.get(id);
//				result.addElement(p);
//				System.out.println(p._edge.getCost());
//			}
//			_minSpanningTree(result);
//		}
//		
//	}

	public Vector<PathSegment> minSpanningTree() throws GraphException {
		Vector<PathSegment> result = new Vector<>();
		String[][] cycle = new String[this.vertices().size()][2];
		String[][] diffCycles = new String[100][2];
		int pos = 0;
		int num = 0;
		for (int i = 0; i < cycle.length; i++) {
			cycle[i][0] = this.vertices().get(i).getUniqueID();
			cycle[i][1] = "" + num;
		}
		for (int i = 0; i < diffCycles.length; i++) {
			diffCycles[i][0] = "";
			diffCycles[i][1] = "";
		}
//		diffCycles[pos][0] = "3";
//		diffCycles[pos][1] = "2";
//		pos++;

		for (int i = 0; i < this.edges().size(); i++) {
			int min = 2147483647; // Maximum integer value..
			String id = "";
			for (int j = 0; j < this.edges().size(); j++) {
				Edge e = this.edges().get(j);
				if (e.getCost() < min && e.getLabel() != "TAKEN") {
					min = e.getCost();
					id = e.getUniqueID();
				}
			}

			Edge e = this.edges.get(id);
			if (e.getLabel() != "TAKEN") {
				e.setLabel("TAKEN");
				String cycle1 = "";
				String cycle2 = "";
				for (int k = 0; k < cycle.length; k++) {
					if (e.getVertex1().getUniqueID() == cycle[k][0]) {
						cycle1 = cycle[k][1];
					} else if (e.getVertex2().getUniqueID() == cycle[k][0])
						cycle2 = cycle[k][1];
				}

				if (!cycle1.equals("0")) {
					if (!cycle2.equals("0")) {
						boolean pass = true;
						for (int k = 0; k < diffCycles.length; k++) {
							if ((cycle1.equals(diffCycles[k][0]) && cycle2.equals(diffCycles[k][1]))
									|| (cycle2.equals(diffCycles[k][0]) && cycle1.equals(diffCycles[k][1]))) {
								pass = false;
							}
						}
						if (!cycle1.equals(cycle2) && pass) {
							PathSegment p = new PathSegment();
							p._edge = e;
							result.add(p);
							diffCycles[pos][0] = cycle1;
							diffCycles[pos][1] = cycle2;
							pos++;
							for (int k = 0; k < diffCycles.length; k++) {
								if (diffCycles[k][0].equals(cycle1) && !diffCycles[k][1].equals(cycle2)) {
									diffCycles[pos][0] = diffCycles[k][1];
									diffCycles[pos][1] = cycle2;
									pos++;
								} else if (!diffCycles[k][0].equals(cycle1) && diffCycles[k][1].equals(cycle2)) {
									System.out.println(pos);
									diffCycles[pos][0] = diffCycles[k][0];
									diffCycles[pos][1] = cycle1;
									pos++;
								}
								
							}
//							for (int z = 0; z < pos; z++) {
//								System.out.println("diff contains: " + diffCycles[z][0] + " + " + diffCycles[z][1]);
//							}
//							System.out.println("EDGE with cycle1 != 0 and cycle2 != 0: " + e.getCost());
						}
					} else {
						for (int k = 0; k < cycle.length; k++) {
							if (e.getVertex2().getUniqueID().equals(cycle[k][0]))
								cycle[k][1] = cycle1;
						}
						PathSegment p = new PathSegment();
						p._edge = e;
						result.add(p);
//						System.out.println("EDGE with cycle1 != 0 and cycle2 = 0: " + e.getCost());
					}
				} else if (!cycle2.equals("0")) {
					for (int k = 0; k < cycle.length; k++) {
						if (e.getVertex1().getUniqueID().equals(cycle[k][0]))
							cycle[k][1] = cycle2;
					}
					PathSegment p = new PathSegment();
					p._edge = e;
					result.add(p);
//					System.out.println("EDGE with cycle1 = 0 and cycle2 != 0: " + e.getCost());
				} else {
					num++;
					for (int k = 0; k < cycle.length; k++) {
						if (e.getVertex1().getUniqueID().equals(cycle[k][0]))
							cycle[k][1] = "" + num;
						else if (e.getVertex2().getUniqueID().equals(cycle[k][0]))
							cycle[k][1] = "" + num;
					}
					PathSegment p = new PathSegment();
					p._edge = e;
					result.add(p);
//					System.out.println("EDGE with cycle1 = 0 and cycle2 = 0: " + e.getCost());
				}

			}
		}
		return result;
	}

//	public static void main(String[] args) throws GraphException {
//
//		Graph testClosePoints = new Graph("George", "1.0");
//		testClosePoints.insertVertex("0", "A", 0, 0);
//		testClosePoints.insertVertex("1", "B", 1, 2);
//		testClosePoints.insertVertex("2", "C", 2, 7);
//		testClosePoints.insertVertex("3", "D", 3, 0);
//		testClosePoints.insertVertex("4", "E", 2, 5);
//		testClosePoints.insertVertex("5", "F", 3, 5);
//		testClosePoints.insertVertex("6", "G", 4, 2);
//		testClosePoints.insertVertex("7", "H", 5, 6);
//		testClosePoints.insertVertex("8", "I", 6, 5);
//		testClosePoints.insertVertex("9", "J", 7, 7);
//
//		System.out.println(testClosePoints.closestPair()[0].getX() + "," + testClosePoints.closestPair()[0].getY()
//				+ " and " + testClosePoints.closestPair()[1].getX() + "," + testClosePoints.closestPair()[1].getY());
//
//	}

}