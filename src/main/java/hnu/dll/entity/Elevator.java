package hnu.dll.entity;

import hnu.dll.basic_entity.Location;
import hnu.dll.basic_entity.Status;

import java.util.Map;

public class Elevator extends Entity {

    private Double velocity;
    private Double openingOrCloseTimeCost;

    private Integer layer;

    public Elevator(String name, Double velocity, Double openingOrCloseTimeCost) {
        super(name);
        this.velocity = velocity;
        this.openingOrCloseTimeCost = openingOrCloseTimeCost;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public Double getVelocity() {
        return velocity;
    }

    public Double getOpeningOrCloseTimeCost() {
        return openingOrCloseTimeCost;
    }

    public Double getCrossFloorRunningTime(Integer startLayer, Integer endLayer, Double averageLayerHeight) {
        return Math.abs(endLayer - startLayer) * averageLayerHeight / this.velocity;
    }
}
