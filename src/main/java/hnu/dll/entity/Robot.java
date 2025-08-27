package hnu.dll.entity;

import hnu.dll.basic_entity.ThreeDLocation;


public class Robot extends Entity {

    private String type;

    private Double flatGroundVelocity;
    private Double stairVelocity;
    private Double capacity;

    private ThreeDLocation threeDLocation;

    public Robot(String name, String type, Double flatGroundVelocity, Double stairVelocity, Double capacity) {
        super(name);
        this.name = name;
        this.type = type;
        this.flatGroundVelocity = flatGroundVelocity;
        this.stairVelocity = stairVelocity;
        this.capacity = capacity;
    }

    public Double getFlatGroundVelocity() {
        return flatGroundVelocity;
    }

    public void setFlatGroundVelocity(Double flatGroundVelocity) {
        this.flatGroundVelocity = flatGroundVelocity;
    }

    public Double getStairVelocity() {
        return stairVelocity;
    }

    public void setStairVelocity(Double stairVelocity) {
        this.stairVelocity = stairVelocity;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public ThreeDLocation getLocation() {
        return threeDLocation;
    }

    public void setLocation(ThreeDLocation threeDLocation) {
        this.threeDLocation = threeDLocation;
    }


    public String getType() {
        return type;
    }
}
