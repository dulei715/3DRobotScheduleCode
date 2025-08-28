package hnu.dll.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BipartiteGraph<U, V, E> {
    private List<U> partAList;
    private List<V> partBList;
    private Map<U, Map<V, E>> relationTable;

    public BipartiteGraph(List<U> partAList, List<V> partBList, E defaultValue) {
        this.partAList = partAList;
        this.partBList = partBList;
        this.relationTable = new HashMap<>();
        Map<V, E> tempMap;
        for (U partA : partAList) {
            tempMap = new HashMap<>();
            for (V partB : partBList) {
                tempMap.put(partB, defaultValue);
            }
            this.relationTable.put(partA, tempMap);
        }
    }
    public BipartiteGraph() {
        this.partAList = new ArrayList<>();
        this.partBList = new ArrayList<>();
        this.relationTable = new HashMap<>();
    }

    public void addValue(U partA, V partB, E value) {
        Map<V, E> valueMap = this.relationTable.getOrDefault(partA, new HashMap<>());
        valueMap.put(partB, value);
        this.relationTable.put(partA, valueMap);
    }

    public void setWeight(U partA, V partB, E weight) {
        this.relationTable.get(partA).put(partB, weight);
    }

    public E getWeight(U partA, V partB) {
        return this.relationTable.get(partA).get(partB);
    }
}
