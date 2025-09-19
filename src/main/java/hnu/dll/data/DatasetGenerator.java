package hnu.dll.data;

import hnu.dll.config.Constant;
import hnu.dll.entity.Robot;
import hnu.dll.structure.Building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatasetGenerator {
    public static List<Building> generateBuildings(Integer buildingSize, Integer defaultLayerSize) {
        List<Building> buildingList = new ArrayList<>(buildingSize);
        Building building;
        for (int i = 1; i <= buildingSize; ++i) {
            building = new Building("Building-"+i, defaultLayerSize, Constant.NeighboringLayersDistance);
            buildingList.add(building);
        }
        return buildingList;
    }
    public static Map<String, List<Robot>> generateRobotMap(Integer dogSize, Integer personSize) {
        Map<String, List<Robot>> map = new HashMap<>();
        Robot robot;
        List<Robot> tempRobotList = new ArrayList<>(dogSize);
        for (int i = 0; i < dogSize; ++i) {
            robot = new Robot("robot-dog-"+(i+1), Robot.DogRobotType, Constant.DogRobotPlaneVelocity, Constant.DogRobotStairVelocity, Constant.DogRobotCapacity);
            tempRobotList.add(robot);
        }
        map.put("robot-dog", tempRobotList);
        tempRobotList = new ArrayList<>(personSize);
        for (int i = 0; i < personSize; ++i) {
            robot = new Robot("robot-person-"+(i+1), Robot.PersonRobotType, Constant.PersonRobotPlaneVelocity, Constant.PersonRobotStairVelocity, Constant.PersonRobotCapacity);
            tempRobotList.add(robot);
        }
        map.put("robot-person", tempRobotList);
        return map;
    }
}
