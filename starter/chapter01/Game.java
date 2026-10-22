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

public class Game {
    public static void main(String[] args) {
        Player player = new Player(WorldBuilder.build());
        Scanner input = new Scanner(System.in);
        System.out.println("Use look, go <index>, or quit.");
        while (true) {
            System.out.print("> ");
            if (!input.hasNextLine()) { break; }
            String command = input.nextLine().trim();
            if (command.equals("quit")) { break; }
            if (command.equals("look")) {
                describe(player.getCurrentRoom());
            } else if (command.startsWith("go ")) {
                try {
                    int index = Integer.parseInt(
                        command.substring(3).trim());
                    if (player.move(index)) {
                        describe(player.getCurrentRoom());
                    } else {
                        System.out.println("No passage there.");
                    }
                } catch (NumberFormatException error) {
                    System.out.println("Use an integer index.");
                }
            } else {
                System.out.println("Use look, go <index>, or quit.");
            }
        }
    }

    private static void describe(Room room) {
        System.out.println(room.getName());
        for (int i = 0; i < room.getExitCount(); i++) {
            Room destination = room.getExit(i);
            if (destination != null) {
                System.out.println(i + ": " + destination.getName());
            }
        }
    }
}
