package hnu.dll.structure;

import hnu.dll.config.Constant;
import hnu.dll.structure.basic_structure.BasicPair;

import java.util.LinkedList;
import java.util.List;

public class TimePointPath implements Comparable<TimePointPath>{

    private Double startTime;
    // 第一个是startTime所在位置，每个相邻元素之间间隔TimeUnit大小
    private LinkedList<Anchor> timeStreamAnchorList;

    private TimePointPath(Double startTime, LinkedList<Anchor> timeStreamAnchorList) {
        this.startTime = startTime;
        this.timeStreamAnchorList = timeStreamAnchorList;
    }

    /**
     * 将以时长为权重的路径转化为时间序列
     * @param startTime
     * @param anchorPointPath
     * @return
     */
    public static TimePointPath getTimePointPathByAnchorPointPath(Double startTime, AnchorPointPath anchorPointPath) {
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
        return new TimePointPath(startTime, timeStreamAnchorList);
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
