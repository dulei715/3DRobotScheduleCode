package hnu.dll;

import cn.edu.dll.basic.RandomUtil;
import cn.edu.dll.io.print.MyPrint;
import hnu.dll.config.Constant;
import hnu.dll.control.BasicFunctions;
import hnu.dll.control.topk.AnchorEntityConvertor;
import hnu.dll.control.topk.AnchorEntityTransform;
import hnu.dll.data.DatasetGenerator;
import hnu.dll.entity.*;
import hnu.dll.mechanism.three_robot_tools.ThreeDRobotTools;
import hnu.dll.structure.Building;
import hnu.dll.structure.DatasetStructure;
import hnu.dll.structure.TimeWeightedGraph;
import hnu.dll.structure.basic_structure.Anchor;
import hnu.dll.structure.graph.SimpleGraph;
import hnu.dll.structure.match.Match;
import hnu.dll.structure.match.MatchElement;

import java.util.*;

public class ProjectGenerationTestRun {

    public static Random random;

    public static DatasetStructure modelDatasetStructure;
    public static DatasetStructure finalDatasetStructure;

    public static SimpleGraph simpleGraph;
    public static Map<String, Entity> entityMap;
    public static Map<String, List<Robot>> robotTypeMap;
    public static Map<String, TimeWeightedGraph> robotTypeTimeWeightedGraphMap;
    public static Map<String, List<Anchor>> stringAnchorListMap;
    public static List<Elevator> elevatorList;
    public static AnchorEntityConvertor anchorEntityConvertor;
    public static Job job;

    public static void initializeTimeWeightedGraphMap() {
        Robot tempRobot;
        Map<Anchor, Map<Anchor, Double>> graphTable;
        TimeWeightedGraph tempTimeWeightedGraph;
        String robotType;
        robotTypeTimeWeightedGraphMap = new HashMap<>();
        for (Map.Entry<String, List<Robot>> entry : robotTypeMap.entrySet()) {
            robotType = entry.getKey();
            tempRobot = entry.getValue().get(0);
            tempTimeWeightedGraph = ThreeDRobotTools.getTimeWeightedGraph(simpleGraph, tempRobot);
            robotTypeTimeWeightedGraphMap.put(robotType, tempTimeWeightedGraph);
        }
    }

    public static void initializeStringAnchorListMapByAnyTimeWeightedGraph() {
        stringAnchorListMap = BasicFunctions.extractStringAnchorListMap(robotTypeTimeWeightedGraphMap.get(Robot.DogRobotType));
    }

    public static void initializeEntityMap() {
        entityMap = DatasetGenerator.extractEntityMap(finalDatasetStructure);
    }

    public static void initializeConvert() {
        anchorEntityConvertor = new AnchorEntityTransform(stringAnchorListMap, entityMap);
    }

    public static void setParameters() {
        simpleGraph = finalDatasetStructure.getSimpleGraph();
        initializeTimeWeightedGraphMap();
        initializeStringAnchorListMapByAnyTimeWeightedGraph();
        initializeEntityMap();
        initializeConvert();
    }

    public void before() {
        Integer buildingSize = 2;
        Integer buildingLayerSizeLowerBound = 3;
        Integer buildingLayerSizeUpperBound = 5;
        Integer basicAnchorSize = 10;
        Integer maxHalfNeighboringSize = 3;
        Integer elevatorSize = 1;
        Integer stairSize = 1;
        Integer dogSize = 5;
        Integer personSize = 2;
        Integer taskSize = 7;




        Double neighboringDistanceLowerBound = 2D;
        Double neighboringDistanceUpperBound = 7D;

        Building basicBuilding = new Building("building-model-0", 3, Constant.NeighboringLayersDistance);
        Double positionLowerBound = 0D, positionUpperBound = 1000D;
        modelDatasetStructure = DatasetGenerator.generateBasicNormalEntity(basicAnchorSize, maxHalfNeighboringSize, elevatorSize, stairSize, basicBuilding, random, positionLowerBound, positionUpperBound, neighboringDistanceLowerBound, neighboringDistanceUpperBound);
//        modelDatasetStructure.show();

//        MyPrint.showSplitLine("-", 200);

        finalDatasetStructure = DatasetGenerator.generateTotalSimpleGraph(modelDatasetStructure, buildingSize, buildingLayerSizeLowerBound, buildingLayerSizeUpperBound, random, neighboringDistanceLowerBound, neighboringDistanceUpperBound);
//        finalDatasetStructure.show();

//        System.out.println(finalDatasetStructure.getSimpleGraph().getGraphTable().keySet().size());;

        List<Anchor> anchorList = finalDatasetStructure.getAnchorList();

        int locationSize = anchorList.size();
        List<Integer> candidateIndex = RandomUtil.getRandomIntegerListWithoutRepeat(0, locationSize - 1, dogSize + personSize + 2 * taskSize, random);
        List<Integer> dogIndexList = candidateIndex.subList(0, dogSize);
        List<Integer> personIndexList = candidateIndex.subList(dogSize, dogSize + personSize);
        List<Integer> taskIndexList = candidateIndex.subList(dogSize + personSize, candidateIndex.size());


        robotTypeMap = DatasetGenerator.generateRobotMap(dogSize, personSize, anchorList, dogIndexList, personIndexList);

        job = DatasetGenerator.generateJob(taskSize, anchorList, taskIndexList);

        elevatorList = finalDatasetStructure.getElevatorList();

        setParameters();
    }

    public void jobTest() {
        List<Task> taskList = job.getTaskList();
        for (Task task : taskList) {
            System.out.println(task + ": " +task.getStartEndLocation());
        }

    }

    public void robotTest() {
        for (List<Robot> robotList : robotTypeMap.values()) {
            for (Robot robot : robotList) {
                System.out.println(robot + ": " + robot.getLocation());
            }
        }
    }

    public void basicGraphTest() {
        modelDatasetStructure.show();
    }

    public void run() {
        Map<String, TimeWeightedGraph> timeGraphMap = robotTypeTimeWeightedGraphMap;
        List<Robot> robotList = new ArrayList<>();
        for (List<Robot> tempRobotList : robotTypeMap.values()) {
            robotList.addAll(tempRobotList);
        }
//        Integer topKSize = Constant.topKSize;
        Integer topKSize = 1;
        Match planPath = ThreeDRobotTools.getPlanPathWithConflictElimination(timeGraphMap, robotList, job, elevatorList, topKSize, anchorEntityConvertor);
        System.out.println(planPath);
        for (MatchElement matchElement : planPath.getMatchElementList()) {
            System.out.println(matchElement.getTimePointPath());
        }
        MyPrint.showSplitLine("*", 200);
//        System.out.println();
//
//
//        Match idealPlanPath = ThreeDRobotTools.getPlanPathIdeal(timeGraphMap, robotList, job, elevatorList, anchorEntityConvertor);
//        System.out.println(idealPlanPath);
//        for (MatchElement matchElement : idealPlanPath.getMatchElementList()) {
//            System.out.println(matchElement.getTimePointPath());
//        }
//        MyPrint.showSplitLine("*", 200);
    }

    public static void main(String[] args) {
        int runSize = 1000;
        ProjectGenerationTestRun run;
        for (int i = 0; i < runSize; ++i) {
            System.out.println("i = " + i);
            random = new Random(i);
            run = new ProjectGenerationTestRun();
            run.before();
            run.run();
        }
    }




}
