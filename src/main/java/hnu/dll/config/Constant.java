package hnu.dll.config;

import java.util.Objects;

public class Constant {

    /**
     * 时间以秒为单位, 长度以米为单位
     */

    public static String resourcePath = Objects.requireNonNull(
            Thread.currentThread()
                    .getContextClassLoader()
                    .getResource("")
    ).getPath();

    public static final String BasicPath = "/Volumes/leileidu-mac-extension/2.important_files/MainFiles/1.Research/dataset/8_robot_schedule/data/";



    public static final Double NeighboringLayersDistance = 3.5;
    public static final Integer DefaultLayerSize = 2;
    public static final Double TimeUnit = 1D;
    public static final Integer topKSize = 3;

    // for Elevator
    public static final Double ElevatorAverageVelocity = 2D;
    public static final Double OpenOrCloseDoorTimeCost = 1.5;
    public static final Integer DefaultStartLayer = 1;

    // for Robot
    public static final Double DogRobotPlaneVelocity = 0.5;
    public static final Double DogRobotStairVelocity = 0.1;
    public static final Double DogRobotCapacity = 10D;

    public static final Double PersonRobotPlaneVelocity = 1.5;
    public static final Double PersonRobotStairVelocity = 0.5;
    public static final Double PersonRobotCapacity = 7D;

    // for Stair
    public static final Double stairAngle = Math.PI / 6;  // 设置楼梯仰角30度
}
