package hnu.dll.entity;

import hnu.dll.basic_entity.PlaneLocation;
import hnu.dll.basic_entity.ThreeDLocation;

import java.util.List;

public class Stair extends Entity {

    private PlaneLocation planeLocation;
    private Integer layerSize;
    private List<ThreeDLocation> innerNodeList;
    private Double segmentLength;

    public Stair(String name, PlaneLocation planeLocation, Integer layerSize, List<ThreeDLocation> innerNodeList, Double segmentLength) {
        super(name);
        this.planeLocation = planeLocation;
        this.layerSize = layerSize;
        this.innerNodeList = innerNodeList;
        this.segmentLength = segmentLength;
    }

    public PlaneLocation getPlaneLocation() {
        return planeLocation;
    }

    public Integer getLayerSize() {
        return layerSize;
    }

    public List<ThreeDLocation> getInnerNodeList() {
        return innerNodeList;
    }

    public Double getSegmentLength() {
        return segmentLength;
    }
}
