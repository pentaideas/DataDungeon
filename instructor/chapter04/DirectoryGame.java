/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

import java.util.Scanner;

public class DirectoryGame {
    public static void main(String[] args) {
        RoomDirectory directory = RoomDirectory.sample();
        Scanner input = new Scanner(System.in);
        System.out.println("The Ordered Directory");
        System.out.println("Commands: levels, exact <number>,");
        System.out.println("ceiling <number>, "
            + "add <number> <height> <name>, quit");
        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            if (line.equals("quit")) break;
            String[] parts = line.split("\\s+", 4);
            try {
                if (line.equals("levels")) {
                    System.out.println(directory.levels());
                    System.out.println("Size: " + directory.size());
                } else if (parts.length == 2
                        && (parts[0].equals("exact")
                        || parts[0].equals("ceiling"))) {
                    int number = Integer.parseInt(parts[1]);
                    String result = parts[0].equals("exact")
                        ? directory.exact(number) : directory.ceiling(number);
                    System.out.println(result == null ? "No room." : result);
                    System.out.println("Horizontal steps: "
                        + directory.searchSteps());
                } else if (parts.length == 4 && parts[0].equals("add")) {
                    int number = Integer.parseInt(parts[1]);
                    int height = Integer.parseInt(parts[2]);
                    System.out.println(directory.add(number, parts[3], height)
                        ? "Added." : "Cannot add.");
                } else {
                    System.out.println("Unknown command.");
                }
            } catch (NumberFormatException exception) {
                System.out.println("Use integer numbers and heights.");
            }
        }
    }
}
