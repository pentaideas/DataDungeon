/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

import java.util.Arrays;
import java.util.Scanner;

public class LabyrinthGame {
    public static void main(String[] args) {
        MazeGraph graph = MazeGraph.sample();
        RouteFinder finder = new RouteFinder();
        RouteFinder.SearchResult result = finder.search(graph, 0);
        Scanner input = new Scanner(System.in);
        System.out.println("The Labyrinth Revealed");
        System.out.println("map, reach, route <room>, start <room>, quit");
        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            String[] parts = line.split("\\s+");
            if (line.equals("quit")) break;
            try {
                if (line.equals("map")) {
                    for (int room = 0; room < graph.size(); room++) {
                        System.out.println(room + " " + graph.name(room)
                            + ": " + Arrays.toString(graph.neighbors(room)));
                    }
                } else if (line.equals("reach")) {
                    System.out.println("Reachable: "
                        + result.reachableCount());
                } else if (parts.length == 2 && parts[0].equals("start")) {
                    int start = Integer.parseInt(parts[1]);
                    if (start < 0 || start >= graph.size()) {
                        System.out.println("Invalid start.");
                    } else {
                        result = finder.search(graph, start);
                        System.out.println("Start: " + graph.name(start));
                    }
                } else if (parts.length == 2 && parts[0].equals("route")) {
                    int target = Integer.parseInt(parts[1]);
                    System.out.println("Route: "
                        + Arrays.toString(result.pathTo(target)));
                    System.out.println("Passages: "
                        + result.distanceTo(target));
                } else {
                    System.out.println(
                        "Use map, reach, route, start, or quit.");
                }
            } catch (NumberFormatException exception) {
                System.out.println("Use an integer room number.");
            }
        }
    }
}
