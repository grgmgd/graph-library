import java.util.LinkedList;

public class WeightedPath {
    
    private LinkedList<PathSegment> path;
    private int weight;


    public LinkedList<PathSegment> getPath() {
    	return this.path;
    }
    
    public WeightedPath() {
        this.path = new LinkedList<PathSegment>();
        this.weight = 0;
    }

    public WeightedPath(int weight) {
    	this.path = new LinkedList<PathSegment>();
    	this.weight = weight;
    }
    
    public void pushStep(PathSegment step) {
        this.path.add(step);
        if(step.getEdge() != null)
            this.weight += step.getEdge().getCost(); 
    }

    public int getWeight() {
        return this.weight;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
}