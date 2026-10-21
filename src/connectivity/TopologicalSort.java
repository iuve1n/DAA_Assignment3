package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort {
    public static TopologicalResult sort(Graph graph) {
        checkDirected(graph);
        int count = graph.getVertexCount();
        int[] indegrees = new int[count];
        long edgeChecks = 0;

        for (int vertex = 0; vertex < count; vertex++) {
            for (Edge edge : graph.getNeighbors(vertex)) {
                indegrees[edge.getTo()]++;
                edgeChecks++;
            }
        }

        Queue<Integer> ready = new ArrayDeque<>();
        for (int vertex = 0; vertex < count; vertex++) {
            if (indegrees[vertex] == 0) {
                ready.add(vertex);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            int vertex = ready.remove();
            order.add(vertex);
            for (Edge edge : graph.getNeighbors(vertex)) {
                indegrees[edge.getTo()]--;
                edgeChecks++;
                if (indegrees[edge.getTo()] == 0) {
                    ready.add(edge.getTo());
                }
            }
        }
        return new TopologicalResult(order.size() == count, order, edgeChecks);
    }

    private static void checkDirected(Graph graph) {
        if (graph == null || !graph.isDirected()) {
            throw new IllegalArgumentException("Topological sorting requires a directed graph");
        }
    }
}
