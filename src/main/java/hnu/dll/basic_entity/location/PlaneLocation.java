package hnu.dll.basic_entity.location;

import java.util.Objects;

public class PlaneLocation extends Location implements Comparable<PlaneLocation> {
    private Double xIndex;
    private Double yIndex;

    public PlaneLocation(Double xIndex, Double yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    public PlaneLocation(String locationString, String splitTag) {
        String[] locationStringArray = locationString.split(splitTag);
        this.xIndex = Double.parseDouble(locationStringArray[0]);
        this.yIndex = Double.parseDouble(locationStringArray[1]);
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

    public PlaneLocation generateNewPlaneLocationWithBias(Double xBias, Double yBias) {
        return new PlaneLocation(this.xIndex + xBias, this.yIndex + yBias);
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
        return "(" + xIndex + ", " + yIndex + ")";
    }
//    @Override
//    public String toString() {
//        return "PlaneLocation{" +
//                "xIndex=" + xIndex +
//                ", yIndex=" + yIndex +
//                '}';
//    }

    @Override
    public Double[] getIndexArray() {
        return new Double[]{this.xIndex, this.yIndex};
    }

    @Override
    public int compareTo(PlaneLocation location) {
        int differ = this.xIndex.compareTo(location.xIndex);
        if (differ != 0) {
            return differ;
        }
        return this.yIndex.compareTo(location.yIndex);
    }
}
