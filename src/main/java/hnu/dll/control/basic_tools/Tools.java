package hnu.dll.control.basic_tools;

import hnu.dll.basic_entity.location.PlaneLocation;
import hnu.dll.basic_entity.location.ThreeDLocation;
import hnu.dll.config.Constant;
import hnu.dll.control.BasicFunctions;
import hnu.dll.control.BasicUtils;
import hnu.dll.control.topk.AnchorEntityConvertor;
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
                layersAnchorList.add(new Anchor("B-" + (i + 1) + "-of " + elevator.getName(), planeLocation, BasicUtils.getZIndex(i + 1)));
            }
            // 将电梯扩展的节点之间的权重加入结果图中
            for (int i = 0; i < cliqueSize; i++) {
                anchorA = layersAnchorList.get(i);
                for (int j = 0; j < i; j++) {
                    anchorB = layersAnchorList.get(j);
                    Double time = elevator.getCrossFloorRunningTime(i + 1, j + 1);
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
//                tempWeight = entityDoubleEntry.getValue() / robot.getFlatGroundVelocity() + 2 * elevator.getOpeningOrCloseTimeCost();
                // 不包括开关电梯门的时间
                tempWeight = entityDoubleEntry.getValue() / robot.getFlatGroundVelocity();
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
            Double tempIndex = 1D;
            for (ThreeDLocation location : innerNodeList) {
                layersAnchorList.add(new Anchor("C-" + tempIndex + " of " + stair.getName(), location));
                tempIndex += 0.5;
            }

            // 将楼梯扩展的节点之间的权重加入结果图中
            for (int i = 1; i < layersAnchorList.size(); i++) {
                anchorA = layersAnchorList.get(i-1);
                anchorB = layersAnchorList.get(i);
                Double time = stair.getSegmentLength() / robot.getStairVelocity();
                timeWeightedGraph.addElement(anchorA, anchorB, time);
                timeWeightedGraph.addElement(anchorB, anchorA, time);
            }

            // 遍历与楼梯相连的节点，将这些节点关联到相应的扩展点，并将权重加入结果图中
            nextNodeAndWeight = originalGraph.getNextNodeAndWeight(entity);
            for (Map.Entry<Entity, Double> entityDoubleEntry : nextNodeAndWeight.entrySet()) {
                tempEntity = entityDoubleEntry.getKey();
                // 假设楼梯只会和普通节点相连
                anchorB = (Anchor) tempEntity;
                anchorA = layersAnchorList.get((BasicUtils.getLayer(anchorB.getLocation().getzIndex()) - 1) * 2);
                tempWeight = entityDoubleEntry.getValue() / robot.getFlatGroundVelocity();
                timeWeightedGraph.addElement(anchorA, anchorB, tempWeight);
                timeWeightedGraph.addElement(anchorB, anchorA, tempWeight);
            }


        }

        for (Entity entity : classifiedNodeSetList.get(2)) {
            // anchor
            anchorA = (Anchor) entity;
            // 前两个循环会处理与电梯和楼梯连接的一般锚点，这里剩下的只用处理纯一般锚点之间的连接
            nextNodeAndWeight = originalGraph.getNextNodeAndWeight(entity);
            for (Map.Entry<Entity, Double> entityDoubleEntry : nextNodeAndWeight.entrySet()) {
                tempEntity = entityDoubleEntry.getKey();
                if (tempEntity instanceof Elevator || tempEntity instanceof Stair) {
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








    public static Map<BasicPair<Task, Robot>,SortedPathStructure<AnchorPointPath>> taskAssignment(Map<String, TimeWeightedGraph> graphMap, List<Task> taskList, List<Robot> robotList, Integer topKSize, AnchorEntityConvertor convert) {
        Anchor startAnchor, innerAnchor, endAnchor;
        Map<BasicPair<Task, Robot>, SortedPathStructure<AnchorPointPath>> result = new HashMap<>();
        SortedPathStructure<AnchorPointPath> firstSegmentSortedPaths, lastSegmentSortedPath;
        AnchorPointPath newPath;
        Task tempTask;
        Robot tempRobot;
        Map<Robot, SortedPathStructure<AnchorPointPath>> tempMap;
        Double fetchTaskTimeCost, deliveryTaskTimeCost;
        SortedPathStructure<AnchorPointPath> tempStructure;
        Map<String, SortedPathStructure<AnchorPointPath>> tempLastSegmentSortedPathMap;
        Map<Task, Map<Robot, SortedPathStructure<AnchorPointPath>>> topKMap = new HashMap<>();
        for (Task task : taskList) {
//            extraTime = task.getFetchTime() + task.getSendOffTime();
            fetchTaskTimeCost = task.getFetchTime();
            deliveryTaskTimeCost = task.getSendOffTime();
            innerAnchor = task.getStartAnchor();
            endAnchor = task.getEndAnchor();
            tempLastSegmentSortedPathMap = new HashMap<>();
            for (Map.Entry<String, TimeWeightedGraph> entry : graphMap.entrySet()) {
                String type = entry.getKey();
                TimeWeightedGraph tempGraph = entry.getValue();
                tempLastSegmentSortedPathMap.put(type, BasicFunctions.topKPathTime(tempGraph, innerAnchor, endAnchor, topKSize, convert));
            }

//            lastSegmentSortedPath = BasicFunctions.topKPathTime(graph, innerLocation, endLocation, topKSize);
            tempMap = new HashMap<>();
            for (Robot robot : robotList) {
                if (task.getOccupyingSpace() > robot.getCapacity()) {
                    continue;
                }
                String robotType = robot.getType();
                lastSegmentSortedPath = tempLastSegmentSortedPathMap.get(robotType);
                tempStructure = new SortedPathStructure<>();
                startAnchor = robot.getLocation();
                firstSegmentSortedPaths = BasicFunctions.topKPathTime(graphMap.get(robotType), startAnchor, innerAnchor, topKSize, convert);
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
        BipartiteGraph<Task, Robot, Double> bipartiteGraph = BasicFunctions.extractTopOneToConstructBipartiteGraph(topKMap);
        List<BasicPair<Task, Robot>> matchList = BasicFunctions.getMatchByKuhnMunkres(bipartiteGraph);
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
        TimePointPath tempTimePointPath;
        BasicPair<Task, Robot> tempPair;
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

    /**
     * 返回冲突的实体以及首次征用电梯(上一时刻未占用该电梯) 对应的路线
     * @param temporalPathSet
     * @param timeSlot
     * @return
     */
    private static Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> getConflictMapWithElevatorSingleProposalSet(Collection<SortedPathStructure<TimePointPath>> temporalPathSet, Integer timeSlot) {
        Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> result = new HashMap<>();
        TimePointPath tempPath;
        AnchorEntity tempAnchorEntity;
        Set<SortedPathStructure<TimePointPath>> tempSet;
        Map.Entry<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> next;
        for (SortedPathStructure<TimePointPath> pathStructure : temporalPathSet) {
            tempPath = pathStructure.getFirst();
            if (timeSlot >= tempPath.getTimeLength()) {
                continue;
            }
            tempAnchorEntity = tempPath.getAnchorEntityByIndex(timeSlot);
            result.computeIfAbsent(tempAnchorEntity, k->new HashSet<>()).add(pathStructure);
//            tempSet = result.getOrDefault(tempAnchorEntity, new HashSet<>());
//            tempSet.add(pathStructure);
        }
        Iterator<Map.Entry<AnchorEntity, Set<SortedPathStructure<TimePointPath>>>> iterator = result.entrySet().iterator();
        // 移除只含一个元素的集合，表示不冲突
        while (iterator.hasNext()) {
            next = iterator.next();
            // 只有当实体的占用不大于1(无冲突)且[占用的不是电梯或[是电梯但上个时刻已经占用了]时才移除]
            Entity tempEntity = next.getKey().getEntity();
            Set<SortedPathStructure<TimePointPath>> tempStructureSet = next.getValue();
            if (tempStructureSet.size() <= 1 && (!(tempEntity instanceof Elevator) ||
                    (timeSlot > 0 && tempStructureSet.iterator().next().getFirst().getAnchorEntityByIndex(timeSlot-1).getEntity().equals(tempEntity)))) {
                iterator.remove();
            }
        }
        return result;
    }

    /**
     * 获取所有冲突中的胜利匹配（路线最长）
     * @param conflictMapWithElevatorProposal
     * @return
     */
    protected static CompeteStructure getWinnerAndFailureMatch(Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> conflictMapWithElevatorProposal, Integer conflictTimeSlot) {
        AnchorEntity tempAnchorEntity, beforeAnchorEntity;
        Set<SortedPathStructure<TimePointPath>> tempPathStructureSet, failurePathStructureSet;
        SortedPathStructure<TimePointPath> winnerPath = null;
        Integer tempLength, maxLength;
        TimePointPath tempTimePointPath;
        Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> multipleMap, alreadyMap;
        /**
         * todo: 已修正
         * 1. 已经占用电梯的优先级最高
         * 2. 同时占用电梯的剩余路线时长越长优先级越高
         *
         * 要对是否存在已经占用的电梯的情况进行处理，分类续解决冲突，传入的冲突map中，值的集合有两个及以上的按照正常冲突处理，仅有一个的就是电梯
         * (1) 如果已占用
         *      胜利者不用延迟；
         *      其他失败者的要等到当前执行完，并且还要算上执行完后电梯空运行的时间（到达任意一个失败者所在层）
         * (2) 如果电梯被同时征用
         *      胜利者需要等待电梯空运时间
         *      其他失败者要要等到胜利者执行完，并且还要算上执行完后电梯空运行的时间
         *
         * 1. 要将当前第一次占用电梯且无冲突的执行路线延迟空电梯运行的时间单位(Single User)
         * 2. 要将都是新占用的 (Multiple first Users)
         *
         * 返回三个部分：
         * (1) 首次单个占用电梯的路线
         * (2) 冲突获胜路线
         *      a. 已占用电梯多时的胜利者
         *      b. 首次竞争电梯的胜利者
         *      c. 竞争其他实体的胜利者
         * (3) 冲突失败路线
         *      (同2中对应的失败者)
         */

        Map<AnchorEntity, SortedPathStructure<TimePointPath>> singleFirstElevatorOccupationMap = new HashMap<>();
        Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> tempConflictMap = new HashMap<>(conflictMapWithElevatorProposal);
        Iterator<Map.Entry<AnchorEntity, Set<SortedPathStructure<TimePointPath>>>> tempEntryIterator = tempConflictMap.entrySet().iterator();
        Map.Entry<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> tempAnchorEntitySetEntry;

        while (tempEntryIterator.hasNext()) {
            tempAnchorEntitySetEntry = tempEntryIterator.next();
            tempAnchorEntity = tempAnchorEntitySetEntry.getKey();
            tempPathStructureSet = tempAnchorEntitySetEntry.getValue();
            if (tempPathStructureSet.size() <= 1) {
                // 记录首次单个电梯占用
                singleFirstElevatorOccupationMap.put(tempAnchorEntity, tempPathStructureSet.iterator().next());
                tempEntryIterator.remove();
            }
        }

        // 处理存在上个时刻已经占用实体的冲突情况：找出冲突中的已经占用电梯的实体作为赢家，其他为失败者，并从冲突集合中剔除
        alreadyMap = new HashMap<>();
        tempEntryIterator = tempConflictMap.entrySet().iterator();
        while (conflictTimeSlot > 0 && tempEntryIterator.hasNext()) {
            tempAnchorEntitySetEntry = tempEntryIterator.next();
            tempAnchorEntity = tempAnchorEntitySetEntry.getKey();
            tempPathStructureSet = tempAnchorEntitySetEntry.getValue();
            for (SortedPathStructure<TimePointPath> tempPathStructure : tempPathStructureSet) {
                tempTimePointPath = tempPathStructure.getFirst();
                beforeAnchorEntity = tempTimePointPath.getAnchorEntityByIndex(conflictTimeSlot - 1);
                if (beforeAnchorEntity.getEntity().equals(tempAnchorEntity.getEntity())) {
                    failurePathStructureSet = new HashSet<>(tempPathStructureSet);
                    failurePathStructureSet.remove(tempPathStructure);
                    alreadyMap.put(tempAnchorEntity, new BasicPair<>(tempPathStructure, failurePathStructureSet));
                    tempEntryIterator.remove();
                    break;
                }
            }
        }

        // 处理都是新占用电梯的情况：找出冲突中剩余路线时长最长的座位赢家，其他为失败者
        multipleMap = new HashMap<>();
        tempEntryIterator = tempConflictMap.entrySet().iterator();
        while (tempEntryIterator.hasNext()) {
            tempAnchorEntitySetEntry = tempEntryIterator.next();
            maxLength = 0;
            tempAnchorEntity = tempAnchorEntitySetEntry.getKey();
            tempPathStructureSet = tempAnchorEntitySetEntry.getValue();
            for (SortedPathStructure<TimePointPath> tempPathStructure : tempPathStructureSet) {
                tempLength = tempPathStructure.getFirst().getTimeLength();
                if (tempLength > maxLength) {
                    maxLength = tempLength;
                    winnerPath = tempPathStructure;
                }
            }
            failurePathStructureSet = new HashSet<>(tempPathStructureSet);
            failurePathStructureSet.remove(winnerPath);
            multipleMap.put(tempAnchorEntity, new BasicPair<>(winnerPath, failurePathStructureSet));
        }
        return new CompeteStructure(singleFirstElevatorOccupationMap, multipleMap, alreadyMap);
    }

    /**
     * 计算被当前path占用给定的entity的剩余时间数,设计保证了该值不小于0
     * 仅适用于之前就已占用了电梯的情况
     * @param timePointPath
     * @param entity
     * @param startIndex
     * @return
     */
    protected static BasicPair<Integer, Integer> getRemainingOccupiedTimeSlotsAndStopLayer(TimePointPath timePointPath, Entity entity, Integer startIndex) {
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

    /**
     * 1. 处理单个首次电梯请求
     * 2. 处理多个首次电梯请求冲突
     * 3. 处理一个早已占用电梯+多个首次电梯请求冲突
     * 4. 处理其他冲突
     * @param currentTimeIndex
     * @return 每个需要延后的路径对应的 pair(是否成功占用该电梯，顺延多少个时刻)
     * 这里不会记录那些在上个时刻就已经占用电梯的路线
     * 因此如果pair的key是true，那表示这个路线是单个首次占用电梯的路线或者是多个首次占用电梯路线的胜利者
     */
    protected static Map<SortedPathStructure<TimePointPath>, BasicPair<Boolean, Integer>> getWaitingTimeSet(CompeteStructure competeStructure, Integer currentTimeIndex) {
        Map<AnchorEntity, SortedPathStructure<TimePointPath>> singleMap = competeStructure.getSingleFirstElevatorRequirementMap();
        Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> multipleMap = competeStructure.getMultipleFirstRequirementMap();
        Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> alreadyMap = competeStructure.getAlreadyOccupiedMap();
        Map<SortedPathStructure<TimePointPath>, BasicPair<Boolean, Integer>> result = new HashMap<>();
        AnchorEntity tempAnchorEntity;
        Anchor tempAnchor;
        Entity tempEntity;
        SortedPathStructure<TimePointPath> tempWinnerPathStructure, singleSortedPathStructure;
        Set<SortedPathStructure<TimePointPath>> failurePathSet;
        TimePointPath winnerTimePointPath;
        BasicPair<Integer, Integer> timeSlotsLayerPair;
        Integer remainOccupiedTimeSlots;
        Integer tempWaitedTimeSlots, winnerWaitedTimeSlot, failureWaitedTimeSlot;
        Integer tempLastLayer, targetLayer;
        BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>> tempPair;
        Set<SortedPathStructure<TimePointPath>> tempFailurePathStructureSet;
        /**
         * 1. 处理单个首次申请电梯的请求
         *    (1) 要求此刻启动电梯
         *    (2) 添加电梯占用时刻数为电梯到达请求层的耗时数
         *    (3) 忽略添加后不再是top 1 情况的影响
         */

        for (Map.Entry<AnchorEntity, SortedPathStructure<TimePointPath>> entry : singleMap.entrySet()) {
            tempAnchorEntity = entry.getKey();
            singleSortedPathStructure = entry.getValue();
            Elevator elevator = (Elevator) tempAnchorEntity.getEntity();
            tempLastLayer = elevator.getCurrentLayer(currentTimeIndex);
            // 保证0时刻不会有电梯的请求
            Anchor beforeAnchor = singleSortedPathStructure.getFirst().getAnchorEntityByIndex(currentTimeIndex - 1).getAnchor();
            targetLayer = BasicFunctions.getLayer(beforeAnchor.getLocation());
            // 征用电梯会带来一次电梯开门和一次电梯关门的消耗
//            tempWaitedTimeSlots = BasicFunctions.getElevatorRunningTimeAndOpeningCloseDoorSlots(tempLastLayer, targetLayer, elevator.getVelocity(), Constant.OpenOrCloseDoorTimeCost, Constant.OpenOrCloseDoorTimeCost);
            tempWaitedTimeSlots = BasicFunctions.getElevatorRunningTimeAndOpeningCloseDoorSlots(tempLastLayer, targetLayer, elevator.getVelocity(), Constant.OpenOrCloseDoorTimeCost, 3);
            result.put(singleSortedPathStructure, new BasicPair<>(true, tempWaitedTimeSlots));
        }
        /**
         * 2. 处理同时多方首次占用某个实体的请求
         * todo: 更正：时间扩张图不要包含电梯开关门，电梯开关门只在请求电梯时计算
         */
        for (Map.Entry<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> entry : multipleMap.entrySet()) {
            tempAnchorEntity = entry.getKey();
            tempPair = entry.getValue();
            tempWinnerPathStructure = tempPair.getKey();
            tempFailurePathStructureSet = tempPair.getValue();
            tempAnchor = tempAnchorEntity.getAnchor();
            tempEntity = tempAnchorEntity.getEntity();
            winnerTimePointPath = tempWinnerPathStructure.getFirst();
            timeSlotsLayerPair = getRemainingOccupiedTimeSlotsAndStopLayer(winnerTimePointPath, tempEntity, currentTimeIndex);
            remainOccupiedTimeSlots = timeSlotsLayerPair.getKey();
            if (tempEntity instanceof Elevator) {
                Elevator elevator = (Elevator) tempEntity;
                tempLastLayer = elevator.getCurrentLayer(currentTimeIndex);
                targetLayer = BasicFunctions.getLayer(tempAnchor.getLocation());
                // 成功者等待时间=电梯回来时间+3次电梯门时间
//                winnerWaitedTimeSlot = BasicFunctions.getElevatorRunningTimeSlots(tempLastLayer, targetLayer, elevator.getVelocity());
                winnerWaitedTimeSlot = BasicFunctions.getElevatorRunningTimeAndOpeningCloseDoorSlots(tempLastLayer, targetLayer, elevator.getVelocity(), Constant.ElevatorAverageVelocity, 3);
                result.put(tempWinnerPathStructure, new BasicPair<>(true, winnerWaitedTimeSlot));
                // 失败者等待时间=成功者等待时间+电梯送成功者到站时间+一次关门时间+回来时间+三次电梯门时间
                tempLastLayer = targetLayer;
                for (SortedPathStructure<TimePointPath> failurePathStructure : tempFailurePathStructureSet) {
                    AnchorEntity failureAnchorEntity = failurePathStructure.getFirst().getAnchorEntityByIndex(currentTimeIndex);
                    targetLayer = BasicFunctions.getLayer(failureAnchorEntity.getAnchor().getLocation());
                    failureWaitedTimeSlot = winnerWaitedTimeSlot + remainOccupiedTimeSlots + BasicFunctions.getElevatorRunningTimeAndOpeningCloseDoorSlots(tempLastLayer, targetLayer, elevator.getVelocity(), Constant.OpenOrCloseDoorTimeCost, 4);
                    result.put(failurePathStructure, new BasicPair<>(false, failureWaitedTimeSlot));
                }
            } else {
                failureWaitedTimeSlot = remainOccupiedTimeSlots;
                for (SortedPathStructure<TimePointPath> failurePathStructure : tempFailurePathStructureSet) {
                    result.put(failurePathStructure, new BasicPair<>(false, failureWaitedTimeSlot));
                }
            }
        }


        /**
         * 3. 处理胜利者在前一时刻已经占用某个实体的请求
         */
        // 处理早已率先占用实体的情况
        for (Map.Entry<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> entry : alreadyMap.entrySet()) {
            tempAnchorEntity = entry.getKey();
            tempEntity = tempAnchorEntity.getEntity();
            tempPair = entry.getValue();
            tempWinnerPathStructure = tempPair.getKey();
            winnerTimePointPath = tempWinnerPathStructure.getFirst();
            tempFailurePathStructureSet = tempPair.getValue();
            timeSlotsLayerPair = getRemainingOccupiedTimeSlotsAndStopLayer(winnerTimePointPath, tempEntity, currentTimeIndex);
            remainOccupiedTimeSlots = timeSlotsLayerPair.getKey();
            tempAnchor = tempAnchorEntity.getAnchor();
            if (tempEntity instanceof Elevator) {
                Elevator elevator = (Elevator) tempEntity;
                tempLastLayer = timeSlotsLayerPair.getValue();
                for (SortedPathStructure<TimePointPath> failurePathStructure : tempFailurePathStructureSet) {
                    AnchorEntity failureAnchorEntity = failurePathStructure.getFirst().getAnchorEntityByIndex(currentTimeIndex);
                    targetLayer = BasicFunctions.getLayer(failureAnchorEntity.getAnchor().getLocation());
                    failureWaitedTimeSlot = remainOccupiedTimeSlots + BasicFunctions.getElevatorRunningTimeSlots(tempLastLayer, targetLayer, elevator.getVelocity());
                    result.put(failurePathStructure, new BasicPair<>(false, failureWaitedTimeSlot));
                }
            } else {
                // 计算当前楼梯或锚点被占用的等待时间
                failureWaitedTimeSlot = remainOccupiedTimeSlots;
                for (SortedPathStructure<TimePointPath> failurePathStructure : tempFailurePathStructureSet) {
                    result.put(failurePathStructure, new BasicPair<>(false, failureWaitedTimeSlot));
                }
            }
        }

        return result;
    }

    protected static boolean delayAndUpdate(Map<SortedPathStructure<TimePointPath>, BasicPair<Boolean, Integer>> timeWaitedMap, Integer startTime) {
        SortedPathStructure<TimePointPath> tempStructure;
        TimePointPath tempPath;
        Integer tempDelaySlots;
        AnchorEntity tempLastAnchorEntity, tempCurrentAnchorEntity;
        BasicPair<Boolean, Integer> tempPair;
        Boolean tempStatus;
        boolean flag = false;
        /**
         * 遍历所有的路径结构：
         * 1. boolean值为true的表示单个首次占用或多个首次占用的胜利者，他们占用当前实体（只有电梯的情况），并延续相应时间
         * 2. boolean值为false的表示失败者，要顺延前一时刻的位置
         */
        for (Map.Entry<SortedPathStructure<TimePointPath>, BasicPair<Boolean, Integer>> entry : timeWaitedMap.entrySet()) {
            tempStructure = entry.getKey();
            tempPath = tempStructure.getFirst();
            tempPair = entry.getValue();
            tempStatus = tempPair.getKey();
            tempDelaySlots = tempPair.getValue();
            if (tempStatus) {
                // 处理胜利者的时延
                tempCurrentAnchorEntity = tempPath.getAnchorEntityByIndex(startTime);
                // 这里从当前时间点插或从者后一个时间都可以，因为插的是同一个电梯
                tempPath.insertAnchorEntity(tempCurrentAnchorEntity, startTime, tempDelaySlots);
            } else {
                // 处理失败者的时延
                // 假设一开始的0时刻没有冲突
//                System.out.println("有冲突");
                tempLastAnchorEntity = tempPath.getAnchorEntityByIndex(startTime - 1);
                tempPath.insertAnchorEntity(tempLastAnchorEntity, startTime, tempDelaySlots);
            }
            if (tempPath != tempStructure.getFirst()) {
                flag = true;
            }
        }
        return flag;
    }



    protected static Integer eliminate(Map<BasicPair<Task, Robot>, SortedPathStructure<TimePointPath>> temporalPathMap, Collection<Elevator> elevators) {
        Integer maximumPathLength = getMaximumSpatialPathLength(temporalPathMap.values());
        Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> conflictMapWithSingleElevatorProposal;
        Boolean flag = false;
        BasicPair<Map<AnchorEntity, SortedPathStructure<TimePointPath>>, Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>>> winFailurePair;
        Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> multipleMap;
        Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> alreadyMap;
        Map<SortedPathStructure<TimePointPath>, BasicPair<Boolean, Integer>> waitingTimeSet;
        CompeteStructure tempCompeteStructure;
        Map<AnchorEntity, SortedPathStructure<TimePointPath>> singleFirstElevatorOccupationMap;
        for (int i = 0; i < maximumPathLength; ++i) {
            conflictMapWithSingleElevatorProposal = getConflictMapWithElevatorSingleProposalSet(temporalPathMap.values(), i);
//            if (!conflictMap.isEmpty()) {
//                System.out.println("haha");
//            }
            tempCompeteStructure = getWinnerAndFailureMatch(conflictMapWithSingleElevatorProposal, i);

//            singleFirstElevatorOccupationMap = tempCompeteStructure.getSingleFirstElevatorRequirementMap();
//            multipleMap = tempCompeteStructure.getMultipleFirstRequirementMap();
//            alreadyMap = tempCompeteStructure.getAlreadyOccupiedMap();
            // todo: 分别处理单个首次电梯请求、冲突
            /**
             * 要求getWaitingTimeSet和delayAndUpdate两个能合力处理下面情况
             *  1. 处理单个首次电梯请求
             *  2. 处理多个首次电梯请求冲突
             *  3. 处理一个早已占用电梯+多个首次电梯请求冲突
             *  4. 处理其他冲突
             */
            waitingTimeSet = getWaitingTimeSet(tempCompeteStructure, i);
            flag = delayAndUpdate(waitingTimeSet, i);
            if (!conflictMapWithSingleElevatorProposal.isEmpty()) {
                maximumPathLength = BasicFunctions.getMaximalTimeSlotLength(waitingTimeSet.keySet());
            }
            if (flag) {
                i = 0;
                Elevator.batchResetElevators(elevators);
            } else {
                Elevator.batchUpdateElevators(elevators);
            }
        }
        return maximumPathLength;
    }

    /**
     * 核心算法
     * @param timeGraphMap
     * @param robotList
     * @param job
     * @return
     */
    public static Match getPlanPath(Map<String, TimeWeightedGraph> timeGraphMap, List<Robot> robotList, Job job, Collection<Elevator> elevatorCollection, AnchorEntityConvertor convert) {
        Map<BasicPair<Task,Robot>, SortedPathStructure<TimePointPath>> result;
        job.initialTaskStartTimeAndEndTime();
        List<Task> taskList = job.getTaskList();
        Map<BasicPair<Task, Robot>, SortedPathStructure<AnchorPointPath>> matchMapBasicPair = taskAssignment(timeGraphMap, taskList, robotList, Constant.topKSize,  convert);
        BasicPair<Task, Robot> tempPair;
        SortedPathStructure<TimePointPath> pathStructure;
        MatchElement tempMatchElement;
        AnchorPointPath tempAnchorPointPath;
        TimePointPath tempTimePointPath;
        result = toTimePointSortedPath(matchMapBasicPair);
        eliminate(result, elevatorCollection);
        Match match = new Match();

        for (Map.Entry<BasicPair<Task, Robot>, SortedPathStructure<TimePointPath>> entry : result.entrySet()) {
            tempPair = entry.getKey();
            pathStructure = entry.getValue();
            tempAnchorPointPath = matchMapBasicPair.get(tempPair).getFirst();
            tempTimePointPath = pathStructure.getFirst();
            tempMatchElement = new MatchElement(tempPair.getKey(), tempPair.getValue(), tempAnchorPointPath, tempTimePointPath);
            match.add(tempMatchElement);
        }

        return match;
    }

}
