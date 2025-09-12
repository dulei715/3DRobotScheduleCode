package hnu.dll.structure.path;

import hnu.dll.structure.AnchorEntity;
import hnu.dll.structure.basic_structure.BasicPair;

import java.util.Arrays;
import java.util.LinkedList;

public class AnchorPointPath extends Path implements Comparable<AnchorPointPath>{
    private AnchorEntity startAnchorEntity;
    private LinkedList<BasicPair<Double, AnchorEntity>> internalDataList = null;

    public AnchorPointPath(AnchorEntity startAnchorEntity) {
        this.startAnchorEntity = startAnchorEntity;
        this.internalDataList = new LinkedList<>();
    }

    public AnchorPointPath(AnchorEntity startAnchorEntity, BasicPair<Double, AnchorEntity>... internalPair) {
        this.startAnchorEntity = startAnchorEntity;
        this.internalDataList = new LinkedList<>();
        this.internalDataList.addAll(Arrays.asList(internalPair));
    }

    public void setStartAnchorEntity(AnchorEntity startAnchorEntity) {
        this.startAnchorEntity = startAnchorEntity;
    }

    public void addInternalPair(BasicPair<Double, AnchorEntity> internalData) {
        this.internalDataList.add(internalData);
    }

    public void addInternalPair(int index, BasicPair<Double, AnchorEntity> internalData, Double nextWeight) {
        if (index >= this.internalDataList.size()) {
            this.addInternalPair(internalData);
            return;
        }
        BasicPair<Double, AnchorEntity> pair = this.internalDataList.get(index);
        this.internalDataList.add(index, internalData);
        pair.setKey(nextWeight);
    }

    public AnchorEntity getLastAnchorPair() {
        return this.internalDataList.getLast().getValue();
    }


    public void addPath(AnchorPointPath path, Double weighted) {
        this.internalDataList.add(new BasicPair<>(weighted, path.startAnchorEntity));
        this.internalDataList.addAll(path.internalDataList);
    }

    /**
     * 传入的path的头是调用该函数的path的尾
     * @param path
     */
    public void addPathWithTailEqualsHead(AnchorPointPath path) {
        this.internalDataList.addAll(path.internalDataList);
    }

    /**
     * pathA的尾部是pathB的头部
     * @param pathA
     * @param pathB
     * @return
     */
    public static AnchorPointPath getCombinePathWithBothTailWeights(AnchorPointPath pathA, AnchorPointPath pathB) {
        AnchorPointPath path = new AnchorPointPath(pathA.startAnchorEntity);
        path.internalDataList.addAll(pathA.internalDataList);
        path.internalDataList.addAll(pathB.internalDataList);
        return path;
    }

    /**
     * pathA的尾部和pathB的头部之间有权重（尽管如此，盖尾部和该头部也可能重合，这时会构造出自环）
     * @param pathA
     * @param pathB
     * @return
     */
//    @Deprecated
//    public static AnchorPointPath getCombinePath(AnchorPointPath pathA, AnchorPointPath pathB, Double weight) {
//        AnchorPointPath path = new AnchorPointPath(pathA.startAnchor);
//        path.internalDataList.addAll(pathA.internalDataList);
//        path.addInternalPair(new BasicPair<>(weight, pathB.startAnchor));
//        path.internalDataList.addAll(pathB.internalDataList);
//        return path;
//    }

//    public static AnchorPointPath getCombinePath(AnchorPointPath pathA, AnchorPointPath pathB, Double jointWeight, Double tailWeight) {
//        AnchorPointPath path = new AnchorPointPath(pathA.startAnchor);
//        path.internalDataList.addAll(pathA.internalDataList);
//        path.addInternalPair(new BasicPair<>(jointWeight, pathB.startAnchor));
//        path.internalDataList.addAll(pathB.internalDataList);
//        path.internalDataList.add(new BasicPair<>(tailWeight, pathB.getLastAnchor()))
//        return path;
//    }
    // todo
    public static AnchorPointPath getCombinePathWithBothTailWeights(AnchorPointPath pathA, AnchorPointPath pathB, Double jointWeight, Double tailWeight) {
        AnchorPointPath path = new AnchorPointPath(pathA.startAnchorEntity);
        path.internalDataList.addAll(pathA.internalDataList);
        path.addInternalPair(new BasicPair<>(jointWeight, pathB.startAnchorEntity));
        path.internalDataList.addAll(pathB.internalDataList);
        path.internalDataList.add(new BasicPair<>(tailWeight, pathB.getLastAnchorPair()));
        return path;
    }

    // todo: 可以优化，这个函数直接影响compareTo函数
    public Double getWeightedSum() {
        Double result = 0D;
        for (BasicPair<Double, AnchorEntity> pair : this.internalDataList) {
            result += pair.getKey();
        }
        return result;
    }

    public AnchorEntity getStartAnchorPair() {
        return startAnchorEntity;
    }

    public LinkedList<BasicPair<Double, AnchorEntity>> getInternalDataList() {
        return internalDataList;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startAnchorEntity.getAnchor().getName());
        Double totalValue = 0D, tempWeight;
        for (BasicPair<Double, AnchorEntity> pair : this.internalDataList) {
            tempWeight = pair.getKey();
            stringBuilder.append(" --(").append(tempWeight).append(")-> ");
            totalValue += tempWeight;
            stringBuilder.append(pair.getValue());
        }
        return "sum(weight)=" + totalValue + ": " + stringBuilder;
    }

    @Override
    /**
     * 仅仅通过边权重比较
     */
    public int compareTo(AnchorPointPath path) {
        Double weightA = this.getWeightedSum();
        Double weightB = path.getWeightedSum();
        if (!weightA.equals(weightB)) {
            return weightA.compareTo(weightB);
        }
        int sizeA = this.internalDataList.size();
        int sizeB = path.internalDataList.size();
        if (sizeA != sizeB) {
            return sizeA - sizeB;
        }
        int tempCompare = this.startAnchorEntity.compareTo(path.startAnchorEntity);
        if (tempCompare != 0) {
            return tempCompare;
        }
        BasicPair<Double, AnchorEntity> internalA, internalB;
        Double tempWeightA, tempWeightB;
//        AnchorEntity tempAnchorA, tempAnchorB;
        for (int i = 0; i < sizeA; ++i) {
            internalA = this.internalDataList.get(i);
            internalB = this.internalDataList.get(i);
            tempWeightA = internalA.getKey();
            tempWeightB = internalB.getKey();
            tempCompare = tempWeightA.compareTo(tempWeightB);
            if (tempCompare != 0) {
                return tempCompare;
            }
//            tempAnchorA = internalA.getValue();
//            tempAnchorB = internalB.getValue();
            tempCompare = tempWeightA.compareTo(tempWeightB);
            if (tempCompare != 0) {
                return tempCompare;
            }
        }
        return 0;
    }

}
