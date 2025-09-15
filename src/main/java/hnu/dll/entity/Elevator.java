package hnu.dll.entity;

import hnu.dll.basic_entity.location.PlaneLocation;
import hnu.dll.config.Constant;
import hnu.dll.control.BasicFunctions;
import hnu.dll.structure.Building;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 仅支持回退到0时刻的检索
 */
public class Elevator extends Entity {

    private Building building;

    private Double velocity;
    private Double openingOrCloseTimeCost;

//    private Integer layerSize;
//    private Double averageLayerHeight;
    private PlaneLocation planeLocation;

    private Integer originalStartLayer;

    /*
        以下是电梯的状态变量
     */
    private Integer currentTimeSlot;
    private Integer currentLayer;
    private Boolean isRunning;
    private Integer startLayer;
    private Integer endLayer;
    private Integer directStatus;
    private Double internalLayerStatus;


    public Double getCrossFloorRunningTime(Integer startLayer, Integer endLayer) {
        return Math.abs(endLayer - startLayer) * building.getAverageLayerHeight() / this.velocity;
    }

//    public void setMoveDoubleLayerEachTimeSlot(Integer startLayer, Integer endLayer) {
//        int unitTimeSize = (int) Math.ceil(Math.abs(endLayer - startLayer) * averageLayerHeight / this.velocity / Constant.TimeUnit);
//
//    }


    public Elevator(String name, Building building, Double velocity, Double openingOrCloseTimeCost, PlaneLocation planeLocation, Integer originalStartLayer) {
        super(name);
        this.building = building;
        this.velocity = velocity;
        this.openingOrCloseTimeCost = openingOrCloseTimeCost;
//        this.layerSize = layerSize;
//        this.averageLayerHeight = averageLayerHeight;
        this.planeLocation = planeLocation;
        this.originalStartLayer = originalStartLayer;
        resetElevator();
    }

//    public void setCurrentLayer(Integer currentTimeSlot, Integer currentLayer) {
//        this.layerStatusList.set(currentTimeSlot, currentLayer);
//    }

    public void resetElevator() {
        this.currentTimeSlot = 0;
        this.currentLayer = this.originalStartLayer;
        this.isRunning = false;
        this.startLayer = this.originalStartLayer;
        this.endLayer = null;
        this.internalLayerStatus = Double.valueOf(this.originalStartLayer);
    }

    public Double getVelocity() {
        return velocity;
    }

    public Double getOpeningOrCloseTimeCost() {
        return openingOrCloseTimeCost;
    }

    public Integer getLayerSize() {
        return this.building.getLayerSize();
    }

    public PlaneLocation getPlaneLocation() {
        return planeLocation;
    }

    /**
     * 传参是为了测试用，测试当前是否同步
     * @param currentTimeSlot
     * @return
     */
    public Integer getCurrentLayer(Integer currentTimeSlot) {
        if(!currentTimeSlot.equals(this.currentTimeSlot)) {
            throw new RuntimeException("当前时刻不同步或不支持其他时刻的查询！");
        }
        return this.currentLayer;
    }



    public void startElevator(Integer endLayer) {
        if (startLayer.equals(endLayer)) {
            throw new RuntimeException("电梯已经在本层了，无需启动！");
        }
        this.endLayer = endLayer;
        this.internalLayerStatus = Double.valueOf(this.startLayer);
        this.directStatus = BasicFunctions.sign(this.endLayer - this.startLayer);
        this.isRunning = true;
    }

    private void stopElevator() {
        this.isRunning = false;
        this.startLayer = this.endLayer;
        this.currentLayer = this.endLayer;
        this.internalLayerStatus = null;
        this.directStatus = null;
        this.endLayer = null;
    }

    public void increaseTimeSlotAndUpdateElevatorStatus() {
        ++this.currentTimeSlot;
        if (!this.isRunning) {
            return;
        }
        this.internalLayerStatus += this.velocity * Constant.TimeUnit * this.directStatus;
        if ((this.internalLayerStatus - this.endLayer) * this.directStatus > 0) {
            this.stopElevator();
            return;
        }
        double layerDiffer = Math.abs(this.internalLayerStatus - this.currentLayer);

        if (Math.abs(layerDiffer) >= 1) {
            this.currentLayer += (int)Math.floor(layerDiffer) * this.directStatus;
        }
    }

    public static void batchResetElevators(Collection<Elevator> elevators) {
        for (Elevator elevator : elevators) {
            elevator.resetElevator();
        }
    }

    public static void batchUpdateElevators(Collection<Elevator> elevators) {
        for (Elevator elevator : elevators) {
            elevator.increaseTimeSlotAndUpdateElevatorStatus();
        }
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "name='" + name +
                ", planeLocation=" + planeLocation + '\'' +
                '}';
    }
//    public String toString() {
//        return "Elevator{" +
//                "velocity=" + velocity +
//                ", openingOrCloseTimeCost=" + openingOrCloseTimeCost +
//                ", layerSize=" + layerSize +
//                ", planeLocation=" + planeLocation +
//                ", currentLayer=" + currentLayer +
//                ", id='" + id + '\'' +
//                ", name='" + name + '\'' +
//                '}';
//    }
}
