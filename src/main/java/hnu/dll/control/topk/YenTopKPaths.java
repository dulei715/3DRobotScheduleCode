package hnu.dll.control.topk;

import hnu.dll.structure.AnchorEntity;
import hnu.dll.structure.TimeWeightedGraph;
import hnu.dll.structure.basic_structure.Anchor;
import hnu.dll.structure.basic_structure.BasicPair;
import hnu.dll.structure.path.AnchorPointPath;

import java.util.*;

public class YenTopKPaths {
    /* ==================== 对外主入口 ==================== */

    /** 使用默认的 AnchorEntity 构造（Entity 置为 null，可自行替换） */
    public static List<AnchorPointPath> kShortestAnchorPointPaths(
            TimeWeightedGraph g, Anchor s, Anchor t, int kSize) {
        return kShortestAnchorPointPaths(g, s, t, kSize, a -> new AnchorEntity(a, null));
    }

    /** 允许自定义 Anchor -> AnchorEntity 的映射（如绑定业务 Entity） */
    public static List<AnchorPointPath> kShortestAnchorPointPaths(
            TimeWeightedGraph g, Anchor s, Anchor t, int kSize,
            AnchorEntityConvertor converter) {
        List<AnchorPathResult> raw = yen(g, s, t, kSize);
        List<AnchorPointPath> out = new ArrayList<>(raw.size());
        for (AnchorPathResult r : raw) out.add(toAnchorPointPath(r, converter));
        return out;
    }

    private static List<AnchorPathResult> yen(TimeWeightedGraph g, Anchor s, Anchor t, int kSize) {
        if (kSize <= 0) return Collections.emptyList();

        AnchorPathResult first = dijkstra(g, s, t, Collections.emptySet(), Collections.emptySet());
        if (first == null) return Collections.emptyList();

        List<AnchorPathResult> anchorPathResultList = new ArrayList<>();
        anchorPathResultList.add(first);

        PriorityQueue<AnchorPathResult> anchorPathResultQueue = new PriorityQueue<>();
        Set<String> seen = new HashSet<>();
        seen.add(keyOf(first.nodes));

        for (int kth = 1; kth < kSize; ++kth) {
            AnchorPathResult last = anchorPathResultList.get(kth - 1);
            List<Anchor> nodes = last.nodes;

            for (int i = 0; i < nodes.size() - 1; ++i) {
                Anchor spurNode = nodes.get(i);
                List<Anchor> root = new ArrayList<>(nodes.subList(0, i + 1));

                // 禁止边：与 root 前缀一致且在 spur 处延续相同下一边
                Set<EdgeKey> forbiddenEdges = new HashSet<>();
                for (AnchorPathResult p : anchorPathResultList) {
                    if (p.nodes.size() > i && p.nodes.subList(0, i + 1).equals(root)) {
                        forbiddenEdges.add(new EdgeKey(p.nodes.get(i), p.nodes.get(i + 1)));
                    }
                }

                // 禁止节点：root 上除 spurNode 外，避免回到 root 形成环
                Set<Anchor> forbiddenNodes = new HashSet<>(root);
                forbiddenNodes.remove(spurNode);

                AnchorPathResult spurPath = dijkstra(g, spurNode, t, forbiddenNodes, forbiddenEdges);
                if (spurPath == null) continue;

                // 合并 root + spur（去掉重复 spurNode）
                List<Anchor> totalNodes = new ArrayList<>(root);
                totalNodes.addAll(spurPath.nodes.subList(1, spurPath.nodes.size()));

                // 计算边权列表与总代价
                List<Double> rootEdges = prefixEdgeCosts(g, root);
                if (rootEdges == null) continue;
                List<Double> totalEdges = new ArrayList<>(rootEdges);
                totalEdges.addAll(spurPath.edgeCosts);

                double totalCost = sum(rootEdges) + spurPath.totalCost;

                AnchorPathResult candidate = new AnchorPathResult(totalNodes, totalEdges, totalCost);
                if (seen.add(keyOf(totalNodes))) {
                    anchorPathResultQueue.offer(candidate);
                }
            }

            if (anchorPathResultQueue.isEmpty()) break;
            anchorPathResultList.add(anchorPathResultQueue.poll());
        }

        return anchorPathResultList;
    }

