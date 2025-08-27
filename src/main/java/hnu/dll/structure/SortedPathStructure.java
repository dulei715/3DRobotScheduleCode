package hnu.dll.structure;

import hnu.dll.basic_entity.ThreeDLocation;
import hnu.dll.entity.Entity;
import hnu.dll.structure.basic_structure.BasicPair;

import java.util.PriorityQueue;

public class SortedPathStructure {
    private PriorityQueue<SpatialTemporalPath> sortedPaths = new PriorityQueue<>();

    public SortedPathStructure() {
        this.sortedPaths = new PriorityQueue<>();
    }
    public void addPath(SpatialTemporalPath path) {
        sortedPaths.add(path);
    }
    public SpatialTemporalPath getFirst() {
        return this.sortedPaths.peek();
    }
    
}
