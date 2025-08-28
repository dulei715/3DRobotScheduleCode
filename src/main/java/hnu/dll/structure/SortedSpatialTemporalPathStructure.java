package hnu.dll.structure;

import java.util.PriorityQueue;

@Deprecated
public class SortedSpatialTemporalPathStructure {
    private PriorityQueue<SpatialTemporalPath> sortedPaths = new PriorityQueue<>();

    public SortedSpatialTemporalPathStructure() {
        this.sortedPaths = new PriorityQueue<>();
    }
    public void addPath(SpatialTemporalPath path) {
        sortedPaths.add(path);
    }
    public SpatialTemporalPath getFirst() {
        return this.sortedPaths.peek();
    }
    
}
