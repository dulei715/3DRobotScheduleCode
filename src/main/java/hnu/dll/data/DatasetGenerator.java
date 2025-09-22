package hnu.dll.data;

import cn.edu.dll.basic.BasicCalculation;
import cn.edu.dll.basic.RandomUtil;
import cn.edu.dll.io.print.MyPrint;
import hnu.dll.basic_entity.location.Location;
import hnu.dll.basic_entity.location.PlaneLocation;
import hnu.dll.basic_entity.location.ThreeDLocation;
import hnu.dll.config.Constant;
import hnu.dll.entity.*;
import hnu.dll.structure.Building;
import hnu.dll.structure.DatasetStructure;
import hnu.dll.structure.basic_structure.Anchor;
import hnu.dll.structure.graph.SimpleGraph;

import java.util.*;

public class DatasetGenerator {
    public static Map<String, Entity> extractEntityMap(DatasetStructure datasetStructure) {
        Map<String, Entity> map = new HashMap<>();
        List<Anchor> anchorList = datasetStructure.getAnchorList();
        List<Elevator> elevatorList = datasetStructure.getElevatorList();
        List<Stair> stairList = datasetStructure.getStairList();
        for (Anchor anchor : anchorList) {
            map.put(anchor.getName(), anchor);
        }
        for (Elevator elevator : elevatorList) {
            map.put(elevator.getName(), elevator);
        }
        for (Stair stair : stairList) {
            map.put(stair.getName(), stair);
        }
        return map;
    }
    public static List<Building> generateBuildings(Integer buildingSize, Integer layerSizeLowerBound, Integer layerSizeUpperBound, Random random) {
        List<Building> buildingList = new ArrayList<>(buildingSize);
        Building building;
        Integer layerSize;
        for (int i = 1; i <= buildingSize; ++i) {
            layerSize = RandomUtil.getRandomInteger(layerSizeLowerBound, layerSizeUpperBound, random);
            building = new Building("Building-"+i, layerSize, Constant.NeighboringLayersDistance);
            buildingList.add(building);
        }
        return buildingList;
    }
    public static Map<String, List<Robot>> generateRobotMap(Integer dogSize, Integer personSize, List<Anchor> candidateLocationAnchorList, List<Integer> dogIndexList, List<Integer> personIndexList) {
        Map<String, List<Robot>> map = new HashMap<>();
        Robot robot;
        List<Robot> tempRobotList = new ArrayList<>(dogSize);
        int k = 0;
        for (int i = 0; i < dogSize; ++i) {
            robot = new Robot("robot-dog-"+(i+1), Robot.DogRobotType, Constant.DogRobotPlaneVelocity, Constant.DogRobotStairVelocity, Constant.DogRobotCapacity);
            robot.setLocation(candidateLocationAnchorList.get(dogIndexList.get(k++)));
            tempRobotList.add(robot);
        }
        k = 0;
        map.put(Robot.DogRobotType, tempRobotList);
        tempRobotList = new ArrayList<>(personSize);
        for (int i = 0; i < personSize; ++i) {
            robot = new Robot("robot-person-"+(i+1), Robot.PersonRobotType, Constant.PersonRobotPlaneVelocity, Constant.PersonRobotStairVelocity, Constant.PersonRobotCapacity);
            robot.setLocation(candidateLocationAnchorList.get(personIndexList.get(k++)));
            tempRobotList.add(robot);
        }
        map.put(Robot.PersonRobotType, tempRobotList);
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
        tempStair = new Stair("S-" + building.getBuildingNumber() +"-"+ i, building, planeLocation);
        return tempStair;
    }

    public static DatasetStructure generateBasicNormalEntity(Integer anchorSize, Integer maxHalfNeighboringSize, Integer elevatorSize, Integer stairSize, Building building, Random random, Double positionLowerBound, Double positionUpperBound, Double neighboringDistanceLowerBound, Double neighboringDistanceUpperBound) {
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
                tempDistance = BasicCalculation.getPrecisionValue(RandomUtil.getRandomDouble(neighboringDistanceLowerBound, neighboringDistanceUpperBound, random), Constant.PrecisionSize);
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
            tempDistance = BasicCalculation.getPrecisionValue(RandomUtil.getRandomDouble(neighboringDistanceLowerBound, neighboringDistanceUpperBound, random), Constant.PrecisionSize);
            simpleGraph.addElement(tempAnchor, tempElevator, tempDistance);
            simpleGraph.addElement(tempElevator, tempAnchor, tempDistance);
        }

