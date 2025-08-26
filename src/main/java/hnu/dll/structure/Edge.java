package hnu.dll.structure;

import hnu.dll.entity.Entity;

// used for time weighted graph
public class Edge {

    public static final Integer Normal_Type = 0;
    public static final Integer Elevator_Type = 1;
    public static final Integer Stair_Type = 2;

    private String edgeType;
    private Double weight;
    private Entity preEntity;
    private Entity nextEntity;

    public Edge(String edgeType, Double weight, Entity preEntity, Entity nextEntity) {
        this.edgeType = edgeType;
        this.weight = weight;
        this.preEntity = preEntity;
        this.nextEntity = nextEntity;
    }

    public String getEdgeType() {
        return edgeType;
    }

    public Double getWeight() {
        return weight;
    }

    public Entity getPreEntity() {
        return preEntity;
    }

    public Entity getNextEntity() {
        return nextEntity;
    }
}
