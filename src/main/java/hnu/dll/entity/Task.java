package hnu.dll.entity;

import hnu.dll.basic_entity.location.ThreeDLocation;
import hnu.dll.structure.basic_structure.Anchor;

import java.util.Objects;


public class Task extends Entity {

    private Anchor startAnchor;
    private Anchor endAnchor;
    private Double fetchTime;
    private Double sendOffTime;
    private Double occupyingSpace;

    private Double startTime;
    private Double endTime;

    public Task(String name, Anchor startAnchor, Anchor endAnchor, Double fetchTime, Double sendOffTime, Double occupyingSpace) {
        super(name);
        this.startAnchor = startAnchor;
        this.endAnchor = endAnchor;
        this.fetchTime = fetchTime;
        this.sendOffTime = sendOffTime;
        this.occupyingSpace = occupyingSpace;
    }

    public Anchor getStartAnchor() {
        return startAnchor;
    }

    public Anchor getEndAnchor() {
        return endAnchor;
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
        return Objects.equals(startAnchor, task.startAnchor) && Objects.equals(endAnchor, task.endAnchor) && Objects.equals(fetchTime, task.fetchTime) && Objects.equals(sendOffTime, task.sendOffTime) && Objects.equals(occupyingSpace, task.occupyingSpace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAnchor, endAnchor, fetchTime, sendOffTime, occupyingSpace);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getStartEndLocation() {
        return startAnchor + "-->" + endAnchor;
    }
}
