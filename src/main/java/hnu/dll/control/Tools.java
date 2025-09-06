package hnu.dll.control;

import hnu.dll.basic_entity.PlaneLocation;
import hnu.dll.basic_entity.ThreeDLocation;
import hnu.dll.config.Constant;
import hnu.dll.entity.*;
import hnu.dll.structure.*;
import hnu.dll.structure.basic_structure.Anchor;
import hnu.dll.structure.basic_structure.BasicPair;
import hnu.dll.structure.graph.BipartiteGraph;
import hnu.dll.structure.graph.SimpleGraph;
import hnu.dll.structure.match.Match;
import hnu.dll.structure.match.MatchElement;
import hnu.dll.structure.path.AnchorPointPath;
import hnu.dll.structure.path.TimePointPath;

import java.util.*;

public class Tools {

    public static final Class[] SupportTypes = {
            Elevator.class,
            Stair.class,
            Anchor.class
    };



    /**
     * Time Weighted Graph (TWG)
     * 根据提供的距离权重图，转换成耗时权重图
     * @param originalGraph
     * @param robot
     * @return
     */
    public static TimeWeightedGraph getTimeWeightedGraph(SimpleGraph originalGraph, Robot robot) {
        TimeWeightedGraph timeWeightedGraph = new TimeWeightedGraph();
        List<Set<Entity>> classifiedNodeSetList = originalGraph.getNodeSetsClassifiedByTypes(Tools.SupportTypes);
        Map<Entity, Double> nextNodeAndWeight;
        Entity tempEntity;
        Double tempWeight;
        Anchor anchorA, anchorB;
//        Map<Double, Anchor> anchorMap;

        for (Entity entity : classifiedNodeSetList.get(0)) {
            // elevator
            // 将电梯展开成 k-clique
            Elevator elevator = (Elevator) entity;
            Integer cliqueSize = elevator.getLayerSize();
//            anchorMap = new HashMap<>();
            List<Anchor> layersAnchorList = new ArrayList<>();
            PlaneLocation planeLocation = elevator.getPlaneLocation();
            for (int i = 0; i < cliqueSize; i++) {
                // 初始化k个节点
                layersAnchorList.add(new Anchor((i + 1) + "-layer-extension-of-elevator-" + elevator.getName(), planeLocation, BasicUtils.getZIndex(i + 1)));
            }
            // 将电梯扩展的节点之间的权重加入结果图中
            for (int i = 0; i < cliqueSize; i++) {
                anchorA = layersAnchorList.get(i);
                for (int j = 0; j < i; j++) {
                    anchorB = layersAnchorList.get(j);
                    Double time = elevator.getCrossFloorRunningTime(i + 1, j + 1, Constant.NeighboringLayersDistance);
                    timeWeightedGraph.addElement(anchorA, anchorB, time);
                    timeWeightedGraph.addElement(anchorB, anchorA, time);
                }
            }
            // 遍历与电梯相连的节点，将这些节点关联到相应的扩展点，并将权重加入结果图中
            nextNodeAndWeight = originalGraph.getNextNodeAndWeight(entity);
            for (Map.Entry<Entity, Double> entityDoubleEntry : nextNodeAndWeight.entrySet()) {
                tempEntity = entityDoubleEntry.getKey();
                // 假设电梯只会和普通节点相连
                anchorB = (Anchor) tempEntity;
                anchorA = layersAnchorList.get(BasicUtils.getLayer(anchorB.getLocation().getzIndex()) - 1);
                // 包括开关电梯门的时间
                tempWeight = entityDoubleEntry.getValue() / robot.getFlatGroundVelocity() + 2 * elevator.getOpeningOrCloseTimeCost();
                timeWeightedGraph.addElement(anchorA, anchorB, tempWeight);
                timeWeightedGraph.addElement(anchorB, anchorA, tempWeight);
            }
        }

        for (Entity entity : classifiedNodeSetList.get(1)) {
            // stair
            // 将楼梯展开成 k-path
            Stair stair = (Stair) entity;
            Integer pathPointSize = stair.getLayerSize();
            List<Anchor> layersAnchorList = new ArrayList<>();
            PlaneLocation planeLocation = stair.getPlaneLocation();

            List<ThreeDLocation> innerNodeList = stair.getInnerNodeList();
            layersAnchorList.add(new Anchor("1-layer-extension-of-stair-" + stair.getName(), planeLocation, BasicUtils.getZIndex(1)));
            for (int i = 1; i < pathPointSize; i++) {
                ThreeDLocation innerNodeLocation = innerNodeList.get(i - 1);
                layersAnchorList.add(new Anchor((i + 0.5) + "-layer-extension-of-stair-" + stair.getName(), innerNodeLocation));
                layersAnchorList.add(new Anchor((i + 1) + "-layer-extension-of-stair-" + stair.getName(), planeLocation, BasicUtils.getZIndex(i + 1)));
            }

            // 将楼梯扩展的节点之间的权重加入结果图中
            for (int i = 1; i < pathPointSize; i++) {
                anchorA = layersAnchorList.get(i-1);
                anchorB = layersAnchorList.get(i);
                Double time = stair.getSegmentLength() / robot.getStairVelocity();
                timeWeightedGraph.addElement(anchorA, anchorB, time);
                timeWeightedGraph.addElement(anchorB, anchorA, time);
            }
        }

        for (Entity entity : classifiedNodeSetList.get(2)) {
            // anchor
            anchorA = (Anchor) entity;
            // 前两个循环会处理与电梯和楼梯连接的一般锚点，这里剩下的只用处理纯一般锚点之间的连接
            nextNodeAndWeight = originalGraph.getNextNodeAndWeight(entity);
            for (Map.Entry<Entity, Double> entityDoubleEntry : nextNodeAndWeight.entrySet()) {
                tempEntity = entityDoubleEntry.getKey();
                if (tempEntity instanceof Entity || tempEntity instanceof Stair) {
                    continue;
                }
                anchorB = (Anchor) tempEntity;
                tempWeight = entityDoubleEntry.getValue() / robot.getFlatGroundVelocity();
                timeWeightedGraph.addElement(anchorA, anchorB, tempWeight);
                timeWeightedGraph.addElement(anchorB, anchorA, tempWeight);
            }

        }

        return timeWeightedGraph;
    }


