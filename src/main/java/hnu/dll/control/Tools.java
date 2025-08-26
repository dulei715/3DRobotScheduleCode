package hnu.dll.control;

import hnu.dll.entity.Elevator;
import hnu.dll.entity.Entity;
import hnu.dll.entity.Robot;
import hnu.dll.entity.Stair;
import hnu.dll.structure.Anchor;
import hnu.dll.structure.SimpleGraph;
import hnu.dll.structure.TimeWeightedGraph;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tools {

    public static final Class[] SupportTypes = {
            Elevator.class,
            Stair.class,
            Anchor.class
    };

    public static TimeWeightedGraph timeWeightedGraph(SimpleGraph originalGraph, Robot robot) {
        TimeWeightedGraph timeWeightedGraph = new TimeWeightedGraph();
        List<Set<Entity>> classifiedNodeSetList = originalGraph.getNodeSetsClassifiedByTypes(Tools.SupportTypes);
        Map<Entity, Double> nextNodeAndWeight;
        Entity tempEntity;

        for (Entity entity : classifiedNodeSetList.get(0)) {
            // elevator
            // todo
        }

        for (Entity entity : classifiedNodeSetList.get(1)) {
            // stair
            // todo
        }

        for (Entity entity : classifiedNodeSetList.get(2)) {
            // anchor
            Anchor anchor = (Anchor) entity;
            // 前两个循环会处理与电梯和楼梯连接的一般锚点，这里剩下的只用处理纯一般锚点之间的连接
            nextNodeAndWeight = originalGraph.getNextNodeAndWeight(entity);
            for (Map.Entry<Entity, Double> entityDoubleEntry : nextNodeAndWeight.entrySet()) {
                tempEntity = entityDoubleEntry.getKey();
                if (tempEntity instanceof Entity || tempEntity instanceof Stair) {
                    continue;
                }
                timeWeightedGraph.addElement(anchor, (Anchor) tempEntity, entityDoubleEntry.getValue());
            }

        }

        return timeWeightedGraph;
    }
}