        // 构建楼梯和一般锚点之间的距离
        List<Integer> stairRelativeAnchorIndex = RandomUtil.getRandomIntegerList(0, anchorSize - 1, stairSize, random);
        for (int i = 0; i < stairSize; ++i) {
            tempAnchorIndex = stairRelativeAnchorIndex.get(i);
            tempAnchor = anchorList.get(tempAnchorIndex);
            tempStair = stairList.get(i);
            tempDistance = BasicCalculation.getPrecisionValue(RandomUtil.getRandomDouble(neighboringDistanceLowerBound, neighboringDistanceUpperBound, random), Constant.PrecisionSize);
            simpleGraph.addElement(tempAnchor, tempStair, tempDistance);
            simpleGraph.addElement(tempStair, tempAnchor, tempDistance);
        }

        return new DatasetStructure(simpleGraph, anchorList, elevatorList, stairList, planeLocationSet);
    }

    private static Entity searchNewEntity(Entity oldEntity, List<Anchor> oldAnchorList, List<Elevator> oldElevatorList, List<Stair> oldStairList, List<Anchor> newAnchorList, List<Elevator> newElevatorList, List<Stair> newStairList) {
        Integer index;
        Entity resultEntity;
        if (oldEntity instanceof Anchor) {
            Anchor oldAnchor = (Anchor) oldEntity;
            index = oldAnchorList.indexOf(oldAnchor);
            resultEntity =  newAnchorList.get(index);
        } else if (oldEntity instanceof Elevator) {
            Elevator oldElevator = (Elevator) oldEntity;
            index = oldElevatorList.indexOf(oldElevator);
            resultEntity = newElevatorList.get(index);
        } else if (oldEntity instanceof Stair) {
            Stair oldStair = (Stair) oldEntity;
            index = oldStairList.indexOf(oldStair);
            resultEntity = newStairList.get(index);
        } else {
            throw new RuntimeException("不支持的entity类！");
        }
        return resultEntity;
    }


    private static DatasetStructure generateNewBasicGraph(DatasetStructure modelStructure, Double xBias, Double yBias, Building newBuilding) {
        SimpleGraph simpleGraph = modelStructure.getSimpleGraph();
        List<Anchor> anchorList = modelStructure.getAnchorList();
        List<Elevator> elevatorList = modelStructure.getElevatorList();
        List<Stair> stairList = modelStructure.getStairList();
        Set<PlaneLocation> planeLocationSet = modelStructure.getPlaneLocationSet();

        List<Anchor> newAnchorList = new ArrayList<>();
        List<Elevator> newElevatorList = new ArrayList<>();
        List<Stair> newStairList = new ArrayList<>();
        Set<PlaneLocation> newPlaneLocationSet = new HashSet<>();
        Anchor newAnchor;
        Elevator newElevator;
        Stair newStair;
        PlaneLocation newPlaneLocation;

        Integer buildingNumber = newBuilding.getBuildingNumber();

        for (Anchor oldAnchor : anchorList) {
            String oldName = oldAnchor.getName();
            String newName = oldName.replaceFirst("-(\\d+)-", "-".concat(buildingNumber.toString()).concat("-"));
            ThreeDLocation oldLocation = (ThreeDLocation) oldAnchor.getLocation();
            newAnchor = new Anchor(newName, oldLocation.generateNewThreeDLocationWithBias(xBias, yBias, 0D));
            newAnchorList.add(newAnchor);
        }

        for (Elevator oldElevator : elevatorList) {
            String oldName = oldElevator.getName();
            String newName = oldName.replaceFirst("-(\\d+)-", "-".concat(buildingNumber.toString()).concat("-"));
            PlaneLocation oldPlaneLocation = oldElevator.getPlaneLocation();
            newPlaneLocation = oldPlaneLocation.generateNewPlaneLocationWithBias(xBias, yBias);
            newElevator = new Elevator(newName, newBuilding, oldElevator.getVelocity(), oldElevator.getOpeningOrCloseTimeCost(), newPlaneLocation, oldElevator.getOriginalStartLayer());
            newElevatorList.add(newElevator);
        }

        for (Stair oldStair : stairList) {
            String oldName = oldStair.getName();
            String newName = oldName.replaceFirst("-(\\d+)-", "-".concat(buildingNumber.toString()).concat("-"));
            PlaneLocation oldPlaneLocation = oldStair.getPlaneLocation();
            newPlaneLocation = oldPlaneLocation.generateNewPlaneLocationWithBias(xBias, yBias);
            newStair = new Stair(newName, newBuilding, newPlaneLocation);
            newStairList.add(newStair);
        }

        for (PlaneLocation oldPlaneLocation : planeLocationSet) {
            newPlaneLocationSet.add(oldPlaneLocation.generateNewPlaneLocationWithBias(xBias, yBias));
        }



        SimpleGraph newSimpleGraph = new SimpleGraph();
        Entity oldPreEntity, oldNextEntity, newPreEntity, newNextEntity;
        Map<Entity, Double> tempValue;
        Double tempWeight;
        for (Map.Entry<Entity, Map<Entity, Double>> entry : simpleGraph.getGraphTable().entrySet()) {
            oldPreEntity = entry.getKey();
            newPreEntity = searchNewEntity(oldPreEntity, anchorList, elevatorList, stairList, newAnchorList, newElevatorList, newStairList);
            tempValue = entry.getValue();
            for (Map.Entry<Entity, Double> innerEntry : tempValue.entrySet()) {
                oldNextEntity = innerEntry.getKey();
                newNextEntity = searchNewEntity(oldNextEntity, anchorList, elevatorList, stairList, newAnchorList, newElevatorList, newStairList);
                tempWeight = innerEntry.getValue();
                newSimpleGraph.addElement(newPreEntity, newNextEntity, tempWeight);
            }
        }
        return new DatasetStructure(newSimpleGraph, newAnchorList, newElevatorList, newStairList, newPlaneLocationSet);
    }

    public static void extendDataStructureAlongZDirect(DatasetStructure basicDatasetStructure, Building building) {
        Integer layerSize = building.getLayerSize();
        Double averageLayerHeight = building.getAverageLayerHeight();
        SimpleGraph basicSimpleGraph = basicDatasetStructure.getSimpleGraph();
        List<Anchor> basicAnchorList = basicDatasetStructure.getAnchorList();
        List<Elevator> elevatorList = basicDatasetStructure.getElevatorList();
        List<Stair> stairList = basicDatasetStructure.getStairList();
        Set<PlaneLocation> planeLocationSet = basicDatasetStructure.getPlaneLocationSet();
        List<Anchor> tempNewAnchorList;
        Anchor tempNewAnchor;
        Entity oldPreEntity, oldNextEntity, newPreEntity, newNextEntity;
        Map<Entity, Double> tempValue;
        Double tempWeight;
        SimpleGraph tempNewSimpleGraph;
        List<List<Anchor>> anchorListList = new ArrayList<>();
        List<SimpleGraph> simpleGraphList = new ArrayList<>(layerSize);

        anchorListList.add(basicAnchorList);
        simpleGraphList.add(basicSimpleGraph);
        for (int i = 1; i < layerSize; ++i) {
            tempNewAnchorList = new ArrayList<>();
            for (Anchor basicAnchor : basicAnchorList) {
                String oldName = basicAnchor.getName();
                String newName = oldName.replaceFirst("-(\\d+)-(\\d+)-", "-$1-" + (i+1) + "-");
                ThreeDLocation oldLocation = (ThreeDLocation) basicAnchor.getLocation();
                tempNewAnchor = new Anchor(newName, oldLocation.generateNewThreeDLocationWithBias(0D, 0D, averageLayerHeight * i));
                tempNewAnchorList.add(tempNewAnchor);
            }
            anchorListList.add(tempNewAnchorList);
            tempNewSimpleGraph = new SimpleGraph();
            for (Map.Entry<Entity, Map<Entity, Double>> entry : basicSimpleGraph.getGraphTable().entrySet()) {
                oldPreEntity = entry.getKey();
                newPreEntity = searchNewEntity(oldPreEntity, basicAnchorList, elevatorList, stairList, tempNewAnchorList, elevatorList, stairList);
                tempValue = entry.getValue();
                for (Map.Entry<Entity, Double> innerEntry : tempValue.entrySet()) {
                    oldNextEntity = innerEntry.getKey();
                    newNextEntity = searchNewEntity(oldNextEntity, basicAnchorList, elevatorList, stairList, tempNewAnchorList, elevatorList, stairList);
                    tempWeight = innerEntry.getValue();
                    tempNewSimpleGraph.addElement(newPreEntity, newNextEntity, tempWeight);
                }
            }
            simpleGraphList.add(tempNewSimpleGraph);
        }

        // 开始合并
        for (int i = 1; i < layerSize; ++i) {
            tempNewAnchorList = anchorListList.get(i);
            basicAnchorList.addAll(tempNewAnchorList);
            tempNewSimpleGraph = simpleGraphList.get(i);
            basicSimpleGraph.combine(tempNewSimpleGraph);
        }
    }



    public static DatasetStructure generateTotalSimpleGraph(DatasetStructure modelSimpleGraphStructure, Integer buildingSize, Integer buildingLayerSizeLowerBound, Integer buildingLayerSizeUpperBound, Random random, Double neighboringDistanceLowerBound, Double neighboringDistanceUpperBound) {
        // 扩展默认的
        List<Building> buildingList = generateBuildings(buildingSize, buildingLayerSizeLowerBound, buildingLayerSizeUpperBound, random);
        List<Anchor> basicAnchorList = modelSimpleGraphStructure.getAnchorList();
        /** 规定大楼之间形成一条路径，即任意一个大楼最多和两个大楼相连，并且所有大楼都是连通的
         * 因此需要随机选出每个大楼连接其他大楼的结点(这里用AnchorList的下标标识，因为选出的结点不能是电梯或者楼梯，而且规定不能重复)
         */
        int basicAnchorSize = basicAnchorList.size();
        int[] tempRandomIntegerArray;
        List<List<Integer>> indexListList = new ArrayList<>(buildingSize);
        List<Integer> tempList = new ArrayList<>(1);
        tempList.add(RandomUtil.getRandomInteger(0, basicAnchorSize - 1, random));
        indexListList.add(tempList);
        int samplingSize = 2;
        for (int i = 0; i < buildingSize - 1; ++i) {
            tempList = new ArrayList<>(samplingSize);
            tempRandomIntegerArray = RandomUtil.getRandomIntArrayWithoutRepeat(0, basicAnchorSize - 1, samplingSize, random);
            for (int j = 0; j < samplingSize; ++j) {
                tempList.add(tempRandomIntegerArray[j]);
            }
            indexListList.add(tempList);
        }
        tempList = new ArrayList<>(1);
        tempList.add(RandomUtil.getRandomInteger(0, basicAnchorSize - 1, random));
        indexListList.add(tempList);

        List<DatasetStructure> dataStructureList = new ArrayList<>(buildingSize);
        Building tempNewBuilding;
        DatasetStructure tempDatasetStructure;
        for (int i = 0; i < buildingSize; ++i) {
            tempNewBuilding = buildingList.get(i);
            tempDatasetStructure = generateNewBasicGraph(modelSimpleGraphStructure, Constant.buildingXBias * i, Constant.buildingYBias * i, tempNewBuilding);
            extendDataStructureAlongZDirect(tempDatasetStructure, tempNewBuilding);
            dataStructureList.add(tempDatasetStructure);
        }

        /**
         * 等构建完所有的simpleGraph后，利用indexListList需要构建大楼之间的path weight （默认楼与楼之间没有多余的锚点）
         */
        DatasetStructure beforeStructure;
        DatasetStructure currentStructure = dataStructureList.get(0);
        DatasetStructure nextStructure = dataStructureList.get(1);
        DatasetStructure finalStructure = currentStructure;
        SimpleGraph finalSimpleGraph = finalStructure.getSimpleGraph();

        Anchor beforeAnchorB, currentAnchorA;
        Anchor currentAnchorB = currentStructure.getAnchorList().get(indexListList.get(0).get(0));
        Anchor nextAnchorA = nextStructure.getAnchorList().get(indexListList.get(1).get(0));
        Double randomWeight = RandomUtil.getRandomDouble(neighboringDistanceLowerBound, neighboringDistanceUpperBound, random);
        finalSimpleGraph.addElement(currentAnchorB, nextAnchorA, randomWeight);
//        System.out.println(currentAnchorB + " 与 " + nextAnchorA + " 相连");
        List<Integer> beforeIndexList, currentIndexList, nextIndexList;
        for (int i = 1; i < buildingSize - 1; ++i) {
            beforeStructure = dataStructureList.get(i-1);
            currentStructure = dataStructureList.get(i);
            nextStructure = dataStructureList.get(i+1);

            beforeIndexList = indexListList.get(i-1);
            currentIndexList = indexListList.get(i);
            nextIndexList = indexListList.get(i+1);

            beforeAnchorB = beforeStructure.getAnchorList().get(beforeIndexList.get(beforeIndexList.size()-1));
            currentAnchorA = currentStructure.getAnchorList().get(currentIndexList.get(0));
            currentAnchorB = currentStructure.getAnchorList().get(currentIndexList.get(1));
            nextAnchorA = nextStructure.getAnchorList().get(nextIndexList.get(0));

            randomWeight = RandomUtil.getRandomDouble(neighboringDistanceLowerBound, neighboringDistanceUpperBound, random);
            finalSimpleGraph.addElement(beforeAnchorB, currentAnchorA, randomWeight);
            randomWeight = RandomUtil.getRandomDouble(neighboringDistanceLowerBound, neighboringDistanceUpperBound, random);
            finalSimpleGraph.addElement(currentAnchorB, nextAnchorA, randomWeight);

            finalStructure.combine(dataStructureList.get(i));
        }
        beforeStructure = dataStructureList.get(buildingSize - 2);
        currentStructure = dataStructureList.get(buildingSize - 1);
        beforeIndexList = indexListList.get(buildingSize - 2);
        currentIndexList = indexListList.get(buildingSize - 1);
        beforeAnchorB = beforeStructure.getAnchorList().get(beforeIndexList.get(beforeIndexList.size()-1));
        currentAnchorA = currentStructure.getAnchorList().get(currentIndexList.get(0));
        randomWeight = RandomUtil.getRandomDouble(neighboringDistanceLowerBound, neighboringDistanceUpperBound, random);
        finalSimpleGraph.addElement(beforeAnchorB, currentAnchorA, randomWeight);
        finalStructure.combine(dataStructureList.get(buildingSize - 1));
        return finalStructure;
    }

    public static Job generateJob(Integer taskSize, List<Anchor> candidateList, List<Integer> chosenIndexList) {
        Task tempTask;
        List<Task> taskList = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < taskSize; ++i) {
            tempTask = new Task("task-"+(i+1), candidateList.get(chosenIndexList.get(k++)), candidateList.get(chosenIndexList.get(k++)), Constant.DefaultFetchTime, Constant.DefaultSendOffTime, Constant.DefaultOccupyingSpace);
            taskList.add(tempTask);
        }
        return new Job("Job-1", Constant.DefaultJobStartTime, Constant.DefaultJobEndTime, taskList);
    }


    public static void main(String[] args) {
        DatasetStructure modelSimpleGraphStructure;
        Integer buildingSize = 3;
        Integer buildingLayerSizeLowerBound = 3;
        Integer buildingLayerSizeUpperBound = 5;
        Random random = new Random(1);

        Double neighboringDistanceLowerBound = 2D;
        Double neighboringDistanceUpperBound = 7D;

        Integer basicAnchorSize = 3;
        Integer maxHalfNeighboringSize = 3;
        Integer elevatorSize = 2;
        Integer stairSize = 2;
        Building basicBuilding = new Building("building-model-0", 3, Constant.NeighboringLayersDistance);
        Double positionLowerBound = 0D, positionUpperBound = 1000D;
        DatasetStructure modelDatasetStructure = generateBasicNormalEntity(basicAnchorSize, maxHalfNeighboringSize, elevatorSize, stairSize, basicBuilding, random, positionLowerBound, positionUpperBound, neighboringDistanceLowerBound, neighboringDistanceUpperBound);
//        System.out.println(datasetStructure);
        modelDatasetStructure.show();

        MyPrint.showSplitLine("-", 200);

        DatasetStructure finalDatasetStructure = generateTotalSimpleGraph(modelDatasetStructure, buildingSize, buildingLayerSizeLowerBound, buildingLayerSizeUpperBound, random, neighboringDistanceLowerBound, neighboringDistanceUpperBound);
        finalDatasetStructure.show();

        System.out.println(finalDatasetStructure.getSimpleGraph().getGraphTable().keySet().size());;
    }
}