    public static List<AnchorPointPath> getTopKShortestPath(TimeWeightedGraph graph, ThreeDLocation startLocation, ThreeDLocation endLocation, Integer topKSize) {
        List<AnchorPointPath> result = null;
        // todo: return top k nearest paths
        return result;
    }

    public static BipartiteGraph<Task, Robot, Double> extractTopOneToConstructBipartiteGraph(Map<Task, Map<Robot, SortedPathStructure<AnchorPointPath>>> topKMap) {
        List<Task> taskList = new ArrayList<>();
        List<Robot> robotList = new ArrayList<>();
        Task tempTask;
        Robot tempRobot;
        AnchorPointPath tempPath;
        Map<Robot, SortedPathStructure<AnchorPointPath>> tempMap;
        BipartiteGraph<Task, Robot, Double> bipartiteGraph = new BipartiteGraph<>();
        for (Map.Entry<Task, Map<Robot, SortedPathStructure<AnchorPointPath>>> taskMapEntry : topKMap.entrySet()) {
            tempTask = taskMapEntry.getKey();
            tempMap = taskMapEntry.getValue();
            for (Map.Entry<Robot, SortedPathStructure<AnchorPointPath>> entry : tempMap.entrySet()) {
                tempRobot = entry.getKey();
                tempPath = entry.getValue().getFirst();
                bipartiteGraph.addValue(tempTask, tempRobot, tempPath.getWeightedSum());
            }
        }
        return bipartiteGraph;
    }

    public static List<BasicPair<Task, Robot>> getMatchByKuhnMunkres(BipartiteGraph<Task, Robot, Double> graph) {
        List<BasicPair<Task, Robot>> matchList = new ArrayList<>();
        // todo: return a match by Kuhn Munkres algorithm
        return matchList;
    }

