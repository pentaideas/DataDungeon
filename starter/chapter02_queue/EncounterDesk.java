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

public class EncounterDesk {
    public static void main(String[] args) {
        EncounterQueue queue = new EncounterQueue();
        Scanner input = new Scanner(System.in);
        System.out.println("join <name>, next, peek, queue, state, quit");
        while (true) {
            System.out.print("> ");
            if (!input.hasNextLine()) { break; }
            String command = input.nextLine().trim();
            if (command.equals("quit")) { break; }
            if (command.startsWith("join ")) {
                String name = command.substring(5).trim();
                if (!name.isEmpty() && queue.enqueue(name)) {
                    System.out.println("Waiting: " + name);
                } else { System.out.println("Cannot join."); }
            } else if (command.equals("next")) {
                String event = queue.dequeue();
                System.out.println(event == null ? "No encounters."
                                                 : "Encounter: " + event);
            } else if (command.equals("peek")) {
                String event = queue.peek();
                System.out.println(event == null ? "No encounters."
                                                 : "Next: " + event);
            } else if (command.equals("queue")) {
                for (int offset = 0; offset < queue.size(); offset++) {
                    System.out.println(queue.get(offset));
                }
                System.out.println("Waiting count: " + queue.size());
            } else if (command.equals("state")) {
                System.out.println(queue.debugState());
            } else { System.out.println("Unknown command."); }
        }
    }
}
