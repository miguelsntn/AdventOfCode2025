package software.aoc.day08;

import java.util.List;
import java.util.stream.IntStream;

public class DisjointSetTracker implements NetworkTracker {
    private final int[] parent;
    private final int[] clusterSize;
    private int activeClusters;

    public DisjointSetTracker(int totalNodes) {
        this.parent = new int[totalNodes];
        this.clusterSize = new int[totalNodes];
        this.activeClusters = totalNodes;

        for (int i = 0; i < totalNodes; i++) {
            parent[i] = i;
            clusterSize[i] = 1;
        }
    }

    private int findRoot(int nodeId) {
        if (parent[nodeId] == nodeId) {
            return nodeId;
        }
        parent[nodeId] = findRoot(parent[nodeId]);
        return parent[nodeId];
    }

    @Override
    public boolean linkNodes(int id1, int id2) {
        int root1 = findRoot(id1);
        int root2 = findRoot(id2);

        if (root1 == root2) {
            return false;
        }

        if (clusterSize[root1] < clusterSize[root2]) {
            parent[root1] = root2;
            clusterSize[root2] += clusterSize[root1];
        } else {
            parent[root2] = root1;
            clusterSize[root1] += clusterSize[root2];
        }

        activeClusters--;
        return true;
    }

    @Override
    public List<Integer> getClusterSizes() {
        return IntStream.range(0, parent.length)
                .filter(i -> parent[i] == i)
                .mapToObj(i -> clusterSize[i])
                .toList();
    }

    @Override
    public int getRemainingClusters() {
        return activeClusters;
    }
}