    public static SortedPathStructure<AnchorPointPath> topKPathTime(TimeWeightedGraph graph, ThreeDLocation startLocation, ThreeDLocation endLocation, Integer topKSize) {
        Double timeCost;
//        TimeWeightedGraph graph = getTimeWeightedGraph(simpleGraph, robot);
        List<AnchorPointPath> topKPathList = getTopKShortestPath(graph, startLocation, endLocation, topKSize);
//        List<BasicPair<Path, Double>> resultList = new ArrayList<>();
        SortedPathStructure<AnchorPointPath> result = new SortedPathStructure<>();
        for (AnchorPointPath path : topKPathList) {
//            timeCost = path.getWeightedSum();
//            result.add(new BasicPair<>(path, timeCost));
            result.addPath(path);
        }
        return result;
    }

    public static Map<BasicPair<Task, Robot>,SortedPathStructure<AnchorPointPath>> taskAssignment(TimeWeightedGraph graph, List<Task> taskList, List<Robot> robotList, Integer topKSize) {
        ThreeDLocation startLocation, innerLocation, endLocation;
        Map<BasicPair<Task, Robot>, SortedPathStructure<AnchorPointPath>> result = new HashMap<>();
        SortedPathStructure<AnchorPointPath> firstSegmentSortedPaths, lastSegmentSortedPath;
        AnchorPointPath newPath;
        Task tempTask;
        Robot tempRobot;
        Map<Robot, SortedPathStructure<AnchorPointPath>> tempMap;
        Double fetchTaskTimeCost, deliveryTaskTimeCost;
        SortedPathStructure<AnchorPointPath> tempStructure;
        Map<Task, Map<Robot, SortedPathStructure<AnchorPointPath>>> topKMap = new HashMap<>();
        for (Task task : taskList) {
//            extraTime = task.getFetchTime() + task.getSendOffTime();
            fetchTaskTimeCost = task.getFetchTime();
            deliveryTaskTimeCost = task.getSendOffTime();
            innerLocation = task.getStartLocation();
            endLocation = task.getEndLocation();
            lastSegmentSortedPath = topKPathTime(graph, innerLocation, endLocation, topKSize);
            tempMap = new HashMap<>();
            for (Robot robot : robotList) {
                if (task.getOccupyingSpace() > robot.getCapacity()) {
                    continue;
                }
                tempStructure = new SortedPathStructure<>();
                startLocation = robot.getLocation();
                firstSegmentSortedPaths = topKPathTime(graph, startLocation, innerLocation, topKSize);
                for (AnchorPointPath firstPath : firstSegmentSortedPaths.getSortedPaths()) {
                    for (AnchorPointPath lastPath : lastSegmentSortedPath.getSortedPaths()) {
                        newPath = AnchorPointPath.getCombinePathWithBothTailWeights(firstPath, lastPath, fetchTaskTimeCost, deliveryTaskTimeCost);
                        tempStructure.addPath(newPath);
                    }
                }

                tempMap.put(robot, tempStructure);
            }
            topKMap.put(task, tempMap);
        }
        BipartiteGraph<Task, Robot, Double> bipartiteGraph = extractTopOneToConstructBipartiteGraph(topKMap);
        List<BasicPair<Task, Robot>> matchList = getMatchByKuhnMunkres(bipartiteGraph);
        for (BasicPair<Task, Robot> pair : matchList) {
            tempTask = pair.getKey();
            tempRobot = pair.getValue();
            tempStructure = topKMap.get(tempTask).get(tempRobot);
            result.put(pair, tempStructure);
        }
        return result;
    }

