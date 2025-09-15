package hnu.dll.structure;

public class Building {
    private String buildingName;
    private Integer layerSize;
    private Double averageLayerHeight;

    public Building(String buildingName, Integer layerSize, Double averageLayerHeight) {
        this.buildingName = buildingName;
        this.layerSize = layerSize;
        this.averageLayerHeight = averageLayerHeight;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public Integer getLayerSize() {
        return layerSize;
    }

    public Double getAverageLayerHeight() {
        return averageLayerHeight;
    }
}
