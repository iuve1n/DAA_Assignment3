package mst;

import dsu.DisjointSet;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class MSTAlgorithms {
    public static MSTResult kruskal(Graph graph) {
        checkUndirected(graph);
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        long[] comparisons = {0};
        sortedEdges.sort((first, second) -> {
            comparisons[0]++;
            return Integer.compare(first.getWeight(), second.getWeight());
        });

        DisjointSet sets = new DisjointSet(graph.getVertexCount());
        List<Edge> treeEdges = new ArrayList<>();
        long totalWeight = 0;
        long edgeChecks = 0;

        for (Edge edge : sortedEdges) {
            if (treeEdges.size() == graph.getVertexCount() - 1) {
                break;
            }
            edgeChecks++;
            if (sets.union(edge.getFrom(), edge.getTo())) {
                treeEdges.add(edge);
                totalWeight += edge.getWeight();
            }
        }

        boolean connected = treeEdges.size() == graph.getVertexCount() - 1;
        return new MSTResult(totalWeight, treeEdges, connected, edgeChecks, comparisons[0],
                sets.getFindCalls(), sets.getUnionCalls(), 0, 0);
    }

    public static MSTResult prim(Graph graph) {
        checkUndirected(graph);
        boolean[] visited = new boolean[graph.getVertexCount()];
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        List<Edge> treeEdges = new ArrayList<>();
        long totalWeight = 0;
        long edgeChecks = 0;
        long queuePushes = 0;
        long queuePops = 0;

        visited[0] = true;
        for (Edge edge : graph.getNeighbors(0)) {
            edgeChecks++;
            queue.add(edge);
            queuePushes++;
        }

        while (!queue.isEmpty() && treeEdges.size() < graph.getVertexCount() - 1) {
            Edge edge = queue.remove();
            queuePops++;
            if (visited[edge.getTo()]) {
                continue;
            }

            visited[edge.getTo()] = true;
            treeEdges.add(edge);
            totalWeight += edge.getWeight();

            for (Edge next : graph.getNeighbors(edge.getTo())) {
                edgeChecks++;
                if (!visited[next.getTo()]) {
                    queue.add(next);
                    queuePushes++;
                }
            }
        }

        boolean connected = treeEdges.size() == graph.getVertexCount() - 1;
        return new MSTResult(totalWeight, treeEdges, connected, edgeChecks, 0, 0, 0,
                queuePushes, queuePops);
    }

    private static void checkUndirected(Graph graph) {
        if (graph == null || graph.isDirected()) {
            throw new IllegalArgumentException("MST algorithms require an undirected graph");
        }
    }
}
