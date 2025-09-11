package hnu.dll.config;

import java.util.Objects;

public class Constant {

    public static String resourcePath = Objects.requireNonNull(
            Thread.currentThread()
                    .getContextClassLoader()
                    .getResource("")
    ).getPath();



    public static final Double NeighboringLayersDistance = 3D;
    public static final Double TimeUnit = 10D;
    public static final Integer topKSize = 3;

    // for Elevator
    public static final Double ElevatorAverageVelocity = 0.5;
    public static final Double OpenOrCloseDoorTimeCost = 1.0/12;
}
