package hnu.dll.structure.graph;

import cn.edu.dll.constant_values.ConstantValues;
import com.sun.xml.internal.ws.util.StringUtils;
import hnu.dll.config.Constant;
import hnu.dll.entity.Entity;

import java.util.*;
import java.util.stream.Collectors;

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
//        Map<Entity, Double> innerMap = this.graphTable.getOrDefault(preEntity, new HashMap<>());
//        innerMap.put(nextEntity, weight);
//        this.graphTable.put(preEntity, innerMap);
        this.graphTable.computeIfAbsent(preEntity, k->new HashMap<>()).put(nextEntity, weight);
    }

    public Double getWeight(Entity preEntity, Entity nextEntity) {
        Map<Entity, Double> innerMap = this.graphTable.get(preEntity);
        if (innerMap == null) {
            return null;
        }
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

    public void combine(SimpleGraph simpleGraph) {
        Map<Entity, Map<Entity, Double>> graphTableB = simpleGraph.getGraphTable();
        for (Map.Entry<Entity, Map<Entity, Double>> entry : graphTableB.entrySet()) {
            Entity entryKey = entry.getKey();
            Map<Entity, Double> entryValue = entry.getValue();
            for (Map.Entry<Entity, Double> innerEntry : entryValue.entrySet()) {
                Entity innerEntryKey = innerEntry.getKey();
                Double value = innerEntry.getValue();
                this.addElement(entryKey, innerEntryKey, value);
            }
        }
    }

    // for test
    public Map<Entity, Map<Entity, Double>> getGraphTable() {
        return graphTable;
    }

    public void show() {
        Set<Entity> entitySet = new HashSet<>(this.graphTable.keySet());
        Map<String, Entity> nameMap = new TreeMap<>();
        for (Map.Entry<Entity, Map<Entity, Double>> entry : this.graphTable.entrySet()) {
            entitySet.addAll(entry.getValue().keySet());
        }
        for (Entity entity : entitySet) {
            nameMap.put(entity.toSimpleString(), entity);
        }
        Set<String> entityNameSet = nameMap.keySet();
        String space = " ";
        int columnWidth = 20;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%-" + columnWidth + "s", " "));
        stringBuilder.append(entityNameSet.stream().map(e -> String.format("%-" + columnWidth + "s", e)).collect(Collectors.joining(space)));
        Double tempWeight;
        stringBuilder.append(ConstantValues.LINE_SPLIT);
        Entity entity, innerEntity;
        for (String entityName : entityNameSet) {
            stringBuilder.append(String.format("%-" + columnWidth + "s", entityName));
            entity = nameMap.get(entityName);
            for (String innerEntityName : entityNameSet) {
                innerEntity = nameMap.get(innerEntityName);
                tempWeight = this.getWeight(entity, innerEntity);
                stringBuilder.append(space);
                if (tempWeight == null) {
                    stringBuilder.append(String.format("%-" + columnWidth + "s", " "));
                } else {
                    stringBuilder.append(String.format("%-" + columnWidth + "." + Constant.PrecisionSize + "f", tempWeight));
//                    stringBuilder.append(tempWeight);
                }
            }
            stringBuilder.append(ConstantValues.LINE_SPLIT);
        }
        System.out.println(stringBuilder);
    }
}
