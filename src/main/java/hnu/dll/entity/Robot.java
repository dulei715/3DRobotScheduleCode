package hnu.dll.entity;

import hnu.dll.basic_entity.Status;

public abstract class Robot {
    public static final Double batteryCapacity = 100D;

    private Long id;
    private String name;
    private String type;

    private Double velocity;
    private Double capacity;
    private Double remainingPower;

    private Status status;

}
