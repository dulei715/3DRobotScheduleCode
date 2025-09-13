package hnu.dll.structure;

import hnu.dll.entity.Elevator;
import hnu.dll.entity.Entity;
import hnu.dll.structure.basic_structure.BasicPair;
import hnu.dll.structure.path.TimePointPath;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CompeteStructure {
    /**
     * 要记录如下内容：
     * 1. 单个首次访问电梯的路径
     * 2. 多个首次访问实体的路径（胜利者和失败者）
     * 3. 一个已占用实体+多个首次访问实体 的路径（已占用的为胜利者，其他的为失败者）
     */
    private Map<AnchorEntity, SortedPathStructure<TimePointPath>> singleFirstElevatorRequirementMap;
    private Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> multipleFirstRequirementMap;
    private Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> alreadyOccupiedMap;

    public CompeteStructure(Map<AnchorEntity, SortedPathStructure<TimePointPath>> singleFirstElevatorRequirementMap, Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> multipleFirstRequirementMap, Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> alreadyOccupiedMap) {
        this.singleFirstElevatorRequirementMap = singleFirstElevatorRequirementMap;
        this.multipleFirstRequirementMap = multipleFirstRequirementMap;
        this.alreadyOccupiedMap = alreadyOccupiedMap;
    }

    public Map<AnchorEntity, SortedPathStructure<TimePointPath>> getSingleFirstElevatorRequirementMap() {
        return singleFirstElevatorRequirementMap;
    }

    public Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> getMultipleFirstRequirementMap() {
        return multipleFirstRequirementMap;
    }

    public Map<AnchorEntity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> getAlreadyOccupiedMap() {
        return alreadyOccupiedMap;
    }
}