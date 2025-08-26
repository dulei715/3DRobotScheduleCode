package hnu.dll.entity;


public class Job extends Entity {
    private String id;
    private String name;

    private Double startTime;
    private Double endTime;

    public Job(String name, Double startTime, Double endTime) {
        super(name);
        this.startTime = startTime;
        this.endTime = endTime;
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

}
