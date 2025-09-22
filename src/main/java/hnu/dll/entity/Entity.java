package hnu.dll.entity;

import hnu.dll.basic_entity.location.Location;

import java.util.Comparator;
import java.util.UUID;

public abstract class Entity implements Cloneable {
    protected String id;
    protected String name;

    public Entity(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public Entity() {
        this.id = UUID.randomUUID().toString();
        this.name = "default-name";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toSimpleString() {
        return this.name;
    }

}
