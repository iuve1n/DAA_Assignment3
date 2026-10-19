import dsu.DisjointSet;
import graph.Graph;
import mst.MSTAlgorithms;
import mst.MSTResult;

public class MSTTests {
    public static void main(String[] args) {
        testDisjointSet();
        testConnectedGraph();
        testMultipleMinimumTrees();
        testDisconnectedGraph();
        testSingleVertex();
        testMstRejectsDirectedGraph();
        System.out.println("MST and DSU tests passed");
    }

    private static void testDisjointSet() {
        DisjointSet sets = new DisjointSet(5);
        check(sets.union(0, 1), "first union should join two sets");
        check(sets.union(1, 2), "second union should join two sets");
        check(!sets.union(0, 2), "union should report when items are already together");
        check(sets.find(0) == sets.find(2), "connected items should have the same root");
        check(sets.getFindCalls() == 8, "DSU should count find work from find and union");
        check(sets.getUnionCalls() == 3, "DSU should count each union call");
    }

    private static void testConnectedGraph() {
        Graph graph = new Graph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);
        graph.addEdge(0, 3, 10);
        graph.addEdge(0, 2, 5);

        MSTResult kruskal = MSTAlgorithms.kruskal(graph);
        MSTResult prim = MSTAlgorithms.prim(graph);
        check(kruskal.isConnected() && prim.isConnected(), "connected graph should have an MST");
        check(kruskal.getTotalWeight() == 6, "Kruskal should find the expected MST weight");
        check(prim.getTotalWeight() == kruskal.getTotalWeight(), "MST algorithms should agree");
        check(kruskal.getEdges().size() == 3, "MST should have V - 1 edges");
    }

    private static void testMultipleMinimumTrees() {
        Graph graph = new Graph(3, false);
        graph.addEdge(0, 1, 4);
        graph.addEdge(1, 2, 4);
        graph.addEdge(0, 2, 4);
        MSTResult kruskal = MSTAlgorithms.kruskal(graph);
        MSTResult prim = MSTAlgorithms.prim(graph);
        check(kruskal.isConnected() && prim.isConnected(), "equal-weight graph should be connected");
        check(kruskal.getTotalWeight() == 8, "Kruskal should handle tied edge weights");
        check(prim.getTotalWeight() == kruskal.getTotalWeight(), "tied weights should give equal MST totals");
    }

    private static void testDisconnectedGraph() {
        Graph graph = new Graph(4, false);
        graph.addEdge(0, 1, 3);
        graph.addEdge(2, 3, 1);
        check(!MSTAlgorithms.kruskal(graph).isConnected(), "Kruskal should detect disconnected graph");
        check(!MSTAlgorithms.prim(graph).isConnected(), "Prim should detect disconnected graph");
    }

    private static void testSingleVertex() {
        Graph graph = new Graph(1, false);
        MSTResult kruskal = MSTAlgorithms.kruskal(graph);
        MSTResult prim = MSTAlgorithms.prim(graph);
        check(kruskal.isConnected() && prim.isConnected(), "single vertex graph is connected");
        check(kruskal.getTotalWeight() == 0 && prim.getTotalWeight() == 0,
                "single vertex MST has zero weight");
    }

    private static void testMstRejectsDirectedGraph() {
        Graph graph = new Graph(2, true);
        graph.addEdge(0, 1, 1);
        boolean rejected = false;
        try {
            MSTAlgorithms.prim(graph);
        } catch (IllegalArgumentException expected) {
            rejected = true;
        }
        check(rejected, "MST should reject a directed graph");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
