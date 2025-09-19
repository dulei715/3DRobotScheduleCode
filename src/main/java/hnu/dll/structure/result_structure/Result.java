package hnu.dll.structure.result_structure;

public class Result {
    private Integer unitAnchorSize;
    private Long runningTime;
    private Integer robotSize;
    private Integer taskSize;
    private Double errorRatio;

    public Result(Integer unitAnchorSize, Long runningTime, Integer robotSize, Integer taskSize, Double errorRatio) {
        this.unitAnchorSize = unitAnchorSize;
        this.runningTime = runningTime;
        this.robotSize = robotSize;
        this.taskSize = taskSize;
        this.errorRatio = errorRatio;
    }

    public Integer getUnitAnchorSize() {
        return unitAnchorSize;
    }

    public void setUnitAnchorSize(Integer unitAnchorSize) {
        this.unitAnchorSize = unitAnchorSize;
    }

    public Long getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(Long runningTime) {
        this.runningTime = runningTime;
    }

    public Integer getRobotSize() {
        return robotSize;
    }

    public void setRobotSize(Integer robotSize) {
        this.robotSize = robotSize;
    }

    public Integer getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(Integer taskSize) {
        this.taskSize = taskSize;
    }

    public Double getErrorRatio() {
        return errorRatio;
    }

    public void setErrorRatio(Double errorRatio) {
        this.errorRatio = errorRatio;
    }

    @Override
    public String toString() {
        return "Result{" +
                "unitAnchorSize=" + unitAnchorSize +
                ", runningTime=" + runningTime +
                ", robotSize=" + robotSize +
                ", taskSize=" + taskSize +
                ", errorRatio=" + errorRatio +
                '}';
    }
}
