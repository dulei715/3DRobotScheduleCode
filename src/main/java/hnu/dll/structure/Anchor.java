package hnu.dll.structure;

import hnu.dll.basic_entity.Location;
import hnu.dll.entity.Entity;

import java.util.Objects;

public class Anchor extends Entity {
    private Location location;

    public Anchor(String name, Location location) {
        super(name);
        this.location = location;
    }

    public Anchor(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Anchor anchor = (Anchor) o;
        return Objects.equals(location, anchor.location);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(location);
    }
}
