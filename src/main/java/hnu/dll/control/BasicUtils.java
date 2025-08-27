package hnu.dll.control;

import hnu.dll.config.Constant;

public class BasicUtils {

    /**
     * 根据z坐标获取所在层
     * @param layer
     * @return
     */
    public static Double getZIndex(Integer layer) {
        return Constant.NeighboringLayersDistance * (layer - 1);
    }

    /**
     * 根据所在层获取z坐标
     * @param zIndex
     * @return
     */
    public static Integer getLayer(Double zIndex) {
        double tempLayer = zIndex / Constant.NeighboringLayersDistance + 1;
        return (int)Math.round(tempLayer);
    }


    /**
     * 将时间段转成单位时间份数（有损耗）
     * @param timeCost
     * @return
     */
    public static Integer toUnitTimeShareSize(Double timeCost) {
        return (int)Math.ceil(timeCost / Constant.TimeUnit);
    }

    /**
     * 将单位时间份数转成真实时间段（有损耗）
     * @param timeShareSize
     * @return
     */
    public static Double toRealTimeCost(Integer timeShareSize) {
        return Constant.TimeUnit * timeShareSize;
    }


}
