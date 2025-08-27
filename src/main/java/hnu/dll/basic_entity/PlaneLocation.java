package hnu.dll.basic_entity;

import java.util.Objects;

public class PlaneLocation {
    private Double xIndex;
    private Double yIndex;

    public PlaneLocation(Double xIndex, Double yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    public Double getxIndex() {
        return xIndex;
    }

    public void setxIndex(Double xIndex) {
        this.xIndex = xIndex;
    }

    public Double getyIndex() {
        return yIndex;
    }

    public void setyIndex(Double yIndex) {
        this.yIndex = yIndex;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlaneLocation that = (PlaneLocation) o;
        return Objects.equals(xIndex, that.xIndex) && Objects.equals(yIndex, that.yIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xIndex, yIndex);
    }

    @Override
    public String toString() {
        return "PlaneLocation{" +
                "xIndex=" + xIndex +
                ", yIndex=" + yIndex +
                '}';
    }
}
