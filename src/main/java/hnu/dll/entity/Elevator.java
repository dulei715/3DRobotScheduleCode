package hnu.dll.entity;

import hnu.dll.basic_entity.location.PlaneLocation;

public class Elevator extends Entity {

    private Double velocity;
    private Double openingOrCloseTimeCost;

    private Integer layerSize;
    private PlaneLocation planeLocation;

    private Integer currentLayer;



    public Double getCrossFloorRunningTime(Integer startLayer, Integer endLayer, Double averageLayerHeight) {
        return Math.abs(endLayer - startLayer) * averageLayerHeight / this.velocity;
    }


    public Elevator(String name, Double velocity, Double openingOrCloseTimeCost, Integer layerSize, PlaneLocation planeLocation) {
        super(name);
        this.velocity = velocity;
        this.openingOrCloseTimeCost = openingOrCloseTimeCost;
        this.layerSize = layerSize;
        this.planeLocation = planeLocation;
    }

    public void setCurrentLayer(Integer currentLayer) {
        this.currentLayer = currentLayer;
    }

    public Double getVelocity() {
        return velocity;
    }

    public Double getOpeningOrCloseTimeCost() {
        return openingOrCloseTimeCost;
    }

    public Integer getLayerSize() {
        return layerSize;
    }

    public PlaneLocation getPlaneLocation() {
        return planeLocation;
    }

    public Integer getCurrentLayer() {
        return currentLayer;
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "name='" + name +
                ", planeLocation=" + planeLocation +
                ", currentLayer=" + currentLayer + '\'' +
                '}';
    }
//    public String toString() {
//        return "Elevator{" +
//                "velocity=" + velocity +
//                ", openingOrCloseTimeCost=" + openingOrCloseTimeCost +
//                ", layerSize=" + layerSize +
//                ", planeLocation=" + planeLocation +
//                ", currentLayer=" + currentLayer +
//                ", id='" + id + '\'' +
//                ", name='" + name + '\'' +
//                '}';
//    }
}
