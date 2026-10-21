package connectivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopologicalResult {
    private final boolean acyclic;
    private final List<Integer> order;
    private final long edgeChecks;

    public TopologicalResult(boolean acyclic, List<Integer> order, long edgeChecks) {
        this.acyclic = acyclic;
        this.order = Collections.unmodifiableList(new ArrayList<>(order));
        this.edgeChecks = edgeChecks;
    }

    public boolean isAcyclic() {
        return acyclic;
    }

    public List<Integer> getOrder() {
        return order;
    }

    public long getEdgeChecks() {
        return edgeChecks;
    }
}
