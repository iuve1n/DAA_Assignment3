import graph.Graph;
import java.util.Arrays;
import java.util.Collections;
import shortestpath.Dijkstra;
import shortestpath.DijkstraResult;

public class DijkstraTests {
    public static void main(String[] args) {
        testShortestPaths();
        testUnreachableVertex();
        testSingleVertex();
        testNegativeWeightRejected();
        testInvalidSourceRejected();
        System.out.println("Dijkstra tests passed");
    }

    private static void testShortestPaths() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 5);

        DijkstraResult result = Dijkstra.shortestPaths(graph, 0);
        check(result.getDistanceTo(0) == 0, "source distance should be zero");
        check(result.getDistanceTo(1) == 3, "shorter route to vertex 1 should be used");
        check(result.getDistanceTo(2) == 1, "distance to vertex 2 should match");
        check(result.getDistanceTo(3) == 4, "distance to vertex 3 should match");
        check(result.getPathTo(3).equals(Arrays.asList(0, 2, 1, 3)), "path should show the shortest route");
        check(result.getEdgeChecks() == 5, "reachable outgoing edges should be checked");
    }

    private static void testUnreachableVertex() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 7);
        DijkstraResult result = Dijkstra.shortestPaths(graph, 0);
        check(!result.isReachable(2), "separate vertex should be unreachable");
        check(result.getDistanceTo(2) == Long.MAX_VALUE, "unreachable distance should use infinity");
        check(result.getPathTo(2).isEmpty(), "unreachable vertex should have no path");
    }

    private static void testSingleVertex() {
        Graph graph = new Graph(1, false);
        DijkstraResult result = Dijkstra.shortestPaths(graph, 0);
        check(result.isReachable(0) && result.getDistanceTo(0) == 0, "source reaches itself at zero cost");
        check(result.getPathTo(0).equals(Collections.singletonList(0)), "source path should contain itself");
    }

    private static void testNegativeWeightRejected() {
        Graph graph = new Graph(2, true);
        graph.addEdge(0, 1, -1);
        boolean rejected = false;
        try {
            Dijkstra.shortestPaths(graph, 0);
        } catch (IllegalArgumentException expected) {
            rejected = true;
        }
        check(rejected, "negative weight should be rejected");
    }

    private static void testInvalidSourceRejected() {
        Graph graph = new Graph(2, true);
        boolean rejected = false;
        try {
            Dijkstra.shortestPaths(graph, 2);
        } catch (IndexOutOfBoundsException expected) {
            rejected = true;
        }
        check(rejected, "source outside graph should be rejected");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
