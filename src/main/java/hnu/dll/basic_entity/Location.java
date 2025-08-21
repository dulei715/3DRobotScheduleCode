package hnu.dll.basic_entity;

import java.util.Objects;

public class Location {
    private Double xIndex;
    private Double yIndex;

    public Location() {
    }

    public Location(Double xIndex, Double yIndex) {
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
        Location location = (Location) o;
        return Objects.equals(xIndex, location.xIndex) && Objects.equals(yIndex, location.yIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xIndex, yIndex);
    }
}
