package hnu.dll.structure;

import hnu.dll.basic_entity.location.Location;
import hnu.dll.basic_entity.location.PlaneLocation;
import hnu.dll.entity.Elevator;
import hnu.dll.entity.Entity;
import hnu.dll.entity.Stair;
import hnu.dll.structure.basic_structure.Anchor;
import hnu.dll.structure.graph.SimpleGraph;

import java.util.*;

public class DatasetStructure {
    private SimpleGraph simpleGraph;
    private List<Anchor> anchorList;
    private List<Elevator> elevatorList;
    private List<Stair> stairList;
    private Set<PlaneLocation> planeLocationSet;

    public DatasetStructure(SimpleGraph simpleGraph, List<Anchor> anchorList, List<Elevator> elevatorList, List<Stair> stairList, Set<PlaneLocation> planeLocationSet) {
        this.simpleGraph = simpleGraph;
        this.anchorList = anchorList;
        this.elevatorList = elevatorList;
        this.stairList = stairList;
        this.planeLocationSet = planeLocationSet;
    }

    public SimpleGraph getSimpleGraph() {
        return simpleGraph;
    }

    public List<Anchor> getAnchorList() {
        return anchorList;
    }

    public List<Elevator> getElevatorList() {
        return elevatorList;
    }

    public List<Stair> getStairList() {
        return stairList;
    }

    public Set<PlaneLocation> getPlaneLocationSet() {
        return planeLocationSet;
    }

    public void combine(DatasetStructure datasetStructure) {
        this.simpleGraph.combine(datasetStructure.getSimpleGraph());

        LinkedHashSet<Anchor> anchorSet = new LinkedHashSet<>();
        anchorSet.addAll(this.anchorList);
        anchorSet.addAll(datasetStructure.anchorList);
        this.anchorList = new ArrayList<>(anchorSet);

        LinkedHashSet<Elevator> elevatorSet = new LinkedHashSet<>();
        elevatorSet.addAll(this.elevatorList);
        elevatorSet.addAll(datasetStructure.elevatorList);
        this.elevatorList = new ArrayList<>(elevatorSet);

        LinkedHashSet<Stair> stairSet = new LinkedHashSet<>();
        stairSet.addAll(this.stairList);
        stairSet.addAll(datasetStructure.stairList);
        this.stairList = new ArrayList<>(stairSet);

        this.planeLocationSet.addAll(datasetStructure.planeLocationSet);


    }
}
