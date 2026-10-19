package dsu;

public class DisjointSet {
    private final int[] parent;
    private final int[] size;
    private long findCalls;
    private long unionCalls;

    public DisjointSet(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Set must contain at least one item");
        }
        parent = new int[count];
        size = new int[count];
        for (int i = 0; i < count; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int item) {
        checkItem(item);
        findCalls++;
        return findRoot(item);
    }

    public boolean union(int first, int second) {
        checkItem(first);
        checkItem(second);
        unionCalls++;
        findCalls += 2;
        int firstRoot = findRoot(first);
        int secondRoot = findRoot(second);
        if (firstRoot == secondRoot) {
            return false;
        }
        if (size[firstRoot] < size[secondRoot]) {
            int temp = firstRoot;
            firstRoot = secondRoot;
            secondRoot = temp;
        }
        parent[secondRoot] = firstRoot;
        size[firstRoot] += size[secondRoot];
        return true;
    }

    public long getFindCalls() {
        return findCalls;
    }

    public long getUnionCalls() {
        return unionCalls;
    }

    private int findRoot(int item) {
        if (parent[item] != item) {
            parent[item] = findRoot(parent[item]);
        }
        return parent[item];
    }

    private void checkItem(int item) {
        if (item < 0 || item >= parent.length) {
            throw new IndexOutOfBoundsException("Item is outside the set");
        }
    }
}
