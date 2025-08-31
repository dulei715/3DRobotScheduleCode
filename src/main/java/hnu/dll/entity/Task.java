package hnu.dll.entity;

import hnu.dll.basic_entity.ThreeDLocation;

import java.util.Objects;


public abstract class Task extends Entity {

    private ThreeDLocation startLocation;
    private ThreeDLocation endLocation;
    private Double fetchTime;
    private Double sendOffTime;
    private Double occupyingSpace;

    private Double startTime;
    private Double endTime;

    public Task(String name, ThreeDLocation startLocation, ThreeDLocation endLocation, Double fetchTime, Double sendOffTime, Double occupyingSpace) {
        super(name);
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.fetchTime = fetchTime;
        this.sendOffTime = sendOffTime;
        this.occupyingSpace = occupyingSpace;
    }

    public ThreeDLocation getStartLocation() {
        return startLocation;
    }

    public ThreeDLocation getEndLocation() {
        return endLocation;
    }

    public Double getFetchTime() {
        return fetchTime;
    }

    public Double getSendOffTime() {
        return sendOffTime;
    }

    public Double getOccupyingSpace() {
        return occupyingSpace;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(startLocation, task.startLocation) && Objects.equals(endLocation, task.endLocation) && Objects.equals(fetchTime, task.fetchTime) && Objects.equals(sendOffTime, task.sendOffTime) && Objects.equals(occupyingSpace, task.occupyingSpace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startLocation, endLocation, fetchTime, sendOffTime, occupyingSpace);
    }
}
