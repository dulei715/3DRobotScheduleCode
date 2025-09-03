package hnu.dll.structure.path;

import hnu.dll.config.Constant;
import hnu.dll.structure.AnchorEntity;
import hnu.dll.structure.basic_structure.BasicPair;

import java.util.LinkedList;

public class TimePointPath extends Path implements Comparable<TimePointPath>{

    // 第一个是startTime所在位置，每个相邻元素之间间隔TimeUnit大小
    private LinkedList<AnchorEntity> timeStreamAnchorList;
    // todo: add entity

    private TimePointPath(LinkedList<AnchorEntity> timeStreamAnchorList) {
        this.timeStreamAnchorList = timeStreamAnchorList;
    }

    public Integer getTimeLength() {
        return this.timeStreamAnchorList.size();
    }

    public AnchorEntity getAnchorEntityByIndex(Integer index) {
        return this.timeStreamAnchorList.get(index);
    }

    /**
     * 将以时长为权重的路径转化为时间序列
     * @param anchorPointPath
     * @return
     */
    public static TimePointPath getTimePointPathByAnchorPointPath(AnchorPointPath anchorPointPath) {
        LinkedList<AnchorEntity> timeStreamAnchorList = new LinkedList<>();
        AnchorEntity beforeAnchorEntity = anchorPointPath.getStartAnchorPair(), nextAnchor;
        timeStreamAnchorList.add(beforeAnchorEntity);
        Integer edgeTimeLength;
        for (BasicPair<Double, AnchorEntity> segment : anchorPointPath.getInternalDataList()) {
            // 将边长展成时序长度
            edgeTimeLength = (int) Math.ceil(segment.getKey() / Constant.TimeUnit);
            for (int i = 0; i < edgeTimeLength - 1; ++i) {
                timeStreamAnchorList.add(beforeAnchorEntity);
            }
            nextAnchor = segment.getValue();
            timeStreamAnchorList.add(nextAnchor);
            beforeAnchorEntity = nextAnchor;
        }
        return new TimePointPath(timeStreamAnchorList);
    }

    public void insertAnchorEntity(AnchorEntity anchorEntity, Integer timeIndex, Integer timeSlotLength) {
        for (int i = 0; i < timeSlotLength; ++i) {
            this.timeStreamAnchorList.add(timeIndex, anchorEntity);
        }
    }

    @Override
    public int compareTo(TimePointPath path) {
        Integer thisLength = this.timeStreamAnchorList.size();
        Integer thatLength = path.timeStreamAnchorList.size();
        int cmp = thisLength.compareTo(thatLength);
        if (cmp != 0) {
            return cmp;
        }
        AnchorEntity thisAnchorEntity, thatAnchorEntity;
        for (int i = 0; i < thisLength; ++i) {
            thisAnchorEntity = this.timeStreamAnchorList.get(i);
            thatAnchorEntity = path.timeStreamAnchorList.get(i);
            cmp = thisAnchorEntity.compareTo(thatAnchorEntity);
            if (cmp != 0) {
                return cmp;
            }
        }
        return 0;
    }
}
