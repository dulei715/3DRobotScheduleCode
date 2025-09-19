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


    public Integer getBuildingNumber() {
        // 要求Building的命名形如：Building-number
        String[] splitArray = this.buildingName.split("-");
        return Integer.valueOf(splitArray[splitArray.length-1]);
    }
}
