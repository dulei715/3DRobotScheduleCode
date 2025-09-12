package project_test;

import cn.edu.dll.constant_values.ConstantValues;
import cn.edu.dll.io.print.MyPrint;
import cn.edu.dll.io.read.CSVRead;
import cn.edu.dll.io.read.PropertiesRead;
import hnu.dll.basic_entity.location.Location;
import hnu.dll.basic_entity.location.PlaneLocation;
import hnu.dll.basic_entity.location.ThreeDLocation;
import hnu.dll.config.Constant;
import hnu.dll.control.BasicFunctions;
import hnu.dll.control.basic_tools.Tools;
import hnu.dll.control.topk.AnchorEntityConvertor;
import hnu.dll.control.topk.AnchorEntityTransform;
import hnu.dll.entity.*;
import hnu.dll.structure.SortedPathStructure;
import hnu.dll.structure.TimeWeightedGraph;
import hnu.dll.structure.basic_structure.Anchor;
import hnu.dll.structure.basic_structure.BasicPair;
import hnu.dll.structure.graph.SimpleGraph;
import hnu.dll.structure.path.AnchorPointPath;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class ProjectTest {
    public static final String PropertiesSplitTag = ",";
    public static final Integer LayerSize = 2;
    public static final String TestDataPath = ConstantValues.ProjectDir + "/src/test/resources/data";
    public static Map<String, Location> locationMap;
    public static Map<String, Entity> entityMap;
    public static SimpleGraph simpleGraph;
    public static Map<String, List<Robot>> robotTypeMap;
    public static Map<String, TimeWeightedGraph> robotTypeTimeWeightedGraphMap;
    public static Map<String, List<Anchor>> stringAnchorListMap;
    public static AnchorEntityConvertor anchorEntityConvertor;
    public static Job job;

    public static void initializeRobotTypeMap() {
        Robot tempRobot;
        robotTypeMap = new HashMap<>();
        List<Robot> tempRobotList = new ArrayList<>(3);
        tempRobot = new Robot("robot-dog-1", Robot.DogRobotType, Constant.DogRobotPlaneVelocity, Constant.DogRobotStairVelocity, Constant.DogRobotCapacity);
        tempRobot.setLocation((Anchor) entityMap.get("A-1-1"));
        tempRobotList.add(tempRobot);
        tempRobot = new Robot("robot-dog-2", Robot.DogRobotType, Constant.DogRobotPlaneVelocity, Constant.DogRobotStairVelocity, Constant.DogRobotCapacity);
        tempRobot.setLocation((Anchor) entityMap.get("A-1-5"));
        tempRobotList.add(tempRobot);
        tempRobot = new Robot("robot-dog-3", Robot.DogRobotType, Constant.DogRobotPlaneVelocity, Constant.DogRobotStairVelocity, Constant.DogRobotCapacity);
        tempRobot.setLocation((Anchor) entityMap.get("A-2-8"));
        tempRobotList.add(tempRobot);
        robotTypeMap.put(Robot.DogRobotType, tempRobotList);

        tempRobotList = new ArrayList<>();
        tempRobot = new Robot("robot-person-1", Robot.PersonRobotType, Constant.PersonRobotPlaneVelocity, Constant.PersonRobotStairVelocity, Constant.PersonRobotCapacity);
        tempRobot.setLocation((Anchor) entityMap.get("A-1-12"));
        tempRobotList.add(tempRobot);
        tempRobot = new Robot("robot-person-2", Robot.PersonRobotType, Constant.PersonRobotPlaneVelocity, Constant.PersonRobotStairVelocity, Constant.PersonRobotCapacity);
        tempRobot.setLocation((Anchor) entityMap.get("A-2-6"));
        tempRobotList.add(tempRobot);
        robotTypeMap.put(Robot.PersonRobotType, tempRobotList);



    }

    public static void initializeJob() {
        Task tempTask;
        Double defaultFetchTime = 3D, defaultSendOffTime = 3D, defaultOccupyingSpace = 6D;
        List<Task> taskList = new ArrayList<>(5);
        tempTask = new Task("task-1", (Anchor) entityMap.get("A-2-12"), (Anchor) entityMap.get("A-1-3"), defaultFetchTime, defaultSendOffTime, defaultOccupyingSpace);
        taskList.add(tempTask);
        tempTask = new Task("task-2", (Anchor) entityMap.get("A-1-8"), (Anchor) entityMap.get("A-2-5"), defaultFetchTime, defaultSendOffTime, defaultOccupyingSpace);
        taskList.add(tempTask);
        tempTask = new Task("task-3", (Anchor) entityMap.get("A-2-8"), (Anchor) entityMap.get("A-1-1"), defaultFetchTime, defaultSendOffTime, defaultOccupyingSpace);
        taskList.add(tempTask);
        tempTask = new Task("task-4", (Anchor) entityMap.get("A-1-10"), (Anchor) entityMap.get("A-1-12"), defaultFetchTime, defaultSendOffTime, defaultOccupyingSpace);
        taskList.add(tempTask);
        tempTask = new Task("task-5", (Anchor) entityMap.get("A-2-1"), (Anchor) entityMap.get("A-2-6"), defaultFetchTime, defaultSendOffTime, defaultOccupyingSpace);
        taskList.add(tempTask);
        job = new Job("job-1", 0D, 1000000D, taskList);

    }

    public static void initializeSimpleGraph() {
        PropertiesRead propertiesRead = new PropertiesRead(TestDataPath + "/location.properties");
        entityMap = new HashMap<>();
        locationMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : propertiesRead.getEntrySet()) {
            String entityName = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (entityName.startsWith("L")) {
                PlaneLocation location = new PlaneLocation(value, PropertiesSplitTag);
                locationMap.put(entityName, location);
                entityMap.put(entityName, new Elevator(entityName, Constant.ElevatorAverageVelocity, Constant.OpenOrCloseDoorTimeCost, LayerSize, location));
            } else if (entityName.startsWith("S")) {
                PlaneLocation location = new PlaneLocation(value, PropertiesSplitTag);
                locationMap.put(entityName, location);
                List<ThreeDLocation> locationList = Stair.getDefaultInnerNodeList(location, 0D, Constant.NeighboringLayersDistance, LayerSize);
                entityMap.put(entityName, new Stair(entityName, location, LayerSize, locationList, Constant.NeighboringLayersDistance / 2 / Math.sin(Constant.stairAngle)));
            } else {
                ThreeDLocation location = new ThreeDLocation(value, PropertiesSplitTag);
                locationMap.put(entityName, location);
                entityMap.put(entityName, new Anchor(entityName, location));
            }
        }
        simpleGraph = new SimpleGraph();
        List<Map<String, String>> mapList = CSVRead.readData(TestDataPath + "/test_graph_data.csv");
        Set<String> tempToNameSet;
        Entity startEntity, endEntity;
        Double tempWeight;
        for (Map<String, String> map : mapList) {
            String fromName = map.get("data");
            startEntity = entityMap.get(fromName);
            tempToNameSet = map.keySet();
            for (String toName : tempToNameSet) {
                if (toName.equals("data")) {
                    continue;
                }
                String tempValue = map.get(toName);
                if (tempValue.isEmpty() || tempValue.equals("0")) {
                    continue;
                }
                tempWeight = Double.parseDouble(tempValue);
                endEntity = entityMap.get(toName);
                simpleGraph.addElement(startEntity, endEntity, tempWeight);
            }
        }
    }

    public static void initializeTimeWeightedGraphMap() {
        Robot tempRobot;
        Map<Anchor, Map<Anchor, Double>> graphTable;
        TimeWeightedGraph tempTimeWeightedGraph;
        String robotType;
        robotTypeTimeWeightedGraphMap = new HashMap<>();
        for (Map.Entry<String, List<Robot>> entry : robotTypeMap.entrySet()) {
            robotType = entry.getKey();
            tempRobot = entry.getValue().get(0);
            tempTimeWeightedGraph = Tools.getTimeWeightedGraph(simpleGraph, tempRobot);
            robotTypeTimeWeightedGraphMap.put(robotType, tempTimeWeightedGraph);
        }
    }

    public static void initializeStringAnchorListMapByAnyTimeWeightedGraph(TimeWeightedGraph timeWeightedGraph) {
        stringAnchorListMap = BasicFunctions.extractStringAnchorListMap(timeWeightedGraph);
    }

    @BeforeClass
    public static void initialize() {
        initializeSimpleGraph();
        initializeRobotTypeMap();
        initializeJob();
        initializeTimeWeightedGraphMap();
        TimeWeightedGraph timeWeightedGraph = robotTypeTimeWeightedGraphMap.values().iterator().next();
        initializeStringAnchorListMapByAnyTimeWeightedGraph(timeWeightedGraph);
        anchorEntityConvertor = new AnchorEntityTransform(stringAnchorListMap, entityMap);
    }

    @Test
    public void simpleGraphTest() {
        Map<Entity, Map<Entity, Double>> graphTable = simpleGraph.getGraphTable();
        System.out.println(graphTable.size());
        MyPrint.showSplitLine("*", 150);
        MyPrint.showMap(graphTable);
    }


    @Test
    public void timeWeightedGraphTest() {
        Map<Anchor, Map<Anchor, Double>> graphTable;
        for (Map.Entry<String, TimeWeightedGraph> entry : robotTypeTimeWeightedGraphMap.entrySet()) {
            System.out.println(entry.getKey());
            graphTable = entry.getValue().getGraphTable();
            MyPrint.showMap(graphTable);
            System.out.println(graphTable.size());
            MyPrint.showSplitLine("*", 150);
        }
    }

    @Test
    public void extractAnchorSetTest() {
        TimeWeightedGraph timeWeightedGraph = robotTypeTimeWeightedGraphMap.values().iterator().next();
        Map<String, List<Anchor>> stringAnchorListMap = BasicFunctions.extractStringAnchorListMap(timeWeightedGraph);
        MyPrint.showMap(stringAnchorListMap);
        System.out.println(stringAnchorListMap.size());
    }

    @Test
    public void topKShortedPathTest() {
        TimeWeightedGraph graph = robotTypeTimeWeightedGraphMap.get(Robot.DogRobotType);
//        System.out.println(graph.getGraphTable());
        Anchor startAnchor = (Anchor) entityMap.get("A-1-1");
        Anchor endAnchor = (Anchor) entityMap.get("A-2-10");
//      (1D,1D,0D);
//      (6D,6D,3D);
        List<AnchorPointPath> pathList = BasicFunctions.getTopKShortestPath(graph, startAnchor, endAnchor, Constant.topKSize, anchorEntityConvertor);
        MyPrint.showList(pathList, ConstantValues.LINE_SPLIT);
    }


    @Test
    public void taskAssignmentTest() {
        List<Task> taskList = job.getTaskList();
        List<Robot> robotList = Robot.getRobotList(robotTypeMap);
        Integer topKSize = Constant.topKSize; // 3
        Map<BasicPair<Task, Robot>, SortedPathStructure<AnchorPointPath>> pathMap = Tools.taskAssignment(robotTypeTimeWeightedGraphMap, taskList, robotList, topKSize, anchorEntityConvertor);
        MyPrint.showMap(pathMap);
    }

    

}
