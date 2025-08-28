package hnu.dll.structure;

import hnu.dll.basic_entity.ThreeDLocation;
import hnu.dll.entity.Entity;
import hnu.dll.structure.basic_structure.BasicPair;

import java.util.PriorityQueue;

public class SortedPathStructure {
    private PriorityQueue<Path> sortedPaths = new PriorityQueue<>();

    public SortedPathStructure() {
        this.sortedPaths = new PriorityQueue<>();
    }
    public void addPath(Path path) {
        sortedPaths.add(path);
    }
    public Path getFirst() {
        return this.sortedPaths.peek();
    }

    public PriorityQueue<Path> getSortedPaths() {
        return sortedPaths;
    }
}
