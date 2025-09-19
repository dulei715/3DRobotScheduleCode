package hnu.dll.data;

import hnu.dll.basic_entity.location.ThreeDLocation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DatasetUtils {
    public static ThreeDLocation getHorizontalPlaneCenterPoint(List<ThreeDLocation> locationList) {
        int size = locationList.size();
        if (size != 4) {
            throw new RuntimeException("The point size is not equal to 4!");
        }
        for (int i = 1; i < size; ++i) {
            if (!locationList.get(i).getzIndex().equals(locationList.get(i-1).getzIndex())) {
                throw new RuntimeException("It's not a horizontal plane!");
            }
        }
        Double zIndex = locationList.get(0).getzIndex();
        Double xIndex=0D, yIndex=0D;
        Set<Double> xIndexSet = new HashSet<>();
        Set<Double> yIndexSet = new HashSet<>();
        for (int i = 0; i < size; ++i) {
            xIndexSet.add(locationList.get(i).getxIndex());
            yIndexSet.add(locationList.get(i).getyIndex());
        }
        if (xIndexSet.size()!=2 || yIndexSet.size()!=2) {
            throw new RuntimeException("x或y坐标不是两种，不构成矩形！");
        }
        Iterator<Double> xIterator = xIndexSet.iterator();
        Iterator<Double> yIterator = yIndexSet.iterator();
        for (int i = 0; i < 2; ++i) {
            xIndex += xIterator.next();
            yIndex += yIterator.next();
        }
        xIndex /= 2;
        yIndex /= 2;
        return new ThreeDLocation(xIndex, yIndex, zIndex);
    }
}
