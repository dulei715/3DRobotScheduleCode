package hnu.dll.entity;

import hnu.dll.basic_entity.ThreeDLocation;


public abstract class Task extends Entity {

    private ThreeDLocation startThreeDLocation;
    private ThreeDLocation endThreeDLocation;
    private Double fetchTime;
    private Double sendOffTime;

    public Task(ThreeDLocation startThreeDLocation, ThreeDLocation endThreeDLocation, Double fetchTime, Double sendOffTime) {
        this.startThreeDLocation = startThreeDLocation;
        this.endThreeDLocation = endThreeDLocation;
        this.fetchTime = fetchTime;
        this.sendOffTime = sendOffTime;
    }

    public ThreeDLocation getStartLocation() {
        return startThreeDLocation;
    }

    public ThreeDLocation getEndLocation() {
        return endThreeDLocation;
    }

    public Double getFetchTime() {
        return fetchTime;
    }

    public Double getSendOffTime() {
        return sendOffTime;
    }
}
