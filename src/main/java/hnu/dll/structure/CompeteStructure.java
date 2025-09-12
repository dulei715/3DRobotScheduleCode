package hnu.dll.structure;

import hnu.dll.entity.Elevator;
import hnu.dll.entity.Entity;
import hnu.dll.structure.basic_structure.BasicPair;
import hnu.dll.structure.path.TimePointPath;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CompeteStructure {
    private Map<Elevator, SortedPathStructure<TimePointPath>> singleFirstElevatorOccupationMap;
    private Map<Elevator, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> winAndFailureElevatorPathStructureMap;
    private Map<Entity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> winAndFailureEntityPathStructureMap;

    public CompeteStructure() {
        this.singleFirstElevatorOccupationMap = new HashMap<>();
        this.winAndFailureElevatorPathStructureMap = new HashMap<>();
        this.winAndFailureEntityPathStructureMap = new HashMap<>();
    }

    public CompeteStructure(Map<Elevator, SortedPathStructure<TimePointPath>> singleFirstElevatorOccupationMap, Map<Elevator, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> winAndFailureElevatorPathStructureMap, Map<Entity, BasicPair<SortedPathStructure<TimePointPath>, Set<SortedPathStructure<TimePointPath>>>> winANdFailureEntityMap) {
        this.singleFirstElevatorOccupationMap = singleFirstElevatorOccupationMap;
        this.winAndFailureElevatorPathStructureMap = winAndFailureElevatorPathStructureMap;
        this.winAndFailureEntityPathStructureMap = winANdFailureEntityMap;
    }

}
