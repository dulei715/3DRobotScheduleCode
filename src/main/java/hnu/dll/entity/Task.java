package hnu.dll.entity;

import hnu.dll.basic_entity.Location;


public abstract class Task extends Entity {

    private Location startLocation;
    private Location endLocation;
    private Double fetchTime;
    private Double sendOffTime;

    public Task(Location startLocation, Location endLocation, Double fetchTime, Double sendOffTime) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.fetchTime = fetchTime;
        this.sendOffTime = sendOffTime;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public Double getFetchTime() {
        return fetchTime;
    }

    public Double getSendOffTime() {
        return sendOffTime;
    }
}
