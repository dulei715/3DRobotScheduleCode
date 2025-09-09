package hnu.dll.control.topk;

/* ==================== 内部：Yen 核心 ==================== */

import hnu.dll.structure.basic_structure.Anchor;

import java.util.List;
import java.util.stream.Collectors;

/** 中立结果载体（节点序列 + 每段边权 + 总花费），用于内部算法拼装 */
public class AnchorPathResult implements Comparable<AnchorPathResult> {
    final List<Anchor> nodes;      // 含起点与终点
    final List<Double> edgeCosts;  // 长度 = nodes.size() - 1
    final double totalCost;

    AnchorPathResult(List<Anchor> nodes, List<Double> edgeCosts, double totalCost) {
        this.nodes = nodes;
        this.edgeCosts = edgeCosts;
        this.totalCost = totalCost;
    }

    @Override public int compareTo(AnchorPathResult o) {
        int c = Double.compare(this.totalCost, o.totalCost);
        if (c != 0) return c;
        int n = Math.min(this.nodes.size(), o.nodes.size());
        for (int i = 0; i < n; i++) {
            int r = this.nodes.get(i).compareTo(o.nodes.get(i));
            if (r != 0) return r;
        }
        return Integer.compare(this.nodes.size(), o.nodes.size());
    }

    @Override public String toString() {
        return "cost=" + totalCost + ", nodes=" + nodes.stream().map(Object::toString).collect(Collectors.joining("->"));
    }
}
