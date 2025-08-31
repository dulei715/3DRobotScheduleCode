package hnu.dll.structure;

import java.util.PriorityQueue;

public class SortedPathStructure {
    private PriorityQueue<AnchorPointPath> sortedPaths = new PriorityQueue<>();

    public SortedPathStructure() {
        this.sortedPaths = new PriorityQueue<>();
    }
    public void addPath(AnchorPointPath path) {
        sortedPaths.add(path);
    }
    public AnchorPointPath getFirst() {
        return this.sortedPaths.peek();
    }

    public PriorityQueue<AnchorPointPath> getSortedPaths() {
        return sortedPaths;
    }
}
