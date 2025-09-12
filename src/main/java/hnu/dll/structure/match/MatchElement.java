package hnu.dll.structure.match;

import hnu.dll.entity.Robot;
import hnu.dll.entity.Task;
import hnu.dll.structure.path.AnchorPointPath;
import hnu.dll.structure.path.TimePointPath;

public class MatchElement {
    private Task task;
    private Robot robot;
    private AnchorPointPath anchorPointPath;
    private TimePointPath timePointPath;

    public MatchElement(Task task, Robot robot, AnchorPointPath anchorPointPath, TimePointPath timePointPath) {
        this.task = task;
        this.robot = robot;
        this.anchorPointPath = anchorPointPath;
        this.timePointPath = timePointPath;
    }

    public MatchElement() {
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public AnchorPointPath getAnchorPointPath() {
        return anchorPointPath;
    }

    public void setAnchorPointPath(AnchorPointPath anchorPointPath) {
        this.anchorPointPath = anchorPointPath;
    }

    public TimePointPath getTimePointPath() {
        return timePointPath;
    }

    public void setTimePointPath(TimePointPath timePointPath) {
        this.timePointPath = timePointPath;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(task.getName()).append(", ").append(robot.getName()).append(")");
        stringBuilder.append("{").append(anchorPointPath).append("}, ");
        stringBuilder.append("{").append(timePointPath).append("}");
        return stringBuilder.toString();
    }
}
