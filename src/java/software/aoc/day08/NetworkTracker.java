package software.aoc.day08;

import java.util.List;

public interface NetworkTracker {
    boolean linkNodes(int id1, int id2);
    List<Integer> getClusterSizes();
    int getRemainingClusters();
}