    protected static Map<BasicPair<Task, Robot>, SortedPathStructure<TimePointPath>> toTimePointSortedPath(Map<BasicPair<Task, Robot>, SortedPathStructure<AnchorPointPath>> topKPathMap) {
        Map<BasicPair<Task, Robot>, SortedPathStructure<TimePointPath>> result = new HashMap<>();
        Robot tempRobot;
        TimePointPath tempTimePointPath;
        BasicPair<Task, Robot> tempPair;
        Task tempTask;
        Map<Robot, SortedPathStructure<TimePointPath>> tempTimeRobotMap;
        Map<Robot, SortedPathStructure<AnchorPointPath>> tempAnchorRobotMap;
        SortedPathStructure<AnchorPointPath> tempAnchorPathStructure;
        SortedPathStructure<TimePointPath> tempTimePathStructure;
        for (Map.Entry<BasicPair<Task, Robot>, SortedPathStructure<AnchorPointPath>> pairMapEntry : topKPathMap.entrySet()) {
            tempPair = pairMapEntry.getKey();
            tempAnchorPathStructure = pairMapEntry.getValue();
            tempTimePathStructure = new SortedPathStructure<>();
            for (AnchorPointPath tempAnchorPath : tempAnchorPathStructure.getSortedPaths()) {
                tempTimePointPath = TimePointPath.getTimePointPathByAnchorPointPath(tempAnchorPath);
                tempTimePathStructure.addPath(tempTimePointPath);
            }
            result.put(tempPair, tempTimePathStructure);
        }
        return result;
    }

    private static Integer getMaximumSpatialPathLength(Collection<SortedPathStructure<TimePointPath>> temporalPathSet) {
        Integer pathLength = 0;
        TimePointPath tempPath;
        for (SortedPathStructure<TimePointPath> pathStructure : temporalPathSet) {
            tempPath = pathStructure.getFirst();
            pathLength = Math.max(pathLength, tempPath.getTimeLength());
        }
        return pathLength;
    }

    private static Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> getConflictMap(Collection<SortedPathStructure<TimePointPath>> temporalPathSet, Integer timeSlot) {
        Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> result = new HashMap<>();
        TimePointPath tempPath;
        AnchorEntity tempAnchorEntity;
        Entity tempEntity;
        Set<SortedPathStructure<TimePointPath>> tempSet;
        Map.Entry<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> next;
        for (SortedPathStructure<TimePointPath> pathStructure : temporalPathSet) {
            tempPath = pathStructure.getFirst();
            tempAnchorEntity = tempPath.getAnchorEntityByIndex(timeSlot);
//                tempEntity = tempAnchorEntity.getEntity();
            tempSet = result.getOrDefault(tempAnchorEntity, new HashSet<>());
            tempSet.add(pathStructure);
        }
        Iterator<Map.Entry<AnchorEntity, Set<SortedPathStructure<TimePointPath>>>> iterator = result.entrySet().iterator();
        // 移除只含一个元素的集合，表示不冲突
        while (iterator.hasNext()) {
            next = iterator.next();
            if (next.getValue().size() > 1) {
                iterator.remove();
            }
        }
        return result;
    }

    /**
     * 获取所有冲突中的胜利匹配（路线最长）
     * @param conflictMap
     * @return
     */
    public static BasicPair<Map<AnchorEntity, SortedPathStructure<TimePointPath>>, Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>>> getWinnerAndFailureMatch(Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> conflictMap) {
        AnchorEntity tempAnchorEntity;
        Set<SortedPathStructure<TimePointPath>> tempPathStructureSet, failurePathStructureSet;
        SortedPathStructure<TimePointPath> winnerPath = null;
        Integer tempLength, maxLength;
        Map<AnchorEntity, SortedPathStructure<TimePointPath>> winnerMap = new HashMap<>();
        Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> failureMap = new HashMap<>();
        for (Map.Entry<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> anchorEntitySetEntry : conflictMap.entrySet()) {
            maxLength = 0;
            tempAnchorEntity = anchorEntitySetEntry.getKey();
            tempPathStructureSet = anchorEntitySetEntry.getValue();
            for (SortedPathStructure<TimePointPath> tempPathStructure : tempPathStructureSet) {
                tempLength = tempPathStructure.getFirst().getTimeLength();
                if (tempLength > maxLength) {
                    maxLength = tempLength;
                    winnerPath = tempPathStructure;
                }
            }
            winnerMap.put(tempAnchorEntity, winnerPath);
            failurePathStructureSet = new HashSet<>();
            failurePathStructureSet.addAll(tempPathStructureSet);
            failurePathStructureSet.remove(winnerPath);
            failureMap.put(tempAnchorEntity, failurePathStructureSet);
        }
        return new BasicPair<>(winnerMap, failureMap);
    }

