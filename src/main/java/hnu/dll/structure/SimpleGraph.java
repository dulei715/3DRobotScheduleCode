package hnu.dll.structure;

import hnu.dll.entity.Entity;

import java.util.*;

/**
 * 标识节点之间的行走路径长度
 * 如果是电梯，只考虑水平距离
 */
public class SimpleGraph {
    private Map<Entity, Map<Entity, Double>> graphTable;

    public SimpleGraph() {
        this.graphTable = new HashMap<>();
    }

    public SimpleGraph(Map<Entity, Map<Entity, Double>> graphTable) {
        this.graphTable = graphTable;
    }
    public void addElement(Entity preEntity, Entity nextEntity, Double weight) {
        Map<Entity, Double> innerMap = this.graphTable.getOrDefault(preEntity, new HashMap<>());
        innerMap.put(nextEntity, weight);
        this.graphTable.put(preEntity, innerMap);
    }

    public Double getWeight(Entity preEntity, Entity nextEntity) {
        Map<Entity, Double> innerMap = this.graphTable.get(preEntity);
        return innerMap.get(nextEntity);
    }

    public Set<Entity> getNodeSet() {
        return this.graphTable.keySet();
    }

    public List<Set<Entity>> getNodeSetsClassifiedByTypes(Class... classTypes) {
        int len = classTypes.length;
        Map<String, Integer> reverseMap = new HashMap<>();
        List<Set<Entity>> resultList = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            resultList.add(new HashSet<>());
            reverseMap.put(classTypes[i].getName(), i);
        }

        for (Entity node : this.graphTable.keySet()) {
            Set<Entity> set = resultList.get(reverseMap.get(node.getClass().getName()));
            set.add(node);
        }
        return resultList;
    }

    public Map<Entity, Double> getNextNodeAndWeight(Entity preEntity) {
        return this.graphTable.get(preEntity);
    }


}
