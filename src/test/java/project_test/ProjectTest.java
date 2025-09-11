package project_test;

import cn.edu.dll.constant_values.ConstantValues;
import cn.edu.dll.io.print.MyPrint;
import cn.edu.dll.io.read.CSVRead;
import cn.edu.dll.io.read.PropertiesRead;
import hnu.dll.basic_entity.PlaneLocation;
import hnu.dll.basic_entity.ThreeDLocation;
import hnu.dll.config.Constant;
import hnu.dll.entity.Elevator;
import hnu.dll.entity.Entity;
import hnu.dll.entity.Stair;
import hnu.dll.structure.basic_structure.Anchor;
import hnu.dll.structure.graph.SimpleGraph;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.util.*;

public class ProjectTest {
    public static final String PropertiesSplitTag = ",";
    public static final Integer LayerSize = 2;
    public static final String TestDataPath = ConstantValues.ProjectDir + "/src/test/resources/data";
    public static Map<String, ThreeDLocation> locationMap;
    public static SimpleGraph data;

    @Before
    public void initialize() {
//        String propertiesPath = System.getProperty("user.dir");
//        InputStream resourceAsStream = ProjectTest.class.getResourceAsStream(propertiesPath);
//        System.out.println(resourceAsStream);
        PropertiesRead propertiesRead = new PropertiesRead(TestDataPath + "/location.properties");
        Map<String, Entity> entityMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : propertiesRead.getEntrySet()) {
            String entityName = (String) entry.getKey();
            String value = (String) entry.getValue();
//            ThreeDLocation location = new ThreeDLocation(value, PropertiesSplitTag);
            if (entityName.startsWith("L")) {
                PlaneLocation location = new PlaneLocation(value, PropertiesSplitTag);
                entityMap.put(entityName, new Elevator(entityName, Constant.ElevatorAverageVelocity, Constant.OpenOrCloseDoorTimeCost, LayerSize, location));
            } else if (entityName.startsWith("S")) {
                PlaneLocation location = new PlaneLocation(value, PropertiesSplitTag);
                List<ThreeDLocation> locationList = Stair.getDefaultInnerNodeList(location, 0D, Constant.NeighboringLayersDistance, LayerSize);
                entityMap.put(entityName, new Stair(entityName, location, LayerSize, locationList, Constant.NeighboringLayersDistance / 2));
            } else {
                ThreeDLocation location = new ThreeDLocation(value, PropertiesSplitTag);
                entityMap.put(entityName, new Anchor(entityName, location));
            }
        }

//        MyPrint.showMap(entityMap);


        data = new SimpleGraph();
//        MyPrint.showSplitLine("*", 100);
//        MyPrint.showSplitLine();
        List<Map<String, String>> mapList = CSVRead.readData(TestDataPath + "/test_graph_data.csv");
//        System.out.println(mapList);
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
                data.addElement(startEntity, endEntity, tempWeight);
            }
        }
    }


    @Test
    public void basicProjectTest() {
//        System.out.println(Constant.resourcePath);
        MyPrint.showMap(data.getGraphTable());
        System.out.println(data.getGraphTable().size());
    }
}
