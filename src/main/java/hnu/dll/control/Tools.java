package hnu.dll.control;

import hnu.dll.basic_entity.PlaneLocation;
import hnu.dll.basic_entity.ThreeDLocation;
import hnu.dll.config.Constant;
import hnu.dll.entity.*;
import hnu.dll.structure.*;
import hnu.dll.structure.basic_structure.BasicPair;

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


    public static List<Path> getTopKShortestPath(TimeWeightedGraph graph, ThreeDLocation startLocation, ThreeDLocation endLocation, Integer topKSize) {
        List<Path> result = null;
        // todo: return top k nearest paths
        return result;
    }

    public static BipartiteGraph<Task, Robot, Double> extractTopOneToConstructBipartiteGraph(Map<Task, Map<Robot, SortedPathStructure>> topKMap) {
        List<Task> taskList = new ArrayList<>();
        List<Robot> robotList = new ArrayList<>();
        Task tempTask;
        Robot tempRobot;
        Path tempPath;
        Map<Robot, SortedPathStructure> tempMap;
        BipartiteGraph<Task, Robot, Double> bipartiteGraph = new BipartiteGraph();
        for (Map.Entry<Task, Map<Robot, SortedPathStructure>> taskMapEntry : topKMap.entrySet()) {
            tempTask = taskMapEntry.getKey();
            tempMap = taskMapEntry.getValue();
            for (Map.Entry<Robot, SortedPathStructure> entry : tempMap.entrySet()) {
                tempRobot = entry.getKey();
                tempPath = entry.getValue().getFirst();
                bipartiteGraph.addValue(tempTask, tempRobot, tempPath.getWeightedSum());
            }
        }
        return bipartiteGraph;
    }

    public static Match<Task, Robot> getMatchByKuhnMunkres(BipartiteGraph<Task, Robot, Double> graph) {
        Match<Task, Robot> match = new Match();
        // todo: return a match by Kuhn Munkres algorithm
        return match;
    }

    public static SortedPathStructure topKPathTime(TimeWeightedGraph graph, ThreeDLocation startLocation, ThreeDLocation endLocation, Integer topKSize) {
        Double timeCost;
//        TimeWeightedGraph graph = getTimeWeightedGraph(simpleGraph, robot);
        List<Path> topKPathList = getTopKShortestPath(graph, startLocation, endLocation, topKSize);
//        List<BasicPair<Path, Double>> resultList = new ArrayList<>();
        SortedPathStructure result = new SortedPathStructure();
        for (Path path : topKPathList) {
//            timeCost = path.getWeightedSum();
//            result.add(new BasicPair<>(path, timeCost));
            result.addPath(path);
        }
        return result;
    }

    public static BasicPair<Match<Task, Robot>, Map<Task, Map<Robot, SortedPathStructure>>> taskAssignment(TimeWeightedGraph graph, List<Task> taskList, List<Robot> robotList, Integer topKSize) {
        ThreeDLocation startLocation, innerLocation, endLocation;
        SortedPathStructure firstSegmentSortedPaths, lastSegmentSortedPath;
        Path newPath;
        Map<Robot, SortedPathStructure> tempMap;
        Double firstTimeCost, lastTimeCost, extraTime, newTimeCost;
        SortedPathStructure tempStructure;
        Map<Task, Map<Robot, SortedPathStructure>> topKMap = new HashMap<>();
        for (Task task : taskList) {
            extraTime = task.getFetchTime() + task.getSendOffTime();
            innerLocation = task.getStartLocation();
            endLocation = task.getEndLocation();
            lastSegmentSortedPath = topKPathTime(graph, innerLocation, endLocation, topKSize);
            tempMap = new HashMap<>();
            for (Robot robot : robotList) {
                if (task.getOccupyingSpace() > robot.getCapacity()) {
                    continue;
                }
                tempStructure = new SortedPathStructure();
                startLocation = robot.getLocation();
                firstSegmentSortedPaths = topKPathTime(graph, startLocation, innerLocation, topKSize);
                for (Path firstPath : firstSegmentSortedPaths.getSortedPaths()) {
                    for (Path lastPath : lastSegmentSortedPath.getSortedPaths()) {
                        newPath = Path.getCombinePath(firstPath, lastPath, extraTime);
                        tempStructure.addPath(newPath);
                    }
                }

                tempMap.put(robot, tempStructure);
            }
            topKMap.put(task, tempMap);
        }
        BipartiteGraph<Task, Robot, Double> bipartiteGraph = extractTopOneToConstructBipartiteGraph(topKMap);
        Match<Task, Robot> match = getMatchByKuhnMunkres(bipartiteGraph);
        return new BasicPair<>(match, topKMap);
    }

    

}
