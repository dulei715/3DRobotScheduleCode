package hnu.dll.entity;

import hnu.dll.basic_entity.location.PlaneLocation;
import hnu.dll.basic_entity.location.ThreeDLocation;
import hnu.dll.structure.Building;

import java.util.ArrayList;
import java.util.List;

public class Stair extends Entity {

    private Building building;
    private PlaneLocation planeLocation;
    // 所有层及其转弯节点包含底部和顶部
    private List<ThreeDLocation> innerNodeList;
    private Double segmentLength;

    public Stair(String name, Building building, PlaneLocation planeLocation, List<ThreeDLocation> innerNodeList, Double segmentLength) {
        super(name);
        this.building = building;
        this.planeLocation = planeLocation;
        this.innerNodeList = innerNodeList;
        this.segmentLength = segmentLength;
    }
    public Stair(String name, Building building, PlaneLocation planeLocation) {
        super(name);
        this.building = building;
        this.planeLocation = planeLocation;
    }

    public void setInnerNodeList(List<ThreeDLocation> innerNodeList) {
        this.innerNodeList = innerNodeList;
    }


    public void setSegmentLength(Double segmentLength) {
        this.segmentLength = segmentLength;
    }

    public static List<ThreeDLocation> getDefaultInnerNodeList(PlaneLocation bottomLocation, Double startZIndex, Double neighboringLayerDistance, Integer layerSize) {
        Double xIndex = bottomLocation.getxIndex();
        Double yIndex = bottomLocation.getyIndex();
        Double segmentZDistance = neighboringLayerDistance / 2;
        List<ThreeDLocation> result = new ArrayList<>();
        result.add(new ThreeDLocation(xIndex, yIndex, startZIndex));
        Integer size = (layerSize - 1) * 2;
        Double tempZIndex = startZIndex;
        for (int i = 0; i < size; i++) {
            tempZIndex += segmentZDistance;
            result.add(new ThreeDLocation(xIndex, yIndex, tempZIndex));
        }
        return result;
    }

    public PlaneLocation getPlaneLocation() {
        return planeLocation;
    }

    public Integer getLayerSize() {
        return this.building.getLayerSize();
    }

    public List<ThreeDLocation> getInnerNodeList() {
        return innerNodeList;
    }

    public Double getSegmentLength() {
        return segmentLength;
    }


    @Override
    public String toString() {
        return "Stair{" +
                "name" + name +
                ", location='" + planeLocation +
                ", innerNodeList=" + innerNodeList +
                ", segmentLength=" + segmentLength +
                ", layerSize=" + building.getLayerSize() + '\'' +
                '}';
    }
//    public String toString() {
//        return "Stair{" +
//                "planeLocation=" + planeLocation +
//                ", layerSize=" + layerSize +
//                ", innerNodeList=" + innerNodeList +
//                ", segmentLength=" + segmentLength +
//                ", id='" + id + '\'' +
//                ", name='" + name + '\'' +
//                '}';
//    }
}