    /**
     * 计算被当前path占用给定的entity的剩余时间数,设计保证了该值不小于0
     * @param timePointPath
     * @param entity
     * @param startIndex
     * @return
     */
    public static BasicPair<Integer, Integer> getRemainingOccupiedTimeSlotsAndStopLayer(TimePointPath timePointPath, Entity entity, Integer startIndex) {
        Integer pathLength = timePointPath.getTimeLength();
        Entity tempEntity = null;
        Integer remainTimeSlots;
        Integer layer;
        int endStrictIndex;
        for (endStrictIndex = startIndex; endStrictIndex < pathLength; ++endStrictIndex) {
            tempEntity = timePointPath.getAnchorEntityByIndex(endStrictIndex).getEntity();
            if (!tempEntity.equals(entity)) {
                break;
            }
        }
        remainTimeSlots = endStrictIndex - startIndex;
        Anchor anchor;
        if (tempEntity instanceof Anchor) {
            anchor = (Anchor) tempEntity;
        } else {
            // 这里假设任意电梯的出口和任意楼梯的出口都不相连，并且任意路线都不以电梯或楼梯为结束点
            // 走到这一步说明结束占用该位置的下一个位置是电梯或者楼梯，需要重新获取最后占用的位置
            // 并且只有冲突的事anchor才能走到这一步
            anchor = (Anchor) entity;
        }
        layer = BasicFunctions.getLayer(anchor.getLocation());
        return new BasicPair<>(remainTimeSlots, layer);
    }


    public static Map<SortedPathStructure<TimePointPath>, Integer> getWaitingTimeSet(Map<AnchorEntity, SortedPathStructure<TimePointPath>> winnerPathMap, Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> failurePathSetMap, Integer currentTimeIndex) {
        Map<SortedPathStructure<TimePointPath>, Integer> result = new HashMap<>();
        AnchorEntity tempAnchorEntity;
        Anchor tempAnchor;
        Entity tempEntity;
        SortedPathStructure<TimePointPath> winnerSortedPathStructure;
        Set<SortedPathStructure<TimePointPath>> failurePathSet;
        TimePointPath winnerTimePointPath;
        BasicPair<Integer, Integer> timeSlotsLayerPair;
        Integer remainOccupiedTimeSlots;
        Integer tempWaitedTimeSlots;
        Integer tempLastLayer, targetLayer;
        for (Map.Entry<AnchorEntity, SortedPathStructure<TimePointPath>> winnerEntry : winnerPathMap.entrySet()) {
            tempAnchorEntity = winnerEntry.getKey();
            tempEntity = tempAnchorEntity.getEntity();
            winnerSortedPathStructure = winnerEntry.getValue();
            winnerTimePointPath = winnerSortedPathStructure.getFirst();
            timeSlotsLayerPair = getRemainingOccupiedTimeSlotsAndStopLayer(winnerTimePointPath, tempEntity, currentTimeIndex);
            remainOccupiedTimeSlots = timeSlotsLayerPair.getKey();
            tempAnchor = tempAnchorEntity.getAnchor();
            if (tempEntity instanceof Elevator) {
                Elevator elevator = (Elevator) tempEntity;
                tempLastLayer = timeSlotsLayerPair.getValue();
                targetLayer = BasicFunctions.getLayer(tempAnchor.getLocation());
                // 计算电梯被占用的等待时间
                tempWaitedTimeSlots = remainOccupiedTimeSlots + BasicFunctions.getElevatorRunningTimeSlots(tempLastLayer, targetLayer, elevator.getVelocity());
            } else {
                // 计算当前楼梯或锚点被占用的等待时间
                tempWaitedTimeSlots = remainOccupiedTimeSlots;
            }
            failurePathSet = failurePathSetMap.get(tempAnchorEntity);
            for (SortedPathStructure<TimePointPath> pathStructure : failurePathSet) {
                result.put(pathStructure, tempWaitedTimeSlots);
            }
        }
        return result;
    }

