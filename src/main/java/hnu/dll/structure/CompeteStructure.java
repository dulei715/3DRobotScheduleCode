package hnu.dll.structure;

import hnu.dll.entity.Elevator;
import hnu.dll.entity.Entity;
import hnu.dll.structure.basic_structure.BasicPair;
import hnu.dll.structure.path.TimePointPath;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CompeteStructure {
    private Map<AnchorEntity, SortedPathStructure<TimePointPath>> singleFirstElevatorOccupationMap;
    private Map<AnchorEntity, SortedPathStructure<TimePointPath>> winnerMap;
    private Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> failureMap;

    public CompeteStructure(Map<AnchorEntity, SortedPathStructure<TimePointPath>> singleFirstElevatorOccupationMap, Map<AnchorEntity, SortedPathStructure<TimePointPath>> winnerMap, Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> failureMap) {
        this.singleFirstElevatorOccupationMap = singleFirstElevatorOccupationMap;
        this.winnerMap = winnerMap;
        this.failureMap = failureMap;
    }

    public Map<AnchorEntity, SortedPathStructure<TimePointPath>> getSingleFirstElevatorOccupationMap() {
        return singleFirstElevatorOccupationMap;
    }

    public Map<AnchorEntity, SortedPathStructure<TimePointPath>> getWinnerMap() {
        return winnerMap;
    }

    public Map<AnchorEntity, Set<SortedPathStructure<TimePointPath>>> getFailureMap() {
        return failureMap;
    }
}