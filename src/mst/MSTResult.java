package mst;

import graph.Edge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MSTResult {
    private final long totalWeight;
    private final List<Edge> edges;
    private final boolean connected;
    private final long edgeChecks;
    private final long comparisons;
    private final long findCalls;
    private final long unionCalls;
    private final long queuePushes;
    private final long queuePops;

    public MSTResult(long totalWeight, List<Edge> edges, boolean connected, long edgeChecks,
                     long comparisons, long findCalls, long unionCalls,
                     long queuePushes, long queuePops) {
        this.totalWeight = totalWeight;
        this.edges = Collections.unmodifiableList(new ArrayList<>(edges));
        this.connected = connected;
        this.edgeChecks = edgeChecks;
        this.comparisons = comparisons;
        this.findCalls = findCalls;
        this.unionCalls = unionCalls;
        this.queuePushes = queuePushes;
        this.queuePops = queuePops;
    }

    public long getTotalWeight() {
        return totalWeight;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public boolean isConnected() {
        return connected;
    }

    public long getEdgeChecks() {
        return edgeChecks;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getFindCalls() {
        return findCalls;
    }

    public long getUnionCalls() {
        return unionCalls;
    }

    public long getQueuePushes() {
        return queuePushes;
    }

    public long getQueuePops() {
        return queuePops;
    }
}
