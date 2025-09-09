package hnu.dll.control;

import hnu.dll.basic_entity.ThreeDLocation;
import hnu.dll.config.Constant;
import hnu.dll.entity.Robot;
import hnu.dll.entity.Task;
import hnu.dll.structure.SortedPathStructure;
import hnu.dll.structure.TimeWeightedGraph;
import hnu.dll.structure.basic_structure.BasicPair;
import hnu.dll.structure.graph.BipartiteGraph;
import hnu.dll.structure.path.AnchorPointPath;
import hnu.dll.structure.path.TimePointPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BasicFunctions {

    // 暂不支持存在地下层的情况
    public static Integer getElevatorRunningTimeSlots(Integer startLayer, Integer endLayer, Double averageRunningVelocity) {
        Double realTime = (endLayer - startLayer) * Constant.NeighboringLayersDistance / averageRunningVelocity;
        return BasicUtils.toUnitTimeShareSize(realTime);
    }

    public static Integer getLayer(ThreeDLocation location) {
        Double zIndex = location.getzIndex();
        double layerDouble = zIndex / Constant.NeighboringLayersDistance + 1;
        return (int)Math.round(layerDouble);
    }

    public static Integer getMaximalTimeSlotLength(Set<SortedPathStructure<TimePointPath>> pathStructureSet) {
        Integer result = 0;
        TimePointPath path;
        for (SortedPathStructure<TimePointPath> pathStructure : pathStructureSet) {
            path = pathStructure.getFirst();
            result = Math.max(path.getTimeLength(), result);
        }
        return result;
    }

    /**
     * 使用 Dijkstra（非负权）+ Yen 框架；
     * 候选路径 tie-break 时优先用 Anchor.compareTo（你已实现），从而获得稳定顺序；
     * 结果为无环路径（loopless）；
     * 若你的图要“无向”，请对每条边 addElement(u,v,w) 同时再 addElement(v,u,w)。
     * @param graph
     * @param startLocation
     * @param endLocation
     * @param topKSize
     * @return
     */
    public static List<AnchorPointPath> getTopKShortestPath(TimeWeightedGraph graph, ThreeDLocation startLocation, ThreeDLocation endLocation, Integer topKSize) {
        List<AnchorPointPath> result = null;
        // todo: return top k nearest paths
        return result;
    }

    public static List<BasicPair<Task, Robot>> getMatchByKuhnMunkres(BipartiteGraph<Task, Robot, Double> graph) {
        // 1) 左/右端点与索引
        List<Task> left = graph.getPartAList();
        List<Robot> right = graph.getPartBList();
        int nL = left.size(), nR = right.size();
        int n = Math.max(nL, nR); // KM 需要方阵

        // 2) 构造成本矩阵（最小化）：不可边用 +INF，补位用 0
        final double INF = 1e15;
        double[][] cost = new double[n][n];
        for (int i = 0; i < n; i++) Arrays.fill(cost[i], 0.0); // 补位行/列填 0

        for (int i = 0; i < nL; i++) {
            for (int j = 0; j < nR; j++) {
                Double w = graph.getWeight(left.get(i), right.get(j));
                cost[i][j] = (w == null || (w.isNaN())) ? INF : w;
            }
        }

        // 3) Hungarian（最小化版本，经典 u/v/p/way 实现；矩阵为 n×n）
        int[] matchR = hungarianMin(cost, INF); // matchR[j] = i（列 j 匹到行 i），-1 表未匹配

        // 4) 输出真实匹配（过滤补位与 INF 边）
        List<BasicPair<Task, Robot>> res = new ArrayList<>();
        for (int j = 0; j < nR; j++) {
            int i = matchR[j];
            if (i >= 0 && i < nL && cost[i][j] < INF / 2) {
                res.add(new BasicPair<>(left.get(i), right.get(j)));
            }
        }
        return res;
    }

    /* ---------------- Hungarian / KM（最小化） ---------------- */

    /**
     * 经典 Hungarian（最小化）：输入 n×n 成本矩阵，返回列到行的匹配 matchR（-1 表未匹配）。
     * 允许出现 INF 表示不可达；若某列全 INF，将保持 -1 或被迫匹到 INF（由实现决定）。
     */
    private static int[] hungarianMin(double[][] a, final double INF) {
        int n = a.length;
        // 使用 1-based 数组以贴合经典伪码
        double[] u = new double[n + 1];
        double[] v = new double[n + 1];
        int[] p = new int[n + 1];     // p[j]：列 j 当前匹配到的行
        int[] way = new int[n + 1];   // 用于增广的前驱列

        for (int i = 1; i <= n; i++) {
            p[0] = i;
            int j0 = 0;
            double[] minv = new double[n + 1];
            Arrays.fill(minv, INF);
            boolean[] used = new boolean[n + 1];
            Arrays.fill(used, false);

            do {
                used[j0] = true;
                int i0 = p[j0];
                double delta = INF;
                int j1 = 0;

                for (int j = 1; j <= n; j++) {
                    if (used[j]) continue;
                    double cur = a[i0 - 1][j - 1] - u[i0] - v[j];
                    if (cur < minv[j]) {
                        minv[j] = cur;
                        way[j] = j0;
                    }
                    if (minv[j] < delta) {
                        delta = minv[j];
                        j1 = j;
                    }
                }

                for (int j = 0; j <= n; j++) {
                    if (used[j]) {
                        u[p[j]] += delta;
                        v[j] -= delta;
                    } else {
                        minv[j] -= delta;
                    }
                }
                j0 = j1;
            } while (p[j0] != 0);

            // 增广：沿 way 回溯
            do {
                int j1 = way[j0];
                p[j0] = p[j1];
                j0 = j1;
            } while (j0 != 0);
        }

        // p[j] = i  (列 j 匹到行 i)，j ∈ [1..n]
        int[] matchR = new int[n];
        Arrays.fill(matchR, -1);
        for (int j = 1; j <= n; j++) {
            if (p[j] != 0) matchR[j - 1] = p[j] - 1;
        }
        return matchR;
    }

}
