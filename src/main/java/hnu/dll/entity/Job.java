package hnu.dll.entity;


import java.util.List;

public class Job extends Entity {
    private String id;
    private String name;

    private Double startTime;
    private Double endTime;

    private List<Task> taskList;

    public Job(String name, Double startTime, Double endTime, List<Task> taskList) {
        super(name);
        this.startTime = startTime;
        this.endTime = endTime;
        this.taskList = taskList;
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

    public void initialTaskStartTimeAndEndTime() {
        for (Task task : this.taskList) {
            task.setStartTime(this.startTime);
            task.setEndTime(this.endTime);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
