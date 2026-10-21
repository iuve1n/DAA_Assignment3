package benchmark;

import connectivity.SCCResult;
import connectivity.StronglyConnectedComponents;
import connectivity.TopologicalResult;
import connectivity.TopologicalSort;
import graph.Graph;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.function.Supplier;
import mst.MSTAlgorithms;
import shortestpath.Dijkstra;
import shortestpath.DijkstraResult;

public class Benchmark {
    private static final int[] VERTEX_COUNTS = {10, 100, 1000};
    private static final int[] EDGE_COUNTS = {30, 500, 5000};
    private static final int WARMUP_RUNS = 1;
    private static final int MEASURED_RUNS = 5;
    private static final long SEED = 20261021L;

    private static class Measurement {
        private final String algorithm;
        private final long time;
        private final long operation1;
        private final long operation2;

        private Measurement(String algorithm, long time, long operation1, long operation2) {
            this.algorithm = algorithm;
            this.time = time;
            this.operation1 = operation1;
            this.operation2 = operation2;
        }
    }

    public static void main(String[] args) throws IOException {
        String output = args.length == 0 ? "results.csv" : args[0];
        try (PrintWriter writer = new PrintWriter(new FileWriter(output))) {
            writer.println("graph_type,vertices,edges,algorithm,average_time_ns,operation_1,operation_2");
            for (int i = 0; i < VERTEX_COUNTS.length; i++) {
                int vertices = VERTEX_COUNTS[i];
                int edges = EDGE_COUNTS[i];
                Graph undirected = createUndirected(vertices, edges, SEED + i);
                Graph directed = createDirected(vertices, edges, SEED + 100 + i);

                write(writer, "undirected", vertices, edges,
                        measure("Kruskal", () -> MSTAlgorithms.kruskal(undirected),
                                result -> result.getEdgeChecks(), result -> result.getFindCalls()));
                write(writer, "undirected", vertices, edges,
                        measure("Prim", () -> MSTAlgorithms.prim(undirected),
                                result -> result.getEdgeChecks(), result -> result.getQueuePops()));
                write(writer, "directed", vertices, edges,
                        measure("TopologicalSort", () -> TopologicalSort.sort(directed),
                                result -> result.getEdgeChecks(), result -> result.getOrder().size()));
                write(writer, "directed", vertices, edges,
                        measure("KosarajuSCC", () -> StronglyConnectedComponents.kosaraju(directed),
                                result -> result.getEdgeChecks(), result -> result.getComponents().size()));
                write(writer, "directed", vertices, edges,
                        measure("Dijkstra", () -> Dijkstra.shortestPaths(directed, 0),
                                result -> result.getEdgeChecks(), result -> result.getQueuePops()));
            }
        }
        System.out.println("Benchmark results written to " + output);
    }

    private static Graph createUndirected(int vertices, int edges, long seed) {
        Graph graph = new Graph(vertices, false);
        Random random = new Random(seed);
        boolean[][] added = new boolean[vertices][vertices];
        int count = 0;
        for (int vertex = 1; vertex < vertices; vertex++) {
            int parent = random.nextInt(vertex);
            addUndirected(graph, added, parent, vertex, random);
            count++;
        }
        while (count < edges) {
            int from = random.nextInt(vertices);
            int to = random.nextInt(vertices);
            if (from != to && !added[from][to]) {
                addUndirected(graph, added, from, to, random);
                count++;
            }
        }
        return graph;
    }

    private static void addUndirected(Graph graph, boolean[][] added, int from, int to, Random random) {
        graph.addEdge(from, to, random.nextInt(100) + 1);
        added[from][to] = true;
        added[to][from] = true;
    }

    private static Graph createDirected(int vertices, int edges, long seed) {
        Graph graph = new Graph(vertices, true);
        Random random = new Random(seed);
        boolean[][] added = new boolean[vertices][vertices];
        int count = 0;
        while (count < edges) {
            int from = random.nextInt(vertices);
            int to = random.nextInt(vertices);
            if (from != to && !added[from][to]) {
                graph.addEdge(from, to, random.nextInt(100) + 1);
                added[from][to] = true;
                count++;
            }
        }
        return graph;
    }

    private static <T> Measurement measure(String name, Supplier<T> operation,
                                          Counter<T> first, Counter<T> second) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            operation.get();
        }
        long total = 0;
        T lastResult = null;
        for (int i = 0; i < MEASURED_RUNS; i++) {
            long start = System.nanoTime();
            lastResult = operation.get();
            total += System.nanoTime() - start;
        }
        return new Measurement(name, total / MEASURED_RUNS,
                first.count(lastResult), second.count(lastResult));
    }

    private static void write(PrintWriter writer, String type, int vertices, int edges, Measurement result) {
        writer.println(type + "," + vertices + "," + edges + "," + result.algorithm + ","
                + result.time + "," + result.operation1 + "," + result.operation2);
    }

    private interface Counter<T> {
        long count(T result);
    }
}
