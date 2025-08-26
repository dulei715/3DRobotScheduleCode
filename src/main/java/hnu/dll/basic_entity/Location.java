package hnu.dll.basic_entity;

import java.util.Objects;

public class Location {
    private Double xIndex;
    private Double yIndex;
    private Double zIndex;

    public Location(Double xIndex, Double yIndex, Double zIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.zIndex = zIndex;
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

    public Double getzIndex() {
        return zIndex;
    }

    public void setzIndex(Double zIndex) {
        this.zIndex = zIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(xIndex, location.xIndex) && Objects.equals(yIndex, location.yIndex) && Objects.equals(zIndex, location.zIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xIndex, yIndex, zIndex);
    }

    @Override
    public String toString() {
        return "Location{" +
                "xIndex=" + xIndex +
                ", yIndex=" + yIndex +
                ", zIndex=" + zIndex +
                '}';
    }
}
