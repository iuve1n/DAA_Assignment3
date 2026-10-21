package shortestpath;

import graph.Edge;
import graph.Graph;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    private static class QueueEntry {
        private final int vertex;
        private final long distance;

        private QueueEntry(int vertex, long distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    public static DijkstraResult shortestPaths(Graph graph, int source) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        if (source < 0 || source >= graph.getVertexCount()) {
            throw new IndexOutOfBoundsException("Source is outside the graph");
        }
        for (Edge edge : graph.getEdges()) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("Dijkstra requires non-negative edge weights");
            }
        }

        int count = graph.getVertexCount();
        long[] distances = new long[count];
        int[] previous = new int[count];
        boolean[] visited = new boolean[count];
        Arrays.fill(distances, Long.MAX_VALUE);
        Arrays.fill(previous, -1);
        distances[source] = 0;

        PriorityQueue<QueueEntry> queue = new PriorityQueue<>(
                Comparator.comparingLong(entry -> entry.distance));
        queue.add(new QueueEntry(source, 0));
        long edgeChecks = 0;
        long queuePops = 0;

        while (!queue.isEmpty()) {
            QueueEntry entry = queue.remove();
            queuePops++;
            if (visited[entry.vertex] || entry.distance != distances[entry.vertex]) {
                continue;
            }
            visited[entry.vertex] = true;

            for (Edge edge : graph.getNeighbors(entry.vertex)) {
                edgeChecks++;
                int next = edge.getTo();
                long newDistance = entry.distance + edge.getWeight();
                if (newDistance < distances[next]) {
                    distances[next] = newDistance;
                    previous[next] = entry.vertex;
                    queue.add(new QueueEntry(next, newDistance));
                }
            }
        }
        return new DijkstraResult(source, distances, previous, edgeChecks, queuePops);
    }
}
