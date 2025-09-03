package hnu.dll.structure.path;

import hnu.dll.config.Constant;
import hnu.dll.structure.Anchor;
import hnu.dll.structure.basic_structure.BasicPair;

import java.util.LinkedList;

public class TimePointPath extends Path implements Comparable<TimePointPath>{

    // 第一个是startTime所在位置，每个相邻元素之间间隔TimeUnit大小
    private LinkedList<Anchor> timeStreamAnchorList;
    // todo: add entity

    private TimePointPath(LinkedList<Anchor> timeStreamAnchorList) {
        this.timeStreamAnchorList = timeStreamAnchorList;
    }

    public Integer getTimeLength() {
        return this.timeStreamAnchorList.size();
    }

    public Anchor getAnchorByIndex(Integer index) {
        return this.timeStreamAnchorList.get(index);
    }

    /**
     * 将以时长为权重的路径转化为时间序列
     * @param anchorPointPath
     * @return
     */
    public static TimePointPath getTimePointPathByAnchorPointPath(AnchorPointPath anchorPointPath) {
        LinkedList<Anchor> timeStreamAnchorList = new LinkedList<>();
        Anchor beforeAnchor = anchorPointPath.getStartAnchor(), nextAnchor;
        timeStreamAnchorList.add(beforeAnchor);
        Integer edgeTimeLength;
        for (BasicPair<Double, Anchor> segment : anchorPointPath.getInternalDataList()) {
            // 将边长展成时序长度
            edgeTimeLength = (int) Math.ceil(segment.getKey() / Constant.TimeUnit);
            for (int i = 0; i < edgeTimeLength - 1; ++i) {
                timeStreamAnchorList.add(beforeAnchor);
            }
            nextAnchor = segment.getValue();
            timeStreamAnchorList.add(nextAnchor);
            beforeAnchor = nextAnchor;
        }
        return new TimePointPath(timeStreamAnchorList);
    }

    public void insertAnchor(Anchor anchor, Integer timeIndex, Integer timeSlotLength) {
        for (int i = 0; i < timeSlotLength; ++i) {
            this.timeStreamAnchorList.add(timeIndex, anchor);
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
        Anchor thisAnchor, thatAnchor;
        for (int i = 0; i < thisLength; ++i) {
            thisAnchor = this.timeStreamAnchorList.get(i);
            thatAnchor = path.timeStreamAnchorList.get(i);
            cmp = thisAnchor.compareTo(thatAnchor);
            if (cmp != 0) {
                return cmp;
            }
        }
        return 0;
    }
}
