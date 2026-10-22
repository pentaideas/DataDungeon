/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

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
        ArrayDeque<Integer> frontier = new ArrayDeque<>();
        frontier.addLast(start);
        while (!frontier.isEmpty()) {
            int room = frontier.removeFirst();
            for (int neighbor : graph.neighbors(room)) {
                if (distance[neighbor] == -1) {
                    distance[neighbor] = distance[room] + 1;
                    parent[neighbor] = room;
                    frontier.addLast(neighbor);
                }
            }
        }
        return new SearchResult(distance, parent);
    }
}
