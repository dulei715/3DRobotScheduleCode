package hnu.dll.entity;

import hnu.dll.basic_entity.location.ThreeDLocation;
import hnu.dll.structure.basic_structure.Anchor;

import java.util.*;


public class Robot extends Entity {

    public static final String DogRobotType = "0";
    public static final String PersonRobotType = "1";

    private String type;

    private Double flatGroundVelocity;
    private Double stairVelocity;
    private Double capacity;

    private Anchor anchorLocation;

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

    public Anchor getLocation() {
        return anchorLocation;
    }

    public void setLocation(Anchor anchor) {
        this.anchorLocation = anchor;
    }

    public static List<Robot> getRobotList(Map<String, List<Robot>> robotMapList) {
        List<Robot> result = new ArrayList<>();
        Collection<List<Robot>> values = robotMapList.values();
        for (List<Robot> list : values) {
            result.addAll(list);
        }
        return result;
    }

    public static Map<String, List<Robot>> getClassifiedRobotMap(List<Robot> robotList) {
        String type;
        Map<String, List<Robot>> result = new HashMap<>();
        List<Robot> tempList;
        for (Robot robot : robotList) {
            type = robot.getType();
            tempList = result.getOrDefault(type, new ArrayList<>());
            tempList.add(robot);
            result.put(type, tempList);
        }
        return result;
    }


    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robot robot = (Robot) o;
        return Objects.equals(type, robot.type) && Objects.equals(flatGroundVelocity, robot.flatGroundVelocity) && Objects.equals(stairVelocity, robot.stairVelocity) && Objects.equals(capacity, robot.capacity) && Objects.equals(anchorLocation, robot.anchorLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, flatGroundVelocity, stairVelocity, capacity, anchorLocation);
    }
}