    public static boolean delayAndUpdate(Map<SortedPathStructure<TimePointPath>, Integer> timeWaitedMap, Integer startTime) {
        SortedPathStructure<TimePointPath> tempStructure;
        TimePointPath tempPath;
        Integer tempDelaySlots;
        AnchorEntity tempLastAnchorEntity;
        boolean flag = false;
        for (Map.Entry<SortedPathStructure<TimePointPath>, Integer> entry : timeWaitedMap.entrySet()) {
            tempStructure = entry.getKey();
            tempPath = tempStructure.getFirst();
            tempDelaySlots = entry.getValue();
            // 假设一开始的0时刻没有冲突
            tempLastAnchorEntity = tempPath.getAnchorEntityByIndex(startTime - 1);
            tempPath.insertAnchorEntity(tempLastAnchorEntity, startTime, tempDelaySlots);
            if (tempPath != tempStructure.getFirst()) {
                flag = true;
            }
        }
        return flag;
    }



    public static Integer eliminate(Map<BasicPair<Task, Robot>, SortedPathStructure<TimePointPath>> temporalPathMap) {
        Integer maximumPathLength = getMaximumSpatialPathLength(temporalPathMap.values());
        Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> conflictMap;
        AnchorEntity tempAnchorEntity;
        Entity tempEntity;
        Set<SortedPathStructure<TimePointPath>> tempPathStructureSet;
        Boolean flag = false;
        BasicPair<Map<AnchorEntity, SortedPathStructure<TimePointPath>>, Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>>> winFailurePair;
        Map<AnchorEntity, SortedPathStructure<TimePointPath>> winnerMatch;
        Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> failureMatch;
        Map<SortedPathStructure<TimePointPath>, Integer> waitingTimeSet;
        for (int i = 0; i < maximumPathLength; ++i) {
            conflictMap = getConflictMap(temporalPathMap.values(), i);
            winFailurePair = getWinnerAndFailureMatch(conflictMap);
            winnerMatch = winFailurePair.getKey();
            failureMatch = winFailurePair.getValue();
            waitingTimeSet = getWaitingTimeSet(winnerMatch, failureMatch, i);
            flag = delayAndUpdate(waitingTimeSet, i);
            if (!conflictMap.isEmpty()) {
                maximumPathLength = BasicFunctions.getMaximalTimeSlotLength(waitingTimeSet.keySet());
            }
            if (flag) {
                i = 0;
            }
        }
        return maximumPathLength;
    }

    public static Match getPlanPath(TimeWeightedGraph graph, List<Robot> robotList, Job job) {
        Map<BasicPair<Task,Robot>, SortedPathStructure<TimePointPath>> result;
        job.initialTaskStartTimeAndEndTime();
        List<Task> taskList = job.getTaskList();
        Map<BasicPair<Task, Robot>, SortedPathStructure<AnchorPointPath>> matchMapBasicPair = taskAssignment(graph, taskList, robotList, Constant.topKSize);
        Task tempTask;
        Integer maximalTimeSlotLength;
        BasicPair<Task, Robot> tempPair;
        SortedPathStructure<TimePointPath> pathStructure;
        MatchElement tempMatchElement;
        Map<Robot,SortedPathStructure<AnchorPointPath>> tempAnchorRobotMap;
        Map<Robot,SortedPathStructure<TimePointPath>> tempTimeRobotMap;
        Robot tempRobot;
        AnchorPointPath tempAnchorPointPath;
        TimePointPath tempTimePointPath;
        result = toTimePointSortedPath(matchMapBasicPair);
        maximalTimeSlotLength = eliminate(result);
        Match match = new Match();

        for (Map.Entry<BasicPair<Task, Robot>, SortedPathStructure<TimePointPath>> entry : result.entrySet()) {
            tempPair = entry.getKey();
            pathStructure = entry.getValue();
            tempAnchorPointPath = matchMapBasicPair.get(tempPair).getFirst();
            tempTimePointPath = pathStructure.getFirst();
            tempMatchElement = new MatchElement(tempPair.getKey(), tempPair.getValue(), tempAnchorPointPath, tempTimePointPath);
            match.add(tempMatchElement);
        }
        match.setMaxCostTimeLength(maximalTimeSlotLength);
        
        return match;
    }

}
