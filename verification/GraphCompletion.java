import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GraphCompletion {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void verify(int[][] edges) {
        int n = edges.length;
        String[] names = new String[n];
        int[][] expected = new int[n][n];
        for (int room = 0; room < n; room++) {
            names[room] = "room-" + room;
            Arrays.fill(expected[room], 1000000);
            for (int neighbor : edges[room]) expected[room][neighbor] = 1;
            expected[room][room] = 0;
        }
        for (int via = 0; via < n; via++) {
            for (int from = 0; from < n; from++) {
                for (int to = 0; to < n; to++) {
                    expected[from][to] = Math.min(expected[from][to],
                        expected[from][via] + expected[via][to]);
                }
            }
        }
        MazeGraph graph = new MazeGraph(names, edges);
        RouteFinder finder = new RouteFinder();
        for (int start = -1; start <= n; start++) {
            RouteFinder.SearchResult result = finder.search(graph, start);
            int reachable = 0;
            for (int target = 0; target < n; target++) {
                int distance = start < 0 || start == n
                    || expected[start][target] == 1000000
                    ? -1 : expected[start][target];
                check(result.distanceTo(target) == distance, "oracle distance");
                int[] path = result.pathTo(target);
                if (distance < 0) {
                    check(path.length == 0, "unreachable route");
                } else {
                    reachable++;
                    check(path.length == distance + 1, "shortest path length");
                    check(path[0] == start && path[path.length - 1] == target,
                        "path endpoints");
                    for (int step = 1; step < path.length; step++) {
                        boolean exists = false;
                        for (int neighbor : edges[path[step - 1]]) {
                            if (neighbor == path[step]) exists = true;
                        }
                        check(exists, "route follows directed edges");
                    }
                    path[0] = -999;
                    check(result.pathTo(target)[0] == start, "result isolation");
                }
                check(Arrays.equals(graph.neighbors(target), edges[target]),
                    "graph unchanged");
            }
            check(result.reachableCount() == reachable, "reachable count");
            check(result.distanceTo(-1) == -1, "invalid target");
            check(result.pathTo(n).length == 0, "target outside graph");
        }
    }

    public static void main(String[] args) {
        verify(new int[][]{});
        verify(new int[][]{{}});
        verify(new int[][]{{0, 1, 1}, {2}, {0}, {}});
        verify(new int[][]{{1, 2}, {3}, {4}, {4}, {}});
        Random random = new Random(1212);
        for (int trial = 0; trial < 120; trial++) {
            int n = 1 + random.nextInt(18);
            int[][] edges = new int[n][];
            for (int from = 0; from < n; from++) {
                ArrayList<Integer> list = new ArrayList<>();
                for (int to = 0; to < n; to++) {
                    if (random.nextInt(5) == 0) list.add(to);
                }
                edges[from] = list.stream().mapToInt(Integer::intValue).toArray();
            }
            verify(edges);
        }
        RouteFinder finder = new RouteFinder();
        RouteFinder.SearchResult first = finder.search(MazeGraph.sample(), 0);
        check(Arrays.equals(first.pathTo(6), new int[]{0, 1, 3, 5, 6}),
            "sample tie-breaking");
        finder.search(MazeGraph.sample(), 7);
        check(first.distanceTo(6) == 4, "earlier result preserved");
        System.out.println("Graph completion passed");
    }
}
