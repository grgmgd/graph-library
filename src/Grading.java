public class Grading {

	public static int runTestCase1() throws GraphException {
		int nMark = 0;
		Graph g = new Graph("1", "1");
		GradingVisitor gVisitor = new GradingVisitor();
		g.insertVertex("1", "1");
		g.insertVertex("2", "2");
		g.insertVertex("3", "3");
		g.insertVertex("4", "4");
		g.insertVertex("5", "5");
		g.insertEdge("1", "4", "88", "88", 5);
		g.insertEdge("1", "2", "2", "2", 2);
		g.insertEdge("2", "3", "14", "14", 14);
		g.insertEdge("2", "4", "99", "99", 3);
		g.insertEdge("2", "5", "4", "4", 4);
		g.insertEdge("4", "5", "58", "58", 58);
		g.insertEdge("3", "5", "34", "34", 34);
		g.dfs("1", gVisitor);
		if(gVisitor.getResult().equalsIgnoreCase("blah"))
			nMark += 12;
		System.out.println(gVisitor.getResult());
		return nMark;
	}
	public static void main(String[]args) throws GraphException {
		int nTotalMark = 0;
		Grading grading = new Grading();
		nTotalMark += grading.runTestCase1();
		
		System.out.println(nTotalMark);
		
	}
}
