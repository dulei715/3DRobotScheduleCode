package hnu.dll.data;

import cn.edu.dll.basic.BasicArrayUtil;
import cn.edu.dll.io.read.XLSXRead;
import hnu.dll.basic_entity.location.PlaneLocation;
import hnu.dll.basic_entity.location.ThreeDLocation;
import hnu.dll.config.Constant;
import hnu.dll.control.BasicFunctions;
import hnu.dll.entity.Elevator;
import hnu.dll.entity.Entity;
import hnu.dll.entity.Stair;
import hnu.dll.structure.Building;

import java.util.*;

public class DatasetHandler {
    private static String basicPath = Constant.BasicPath;
    private String distanceFileName;
    private String locationFileName;
//    private XLSXRead reader;


    public DatasetHandler(String distanceFileName, String locationFileName) {
        this.distanceFileName = distanceFileName;
        this.locationFileName = locationFileName;
        String totalDistanceFileName = basicPath + distanceFileName;
        String totalLocationFileName = basicPath + locationFileName;
    }

    public List<Building> constructBuildingList(Integer buildingSize, Integer layerSize, Double layerNeighboringDistance){
        List<Building> buildingList = new ArrayList<>();
        for (int i = 0; i < buildingSize; ++i) {
            buildingList.add(new Building("Building-"+(i+1), layerSize, layerNeighboringDistance));
        }
        return buildingList;
    }

    private static ThreeDLocation getCenterThreeDLocation(XLSXRead reader, Integer rowStartIndex, Integer rowSize) {
        List<Integer> colIndexList = BasicArrayUtil.getIncreaseIntegerNumberList(1, 1, 3);
        List<Integer> rowIndexList = BasicArrayUtil.getIncreaseIntegerNumberList(rowStartIndex, 1, rowSize);
        List<List<Object>> data = reader.readData(0, rowIndexList, colIndexList);
        List<ThreeDLocation> locationList = new ArrayList<>();
        for (List<Object> datumList : data) {
            List<Double> doubleList = BasicFunctions.toDoubleList(datumList);
            ThreeDLocation location = new ThreeDLocation(doubleList);
            locationList.add(location);
        }
        ThreeDLocation centerLocation = DatasetUtils.getHorizontalPlaneCenterPoint(locationList);
        return centerLocation;
    }

    public void readDataFirstFloorData(Building building) {
        XLSXRead reader = new XLSXRead(this.locationFileName);
        Map<String, List<Entity>> typeNameEntityMap = new HashMap<>();
//        String buildingName = building.getBuildingName();
        String typeName;

        // 添加电梯
        typeName = "L";
        ThreeDLocation centerLocation = getCenterThreeDLocation(reader, 1, 4);
        PlaneLocation planeLocation = new PlaneLocation(centerLocation.getxIndex(), centerLocation.getyIndex());
        Elevator elevator = new Elevator("L-2", building, Constant.ElevatorAverageVelocity, Constant.OpenOrCloseDoorTimeCost, planeLocation, Constant.DefaultElevatorStartLayer);
        typeNameEntityMap.computeIfAbsent(typeName, k->new ArrayList<>()).add(elevator);

        centerLocation = getCenterThreeDLocation(reader, 5, 4);
        planeLocation = new PlaneLocation(centerLocation.getxIndex(), centerLocation.getyIndex());
        elevator = new Elevator("L-1", building, Constant.ElevatorAverageVelocity, Constant.OpenOrCloseDoorTimeCost, planeLocation, Constant.DefaultElevatorStartLayer);
        typeNameEntityMap.computeIfAbsent(typeName, k->new ArrayList<>()).add(elevator);

        // 添加楼梯
        typeName = "S";
        centerLocation = getCenterThreeDLocation(reader, 9, 4);
        planeLocation = new PlaneLocation(centerLocation.getxIndex(), centerLocation.getyIndex());
        Stair stair = new Stair("S-2", building, planeLocation);
        typeNameEntityMap.computeIfAbsent(typeName, k->new ArrayList<>()).add(stair);

        centerLocation = getCenterThreeDLocation(reader, 13, 4);
        planeLocation = new PlaneLocation(centerLocation.getxIndex(), centerLocation.getyIndex());
        stair = new Stair("S-1", building, planeLocation);
        typeNameEntityMap.computeIfAbsent(typeName, k->new ArrayList<>()).add(stair);



    }

    public Map<String, List<Entity>> constructSimpleMap(List<Building> buildingList) {
        return null;
    }

    public static void main(String[] args) {
//        DatasetHandler datasetHandler = new DatasetHandler(Constant.BasicPath);

    }

}
