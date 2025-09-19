package hnu.dll.data;

import cn.edu.dll.basic.BasicCalculation;
import cn.edu.dll.basic.RandomUtil;
import hnu.dll.basic_entity.location.Location;
import hnu.dll.basic_entity.location.PlaneLocation;
import hnu.dll.basic_entity.location.ThreeDLocation;
import hnu.dll.config.Constant;
import hnu.dll.entity.Elevator;
import hnu.dll.entity.Robot;
import hnu.dll.entity.Stair;
import hnu.dll.structure.Building;
import hnu.dll.structure.DatasetStructure;
import hnu.dll.structure.basic_structure.Anchor;
import hnu.dll.structure.graph.SimpleGraph;

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

    private static Location getRandomLocation(Random random, Double lowerBound, Double upperBound, Double defaultZIndex) {
        Double yDouble;
        Double tempDouble;
        Double xDouble;
        tempDouble = RandomUtil.getRandomDouble(lowerBound, upperBound, random);
        xDouble = BasicCalculation.getPrecisionValue(tempDouble, Constant.PrecisionSize);
        tempDouble = RandomUtil.getRandomDouble(lowerBound, upperBound, random);
        yDouble = BasicCalculation.getPrecisionValue(tempDouble, Constant.PrecisionSize);
        return new ThreeDLocation(xDouble, yDouble, defaultZIndex);
    }
    private static Location getRandomLocation(Random random, Double lowerBound, Double upperBound) {
        Double yDouble;
        Double tempDouble;
        Double xDouble;
        tempDouble = RandomUtil.getRandomDouble(lowerBound, upperBound, random);
        xDouble = BasicCalculation.getPrecisionValue(tempDouble, Constant.PrecisionSize);
        tempDouble = RandomUtil.getRandomDouble(lowerBound, upperBound, random);
        yDouble = BasicCalculation.getPrecisionValue(tempDouble, Constant.PrecisionSize);
        return new PlaneLocation(xDouble, yDouble);
    }
