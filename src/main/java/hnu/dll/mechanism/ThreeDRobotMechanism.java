package hnu.dll.mechanism;

import cn.edu.dll.constant_values.ConstantValues;
import hnu.dll.basic_entity.location.Location;
import hnu.dll.control.topk.AnchorEntityConvertor;
import hnu.dll.entity.Elevator;
import hnu.dll.entity.Entity;
import hnu.dll.entity.Job;
import hnu.dll.entity.Robot;
import hnu.dll.structure.Building;
import hnu.dll.structure.TimeWeightedGraph;
import hnu.dll.structure.basic_structure.Anchor;
import hnu.dll.structure.graph.SimpleGraph;

import java.util.List;
import java.util.Map;

public class ThreeDRobotMechanism {
    /**
     * 传入的值
     */
    private List<Building> buildingList;
    private Map<String, List<Robot>> robotTypeMap;
    private List<Elevator> elevatorList;
    private Job job;
    private SimpleGraph simpleGraph;

    /**
     * 构建的值
     */
    private Map<String, TimeWeightedGraph> robotTypeTimeWeightedGraphMap;
    private Map<String, Location> locationMap;
    private Map<String, Entity> entityMap;
    private Map<String, List<Anchor>> stringAnchorListMap;
    private AnchorEntityConvertor anchorEntityConvertor;



    // 测试top k候选路径
    public void run() {

        /**
         * 1. 对每个机器人构建TimeWeightedGraph
         */

        /**
         * 2. 任务分配并去冲突
         */

    }
}
