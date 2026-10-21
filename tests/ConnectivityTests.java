import connectivity.SCCResult;
import connectivity.StronglyConnectedComponents;
import connectivity.TopologicalResult;
import connectivity.TopologicalSort;
import graph.Graph;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConnectivityTests {
    public static void main(String[] args) {
        testTopologicalOrder();
        testCycleDetection();
        testStronglyConnectedComponents();
        testDirectedGraphRequired();
        System.out.println("Connectivity tests passed");
    }

    private static void testTopologicalOrder() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(1, 4, 1);
        TopologicalResult result = TopologicalSort.sort(graph);
        check(result.isAcyclic(), "DAG should have a topological order");
        check(result.getOrder().size() == 5, "order should include every vertex");
        check(before(result.getOrder(), 0, 2), "0 should be before 2");
        check(before(result.getOrder(), 1, 2), "1 should be before 2");
        check(before(result.getOrder(), 2, 3), "2 should be before 3");
        check(before(result.getOrder(), 1, 4), "1 should be before 4");
    }

    private static void testCycleDetection() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        check(!TopologicalSort.sort(graph).isAcyclic(), "cycle should be detected");
    }

    private static void testStronglyConnectedComponents() {
        Graph graph = new Graph(6, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);
        graph.addEdge(4, 5, 1);

        SCCResult result = StronglyConnectedComponents.kosaraju(graph);
        List<List<Integer>> components = result.getComponents();
        check(components.size() == 3, "graph should have three SCCs");
        check(sameComponent(components, 0, 1) && sameComponent(components, 1, 2),
                "0, 1, and 2 should share an SCC");
        check(sameComponent(components, 3, 4), "3 and 4 should share an SCC");
        check(!sameComponent(components, 2, 3), "one-way connection should not merge SCCs");
        check(!sameComponent(components, 4, 5), "one-way connection should not merge SCCs");

        Set<Integer> seen = new HashSet<>();
        for (List<Integer> component : components) {
            for (int vertex : component) {
                check(seen.add(vertex), "vertex should appear in only one SCC");
            }
        }
        check(seen.size() == 6, "every vertex should appear in an SCC");
    }

    private static void testDirectedGraphRequired() {
        Graph graph = new Graph(2, false);
        boolean rejected = false;
        try {
            TopologicalSort.sort(graph);
        } catch (IllegalArgumentException expected) {
            rejected = true;
        }
        check(rejected, "topological sort should reject an undirected graph");
    }

    private static boolean before(List<Integer> order, int first, int second) {
        return order.indexOf(first) < order.indexOf(second);
    }

    private static boolean sameComponent(List<List<Integer>> components, int first, int second) {
        for (List<Integer> component : components) {
            if (component.contains(first) && component.contains(second)) {
                return true;
            }
        }
        return false;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