//    private static Anchor getRandomAnchor(Random random, Double lowerBound, Double upperBound, Integer defaultBuildingNumber, Integer defaultLayerNumber, int i, Double defaultZIndex) {
//        Anchor tempAnchor;
//        PlaneLocation planeLocation = (PlaneLocation) getRandomLocation(random, lowerBound, upperBound);
//        tempAnchor = new Anchor("A-"+ defaultBuildingNumber +"-"+ defaultLayerNumber +"-"+ i, planeLocation, defaultZIndex);
//        return tempAnchor;
//    }

    private static Anchor generateAnchor(PlaneLocation planeLocation, Integer defaultBuildingNumber, Integer layer, int i, Double zIndex) {
        Anchor tempAnchor;
        tempAnchor = new Anchor("A-"+ defaultBuildingNumber +"-"+ layer +"-"+ i, planeLocation, zIndex);
        return tempAnchor;
    }
    private static Elevator generateElevator(PlaneLocation planeLocation, Building building, int i) {
        Elevator tempElevator;
        tempElevator = new Elevator("L-" + building.getBuildingNumber() + "-" + i, building, Constant.ElevatorAverageVelocity, Constant.OpenOrCloseDoorTimeCost, planeLocation, Constant.DefaultElevatorStartLayer);
        return tempElevator;
    }
    private static Stair generateStair(PlaneLocation planeLocation, Building building, int i, Double startZIndex) {
        Stair tempStair;
        Integer layerSize = building.getLayerSize();
        Double neighboringLayerHeight = building.getAverageLayerHeight();
        List<ThreeDLocation> defaultInnerNodeList = Stair.getDefaultInnerNodeList(planeLocation, startZIndex, neighboringLayerHeight, layerSize);
        Double segmentSize = neighboringLayerHeight / 2 / Math.sin(Constant.DefaultStairAngle);
        tempStair = new Stair("S-" + building.getBuildingNumber() +"-"+ i, building, planeLocation, defaultInnerNodeList, segmentSize);
        return tempStair;
    }

    public static DatasetStructure generateBasicNormalAnchor(Integer anchorSize, Integer maxHalfNeighboringSize, Integer elevatorSize, Integer stairSize, Building building, Random random, Double positionLowerBound, Double positionUpperBound, Double neighboringDistanceLowerBound, Double neighboringDistanceUpperBound) {
        Set<PlaneLocation> planeLocationSet = new HashSet<>();
        SimpleGraph simpleGraph = new SimpleGraph();
        PlaneLocation tempPlaneLocation;
        Anchor tempAnchor;
        Elevator tempElevator;
        Stair tempStair;
        List<Anchor> anchorList;
        List<Elevator> elevatorList;
        List<Stair> stairList;
        Integer defaultBuildingNumber = building.getBuildingNumber();
        Double defaultZIndex = 0D;
        Double tempDistance;
        Integer defaultLayerNumber = 1;
        Integer tempAnchorIndex;
        // 生成一般锚点
        anchorList = new ArrayList<>(anchorSize);
        for (int i = 1; i <= anchorSize; ++i) {
            do {
//                tempAnchor = getRandomAnchor(random, lowerBound, upperBound, defaultBuildingNumber, defaultLayerNumber, i, defaultZIndex);
                tempPlaneLocation = (PlaneLocation) getRandomLocation(random, positionLowerBound, positionUpperBound);
            } while (planeLocationSet.contains(tempPlaneLocation));
            planeLocationSet.add(tempPlaneLocation);
            tempAnchor = generateAnchor(tempPlaneLocation, defaultBuildingNumber, defaultLayerNumber, i, defaultZIndex);
            anchorList.add(tempAnchor);
        }

        // todo

        // 生成电梯
        elevatorList = new ArrayList<>();
        for (int i = 1; i <= elevatorSize; ++i) {
            do {
                tempPlaneLocation = (PlaneLocation) getRandomLocation(random, positionLowerBound, positionUpperBound);
            } while (planeLocationSet.contains(tempPlaneLocation));
            planeLocationSet.add(tempPlaneLocation);
            tempElevator = generateElevator(tempPlaneLocation, building, i);
            elevatorList.add(tempElevator);
        }

        // 生成楼梯
        stairList = new ArrayList<>();
        for (int i = 1; i <= stairSize; ++i) {
            do {
                tempPlaneLocation = (PlaneLocation) getRandomLocation(random, positionLowerBound, positionUpperBound);
            } while (planeLocationSet.contains(tempPlaneLocation));
            planeLocationSet.add(tempPlaneLocation);
            tempStair = generateStair(tempPlaneLocation, building, i, defaultZIndex);
            stairList.add(tempStair);
        }

        // 构建一般锚点内部点距离
        for (int i = 0; i < anchorSize; ++i) {
            tempAnchor = anchorList.get(i);
            for (int j = i + 1, k = 0; j < anchorSize && k < maxHalfNeighboringSize; ++j, ++k) {
                 Anchor anotherAnchor = anchorList.get(j);
                tempDistance = RandomUtil.getRandomDouble(neighboringDistanceLowerBound, neighboringDistanceUpperBound, random);
                simpleGraph.addElement(tempAnchor, anotherAnchor, tempDistance);
                simpleGraph.addElement(anotherAnchor, tempAnchor, tempDistance);
            }
        }


        // 构建电梯和一般锚点之间的距离
        List<Integer> elevatorRelativeAnchorIndex = RandomUtil.getRandomIntegerList(0, anchorSize - 1, elevatorSize, random);
        for (int i = 0; i < elevatorSize; ++i) {
            tempAnchorIndex = elevatorRelativeAnchorIndex.get(i);
            tempAnchor = anchorList.get(tempAnchorIndex);
            tempElevator = elevatorList.get(i);
            tempDistance = RandomUtil.getRandomDouble(neighboringDistanceLowerBound, neighboringDistanceUpperBound, random);
            simpleGraph.addElement(tempAnchor, tempElevator, tempDistance);
            simpleGraph.addElement(tempElevator, tempAnchor, tempDistance);
        }

        // 构建楼梯和一般锚点之间的距离
        List<Integer> stairRelativeAnchorIndex = RandomUtil.getRandomIntegerList(0, anchorSize - 1, stairSize, random);
        for (int i = 0; i < stairSize; ++i) {
            tempAnchorIndex = stairRelativeAnchorIndex.get(i);
            tempAnchor = anchorList.get(tempAnchorIndex);
            tempStair = stairList.get(i);
            tempDistance = RandomUtil.getRandomDouble(neighboringDistanceLowerBound, neighboringDistanceUpperBound, random);
            simpleGraph.addElement(tempAnchor, tempStair, tempDistance);
            simpleGraph.addElement(tempStair, tempAnchor, tempDistance);
        }


        return new DatasetStructure(simpleGraph, anchorList, elevatorList, stairList, planeLocationSet);
    }
}
