package hnu.dll.structure.basic_structure;

import hnu.dll.basic_entity.location.PlaneLocation;
import hnu.dll.basic_entity.location.ThreeDLocation;
import hnu.dll.entity.Entity;

import java.util.Objects;

public class Anchor extends Entity implements Comparable<Anchor> {
    private ThreeDLocation threeDLocation;

    public Anchor(String name, ThreeDLocation threeDLocation) {
        super(name);
        this.threeDLocation = threeDLocation;
    }
    public Anchor(String name, PlaneLocation planeLocation, Double zIndex) {
        super(name);
        this.threeDLocation = new ThreeDLocation(planeLocation, zIndex);
    }

    public Anchor(ThreeDLocation threeDLocation) {
        this.threeDLocation = threeDLocation;
    }

    public ThreeDLocation getLocation() {
        return threeDLocation;
    }

    public Anchor(String name, Double x, Double y, Double z) {
        super(name);
        this.threeDLocation = new ThreeDLocation(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Anchor anchor = (Anchor) o;
        return Objects.equals(threeDLocation, anchor.threeDLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(threeDLocation);
    }

    @Override
    public int compareTo(Anchor anchor) {
        return this.threeDLocation.compareTo(anchor.threeDLocation);
    }

    @Override
    public String toString() {
        return "Anchor{" +
                "name='" + name +
                ",location=" + threeDLocation + '\'' +
                '}';
    }
//    public String toString() {
//        return "Anchor{" +
//                "threeDLocation=" + threeDLocation +
//                ", id='" + id + '\'' +
//                ", name='" + name + '\'' +
//                '}';
//    }
}
