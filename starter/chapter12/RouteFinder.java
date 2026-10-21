import java.util.ArrayDeque;
import java.util.Arrays;

public class RouteFinder {
    public static class SearchResult {
        private final int[] distance;
        private final int[] parent;

        private SearchResult(int[] distance, int[] parent) {
            this.distance = distance;
            this.parent = parent;
        }

        public int distanceTo(int target) {
            return target < 0 || target >= distance.length
                ? -1 : distance[target];
        }

        public int[] pathTo(int target) {
            int length = distanceTo(target);
            if (length < 0) return new int[0];
            int[] path = new int[length + 1];
            for (int index = length; index >= 0; index--) {
                path[index] = target;
                target = parent[target];
            }
            return path;
        }

        public int reachableCount() {
            int count = 0;
            for (int steps : distance) if (steps >= 0) count++;
            return count;
        }
    }

    public SearchResult search(MazeGraph graph, int start) {
        int[] distance = new int[graph.size()];
        int[] parent = new int[graph.size()];
        Arrays.fill(distance, -1);
        Arrays.fill(parent, -1);
        if (start < 0 || start >= graph.size()) {
            return new SearchResult(distance, parent);
        }
        distance[start] = 0;
        // TODO: breadth-first search using a FIFO queue.
        return new SearchResult(distance, parent);
    }
}
