package hnu.dll.data;

import hnu.dll.basic_entity.location.ThreeDLocation;

public class DatasetUtils {
    public static Double getHorizontalPlaneCenterPoint(ThreeDLocation[] location) {
        int size = location.length;
        if (size != 4) {
            throw new RuntimeException("The point size is not equal to 4!");
        }
        for (int i = 1; i < size; ++i) {
            if (!location[i].getzIndex().equals(location[i-1].getzIndex())) {
                throw new RuntimeException("It's not a horizontal plane!");
            }
        }
        double zIndex = location[0].getzIndex();
        double xIndex, yIndex;
        
    }
}
