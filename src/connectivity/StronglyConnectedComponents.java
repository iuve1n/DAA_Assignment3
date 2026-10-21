package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StronglyConnectedComponents {
    public static SCCResult kosaraju(Graph graph) {
        checkDirected(graph);
        int count = graph.getVertexCount();
        boolean[] visited = new boolean[count];
        List<Integer> finishOrder = new ArrayList<>();
        long[] edgeChecks = {0};

        for (int vertex = 0; vertex < count; vertex++) {
            if (!visited[vertex]) {
                finishDfs(graph, vertex, visited, finishOrder, edgeChecks);
            }
        }

        List<List<Integer>> reversed = new ArrayList<>();
        for (int vertex = 0; vertex < count; vertex++) {
            reversed.add(new ArrayList<>());
        }
        for (Edge edge : graph.getEdges()) {
            reversed.get(edge.getTo()).add(edge.getFrom());
            edgeChecks[0]++;
        }

        Arrays.fill(visited, false);
        List<List<Integer>> components = new ArrayList<>();
        for (int i = finishOrder.size() - 1; i >= 0; i--) {
            int vertex = finishOrder.get(i);
            if (!visited[vertex]) {
                List<Integer> component = new ArrayList<>();
                collectDfs(vertex, reversed, visited, component, edgeChecks);
                components.add(component);
            }
        }
        return new SCCResult(components, edgeChecks[0]);
    }

    private static void finishDfs(Graph graph, int vertex, boolean[] visited,
                                  List<Integer> finishOrder, long[] edgeChecks) {
        visited[vertex] = true;
        for (Edge edge : graph.getNeighbors(vertex)) {
            edgeChecks[0]++;
            if (!visited[edge.getTo()]) {
                finishDfs(graph, edge.getTo(), visited, finishOrder, edgeChecks);
            }
        }
        finishOrder.add(vertex);
    }

    private static void collectDfs(int vertex, List<List<Integer>> reversed,
                                   boolean[] visited, List<Integer> component, long[] edgeChecks) {
        visited[vertex] = true;
        component.add(vertex);
        for (int neighbor : reversed.get(vertex)) {
            edgeChecks[0]++;
            if (!visited[neighbor]) {
                collectDfs(neighbor, reversed, visited, component, edgeChecks);
            }
        }
    }

    private static void checkDirected(Graph graph) {
        if (graph == null || !graph.isDirected()) {
            throw new IllegalArgumentException("SCC detection requires a directed graph");
        }
    }
}
