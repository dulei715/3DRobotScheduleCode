package hnu.dll.control;

import hnu.dll.basic_entity.ThreeDLocation;
import hnu.dll.config.Constant;
import hnu.dll.structure.SortedPathStructure;
import hnu.dll.structure.path.TimePointPath;

import java.util.Set;

public class BasicFunctions {

    // 暂不支持存在地下层的情况
    public static Integer getElevatorRunningTimeSlots(Integer startLayer, Integer endLayer, Double averageRunningVelocity) {
        Double realTime = (endLayer - startLayer) * Constant.NeighboringLayersDistance / averageRunningVelocity;
        return BasicUtils.toUnitTimeShareSize(realTime);
    }

    public static Integer getLayer(ThreeDLocation location) {
        Double zIndex = location.getzIndex();
        double layerDouble = zIndex / Constant.NeighboringLayersDistance + 1;
        return (int)Math.round(layerDouble);
    }

    public static Integer getMaximalTimeSlotLength(Set<SortedPathStructure<TimePointPath>> pathStructureSet) {
        Integer result = 0;
        TimePointPath path;
        for (SortedPathStructure<TimePointPath> pathStructure : pathStructureSet) {
            path = pathStructure.getFirst();
            result = Math.max(path.getTimeLength(), result);
        }
        return result;
    }
}
