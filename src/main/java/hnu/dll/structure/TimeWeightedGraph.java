package hnu.dll.structure;

import hnu.dll.structure.basic_structure.Anchor;

import java.util.*;

public class TimeWeightedGraph {
    private Map<Anchor, Map<Anchor, Double>> graphTable;

    public TimeWeightedGraph() {
        this.graphTable = new HashMap<>();
    }

    public TimeWeightedGraph(Map<Anchor, Map<Anchor, Double>> graphTable) {
        this.graphTable = graphTable;
    }
    public void addElement(Anchor preAnchor, Anchor nextAnchor, Double weight) {
        Map<Anchor, Double> innerMap = this.graphTable.getOrDefault(preAnchor, new HashMap<>());
        innerMap.put(nextAnchor, weight);
        this.graphTable.put(preAnchor, innerMap);
    }

    public Double getWeight(Anchor preAnchor, Anchor nextAnchor) {
        Map<Anchor, Double> innerMap = this.graphTable.get(preAnchor);
        return innerMap.get(nextAnchor);
    }

    public Set<Anchor> getNodeSet() {
        return this.graphTable.keySet();
    }

    public Map<Anchor, Double> getOutgoing(Anchor anchor) {
        Map<Anchor, Double> map = this.graphTable.get(anchor);
        return (map == null) ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }

    public Set<Anchor> allNodes() {
        Set<Anchor> all = new HashSet<>(this.graphTable.keySet());
        for (Map<Anchor, Double> value : this.graphTable.values()) {
            all.addAll(value.keySet());
        }
        return all;
    }
}
