package hnu.dll.entity;

import java.util.UUID;

public abstract class Entity implements Comparable<Entity> {
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
}
