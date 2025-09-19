package hnu.dll.data;

import cn.edu.dll.basic.BasicCalculation;
import cn.edu.dll.basic.RandomUtil;
import hnu.dll.config.Constant;
import hnu.dll.entity.Entity;
import hnu.dll.entity.Robot;
import hnu.dll.structure.Building;
import hnu.dll.structure.basic_structure.Anchor;

import java.util.*;

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

    private static Anchor getRandomAnchor(Random random, Double lowerBound, Double upperBound, Integer defaultBuildingNumber, Integer defaultLayerNumber, int i, Double defaultZIndex) {
        Anchor tempAnchor;
        Double yDouble;
        Double tempDouble;
        Double xDouble;
        tempDouble = RandomUtil.getRandomDouble(lowerBound, upperBound, random);
        xDouble = BasicCalculation.getPrecisionValue(tempDouble, Constant.PrecisionSize);
        tempDouble = RandomUtil.getRandomDouble(lowerBound, upperBound, random);
        yDouble = BasicCalculation.getPrecisionValue(tempDouble, Constant.PrecisionSize);
        tempAnchor = new Anchor("A-"+ defaultBuildingNumber +"-"+ defaultLayerNumber +"-"+ i, xDouble, yDouble, defaultZIndex);
        return tempAnchor;
    }

    public static Map<String, Entity> generateBasicNormalAnchor(Integer anchorSize, Integer maxNeighboringSize, Random random, Double lowerBound, Double upperBound) {
        Set<Anchor> anchorSet = new HashSet<>(anchorSize);
        Anchor tempAnchor;
        Double defaultZIndex = 0D;
        Integer defaultBuildingNumber = 1;
        Integer defaultLayerNumber = 1;
        for (int i = 1; i <= anchorSize; ++i) {
            do {
                tempAnchor = getRandomAnchor(random, lowerBound, upperBound, defaultBuildingNumber, defaultLayerNumber, i, defaultZIndex);
            } while (anchorSet.contains(tempAnchor));
            anchorSet.add(tempAnchor);
        }
        List<Anchor> anchorList = new ArrayList<>(anchorSet);
        for (int i = 0; i < anchorSize; ++i) {
            
        }
    }
}
