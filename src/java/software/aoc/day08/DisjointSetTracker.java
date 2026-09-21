package software.aoc.day08;

import java.util.List;
import java.util.stream.IntStream;

public class DisjointSetTracker implements NetworkTracker {
    private final int[] parent;
    private final int[] size;
    private int clusters;

    public DisjointSetTracker(int totalNodes) {
        this.parent = new int[totalNodes];
        this.size = new int[totalNodes];
        this.clusters = totalNodes;
        for (int i = 0; i < totalNodes; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    private int findRoot(int i) {
        if (parent[i] == i) return i;
        return parent[i] = findRoot(parent[i]);
    }

    @Override
    public boolean linkNodes(int id1, int id2) {
        int root1 = findRoot(id1);
        int root2 = findRoot(id2);

        if (root1 == root2) return false;

        if (size[root1] < size[root2]) {
            parent[root1] = root2;
            size[root2] += size[root1];
        } else {
            parent[root2] = root1;
            size[root1] += size[root2];
        }
        clusters--;
        return true;
    }

    @Override
    public List<Integer> getClusterSizes() {
        return IntStream.range(0, parent.length)
                .filter(i -> parent[i] == i)
                .mapToObj(i -> size[i])
                .toList();
    }

    @Override
    public int getRemainingClusters() {
        return clusters;
    }
}