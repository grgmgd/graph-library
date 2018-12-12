import java.util.Vector;

public class WeightedPath{
    
    private Vector<PathSegment> path;
    private int weight;

    public WeightedPath(Vector<PathSegment> p1, Vector<PathSegment> p2) {
        this.path = new Vector<PathSegment>();
        this.weight = 0;
        
        for(int i = 0; i < p1.size() - 1; i++) {
            this.pushStep(p1.get(i));
        }

        for(int i = 0; i < p2.size(); i++) {
            this.pushStep(p2.get(i));
        }
    }

    public WeightedPath() {
        this.path = new Vector<PathSegment>();
        this.weight = 0;
    }

    public void pushStep(PathSegment step) {
        this.path.add(step);

        if(step.getEdge() != null)
            this.weight += step.getEdge().getCost(); 
    }

    public Vector<PathSegment> getPath() {
        return this.path;
    }

    public int getWeight() {
        return this.weight;
    }

    public void printPath() {

        System.out.println();
        System.out.print(this.weight + ":  ");

        for(PathSegment p : this.path) {
            System.out.print(p.getVertex().getUniqueID());
            if(p.getEdge() != null) {
                System.out.print(" --" + p.getEdge()._strUniqueID + "--> ");
            }
        }
    }
}