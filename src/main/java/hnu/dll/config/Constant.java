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

    public static final Integer PrecisionSize = 2;



    public static final Double NeighboringLayersDistance = 3.5;
    public static final Integer DefaultLayerSize = 2;
    public static final Double TimeUnit = 1D;
    public static final Integer topKSize = 3;

    // for Elevator
    public static final Double ElevatorAverageVelocity = 2D;
    public static final Double OpenOrCloseDoorTimeCost = 1.5;
    public static final Integer DefaultElevatorStartLayer = 1;

    // for Robot
    public static final Double DogRobotPlaneVelocity = 0.5;
    public static final Double DogRobotStairVelocity = 0.1;
    public static final Double DogRobotCapacity = 10D;

    public static final Double PersonRobotPlaneVelocity = 1.5;
    public static final Double PersonRobotStairVelocity = 0.5;
    public static final Double PersonRobotCapacity = 7D;

    // for Stair
    public static final Double DefaultStairAngle = Math.PI / 6;  // 设置楼梯仰角30度

    // for Building
    public static final Double buildingControlSideLength = 1000D;
    public static final Double buildingXBias = 15000D;
    public static final Double buildingYBias = 15000D;

    // for Job and Task
    public static final Double DefaultJobStartTime = 0D;
    public static final Double DefaultJobEndTime = 100000D;
    public static final Double DefaultFetchTime = 3D;
    public static final Double DefaultSendOffTime = 3D;
    public static final Double DefaultOccupyingSpace = 6D;
}
