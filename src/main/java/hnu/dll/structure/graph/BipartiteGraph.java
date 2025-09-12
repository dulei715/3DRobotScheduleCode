package hnu.dll.structure.graph;

import java.util.*;

public class BipartiteGraph<U, V, E> {
//    private List<U> partAList;
//    private List<V> partBList;
    private Map<U, Map<V, E>> relationTable;

//    public BipartiteGraph(List<U> partAList, List<V> partBList, E defaultValue) {
//        this.partAList = partAList;
//        this.partBList = partBList;
//        this.relationTable = new HashMap<>();
//        Map<V, E> tempMap;
//        for (U partA : partAList) {
//            tempMap = new HashMap<>();
//            for (V partB : partBList) {
//                tempMap.put(partB, defaultValue);
//            }
//            this.relationTable.put(partA, tempMap);
//        }
//    }
    public BipartiteGraph() {
//        this.partAList = new ArrayList<>();
//        this.partBList = new ArrayList<>();
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

    public List<U> getPartAList() {
        return new ArrayList<>(this.relationTable.keySet());
    }
    public List<V> getPartBList() {
        Set<V> set = new HashSet<>();
        Iterator<Map<V, E>> iterator = this.relationTable.values().iterator();
        Map<V,E> tempMap;
        while (iterator.hasNext()) {
            tempMap = iterator.next();
            set.addAll(tempMap.keySet());
        }
        return new ArrayList<>(set);
    }

    public E getWeight(U partA, V partB) {
        Map<V, E> row = this.relationTable.get(partA);
        return (row == null) ? null : row.get(partB);
    }


}
