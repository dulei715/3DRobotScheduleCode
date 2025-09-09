package hnu.dll.basic_entity;

import java.util.Objects;

public class ThreeDLocation implements Comparable<ThreeDLocation> {
    private final Double xIndex;
    private final Double yIndex;
    private final Double zIndex;

    public ThreeDLocation(PlaneLocation planeLocation, Double zIndex) {
        this.xIndex = planeLocation.getxIndex();
        this.yIndex = planeLocation.getyIndex();
        this.zIndex = zIndex;
    }

    public ThreeDLocation(Double xIndex, Double yIndex, Double zIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.zIndex = zIndex;
    }



    public Double getxIndex() {
        return xIndex;
    }


    public Double getyIndex() {
        return yIndex;
    }

    public Double getzIndex() {
        return zIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ThreeDLocation threeDLocation = (ThreeDLocation) o;
        return Objects.equals(xIndex, threeDLocation.xIndex) && Objects.equals(yIndex, threeDLocation.yIndex) && Objects.equals(zIndex, threeDLocation.zIndex);
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

    @Override
    public int compareTo(ThreeDLocation location) {
        int differ = this.xIndex.compareTo(location.xIndex);
        if (differ != 0) {
            return differ;
        }
        differ = this.yIndex.compareTo(location.yIndex);
        if (differ != 0) {
            return differ;
        }
        return this.zIndex.compareTo(location.zIndex);
    }
}
