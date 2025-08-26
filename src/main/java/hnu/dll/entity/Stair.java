package hnu.dll.entity;

import hnu.dll.basic_entity.Location;

public class Stair extends Entity {

    private Location bottomLocation;
    private Location topLocation;
    private Location intermediatePointLocation;

    private Double segmentLength;

    public Stair(String name, Location bottomLocation, Location topLocation, Location intermediatePointLocation, Double segmentLength) {
        super(name);
        this.bottomLocation = bottomLocation;
        this.topLocation = topLocation;
        this.intermediatePointLocation = intermediatePointLocation;
        this.segmentLength = segmentLength;
    }
}
