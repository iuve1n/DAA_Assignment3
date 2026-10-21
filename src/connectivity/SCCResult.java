package connectivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SCCResult {
    private final List<List<Integer>> components;
    private final long edgeChecks;

    public SCCResult(List<List<Integer>> components, long edgeChecks) {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> component : components) {
            copy.add(Collections.unmodifiableList(new ArrayList<>(component)));
        }
        this.components = Collections.unmodifiableList(copy);
        this.edgeChecks = edgeChecks;
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public long getEdgeChecks() {
        return edgeChecks;
    }
}
