package hnu.dll.structure;

import hnu.dll.basic_entity.ThreeDLocation;
import hnu.dll.entity.Entity;
import hnu.dll.structure.basic_structure.BasicPair;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Deprecated
public class SpatialTemporalPath implements Comparable<SpatialTemporalPath>{
    private LinkedList<BasicPair<ThreeDLocation, Entity>> path;

    public SpatialTemporalPath() {
        this.path = new LinkedList<>();
    }

    public void addElement(Integer index, ThreeDLocation location, Entity entity) {
        this.path.add(index, new BasicPair<>(location, entity));
    }

    public BasicPair<ThreeDLocation, Entity> getElement(Integer index) {
        return this.path.get(index);
    }

    public Integer getLength() {
        return this.path.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SpatialTemporalPath that = (SpatialTemporalPath) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }

    @Override
    public int compareTo(SpatialTemporalPath spatialTemporalPath) {
        int differ = this.path.size() - spatialTemporalPath.path.size();
        if (differ != 0) {
            return differ;
        }
        Iterator<BasicPair<ThreeDLocation, Entity>> thisIterator = this.path.iterator();
        Iterator<BasicPair<ThreeDLocation, Entity>> thatIterator = spatialTemporalPath.path.iterator();
        BasicPair<ThreeDLocation, Entity> nextA, nextB;
        while (thisIterator.hasNext()) {
            nextA = thisIterator.next();
            nextB = thatIterator.next();
            differ = nextA.getKey().compareTo(nextB.getKey());
            if (differ != 0) {
                return differ;
            }
        }
        return 0;
    }
}
