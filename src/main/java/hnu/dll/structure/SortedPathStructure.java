package hnu.dll.structure;

import hnu.dll.structure.path.Path;

import java.util.PriorityQueue;

public class SortedPathStructure<T extends Path> {
    private PriorityQueue<T> sortedPaths = new PriorityQueue<>();

    public SortedPathStructure() {
        this.sortedPaths = new PriorityQueue<>();
    }
    public void addPath(T path) {
        sortedPaths.add(path);
    }
    public T getRemoveAndGetFirst() {
//        return this.sortedPaths.peek();
        T first = this.sortedPaths.peek();
        this.sortedPaths.remove(first);
        return first;
    }

    public T getFirst() {
        return this.sortedPaths.peek();
    }


    public PriorityQueue<T> getSortedPaths() {
        return sortedPaths;
    }

    @Override
    public String toString() {
        return "{" +
                sortedPaths.peek()
                +'}';
    }
}