    /** Dijkstra（支持禁止节点与禁止边） */
    private static AnchorPathResult dijkstra(TimeWeightedGraph g,
                                             Anchor s, Anchor t,
                                             Set<Anchor> forbiddenNodes,
                                             Set<EdgeKey> forbiddenEdges) {
        if (forbiddenNodes.contains(s) || forbiddenNodes.contains(t)) return null;

        Map<Anchor, Double> dist = new HashMap<>();
        Map<Anchor, Anchor> prev = new HashMap<>();
        PriorityQueue<Map.Entry<Anchor, Double>> pq =
                new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue));

        dist.put(s, 0.0);
        pq.offer(new AbstractMap.SimpleEntry<>(s, 0.0));

        while (!pq.isEmpty()) {
            Map.Entry<Anchor, Double> cur = pq.poll();
            Anchor u = cur.getKey();
            double du = cur.getValue();
            if (du != dist.getOrDefault(u, Double.POSITIVE_INFINITY)) continue;
            if (u.equals(t)) break;

            if (forbiddenNodes.contains(u)) continue;

            for (Map.Entry<Anchor, Double> e : g.getOutgoing(u).entrySet()) {
                Anchor v = e.getKey();
                if (forbiddenNodes.contains(v)) continue;
                if (forbiddenEdges.contains(new EdgeKey(u, v))) continue;

                Double wObj = e.getValue();
                if (wObj == null) continue;
                double w = wObj;
                if (w < 0) throw new IllegalArgumentException("Edge weight must be non-negative");

                double nd = du + w;
                if (nd < dist.getOrDefault(v, Double.POSITIVE_INFINITY)) {
                    dist.put(v, nd);
                    prev.put(v, u);
                    pq.offer(new AbstractMap.SimpleEntry<>(v, nd));
                }
            }
        }

        if (!dist.containsKey(t)) return null;

        // 重建节点序列
        LinkedList<Anchor> nodes = new LinkedList<>();
        for (Anchor x = t; x != null; x = prev.get(x)) nodes.addFirst(x);

        // 边权与总代价
        List<Double> edgeCosts = new ArrayList<>(Math.max(0, nodes.size() - 1));
        double total = 0.0;
        for (int i = 0; i + 1 < nodes.size(); i++) {
            Double w = g.getWeight(nodes.get(i), nodes.get(i + 1));
            if (w == null) return null;
            edgeCosts.add(w);
            total += w;
        }
        return new AnchorPathResult(nodes, edgeCosts, total);
    }

    private static List<Double> prefixEdgeCosts(TimeWeightedGraph g, List<Anchor> nodes) {
        if (nodes.size() <= 1) return new ArrayList<>();
        List<Double> edges = new ArrayList<>(nodes.size() - 1);
        for (int i = 0; i + 1 < nodes.size(); i++) {
            Double w = g.getWeight(nodes.get(i), nodes.get(i + 1));
            if (w == null) return null;
            edges.add(w);
        }
        return edges;
    }

    private static double sum(List<Double> xs) { double s = 0.0; for (double v : xs) s += v; return s; }

    private static String keyOf(List<Anchor> nodes) {
        // 依赖 Anchor.equals/hashCode 的稳定性；这里做一个简单串键
        StringBuilder sb = new StringBuilder(nodes.size() * 8);
        for (Anchor a : nodes) sb.append('[').append(a.hashCode()).append(']');
        return sb.toString();
    }

    /* ==================== 适配：中立结果 → AnchorPointPath ==================== */
    private static AnchorPointPath toAnchorPointPath(AnchorPathResult r, AnchorEntityConvertor conv) {
        List<Anchor> nodes = r.nodes;
        List<Double> costs = r.edgeCosts;

        AnchorPointPath app = new AnchorPointPath(conv.toAnchorEntity(nodes.get(0)));
        for (int i = 0; i < costs.size(); i++) {
            Anchor next = nodes.get(i + 1);
            app.addInternalPair(new BasicPair<>(costs.get(i), conv.toAnchorEntity(next)));
        }
        return app;
    }
}
