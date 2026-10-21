package shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DijkstraResult {
    private final int source;
    private final long[] distances;
    private final int[] previous;
    private final long edgeChecks;
    private final long queuePops;

    public DijkstraResult(int source, long[] distances, int[] previous,
                          long edgeChecks, long queuePops) {
        this.source = source;
        this.distances = distances.clone();
        this.previous = previous.clone();
        this.edgeChecks = edgeChecks;
        this.queuePops = queuePops;
    }

    public int getSource() {
        return source;
    }

    public long getDistanceTo(int vertex) {
        return distances[vertex];
    }

    public boolean isReachable(int vertex) {
        return distances[vertex] != Long.MAX_VALUE;
    }

    public List<Integer> getPathTo(int vertex) {
        if (!isReachable(vertex)) {
            return Collections.emptyList();
        }
        List<Integer> path = new ArrayList<>();
        int current = vertex;
        while (current != -1) {
            path.add(current);
            if (current == source) {
                break;
            }
            current = previous[current];
        }
        Collections.reverse(path);
        return Collections.unmodifiableList(path);
    }

    public long[] getDistances() {
        return distances.clone();
    }

    public long getEdgeChecks() {
        return edgeChecks;
    }

    public long getQueuePops() {
        return queuePops;
    }
}
