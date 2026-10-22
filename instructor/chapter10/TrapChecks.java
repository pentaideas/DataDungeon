/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class TrapChecks {
    public static void main(String[] args) {
        TrapQueue queue = new TrapQueue();
        queue.schedule(8, "late");
        queue.schedule(2, "early");
        check(queue.poll().name.equals("early"), "earliest time");
        queue.schedule(4, "first tie");
        queue.schedule(4, "second tie");
        check(queue.poll().name.equals("first tie"),
            "first equal-time event");
        check(queue.poll().name.equals("second tie"),
            "second equal-time event");
        check(queue.poll().name.equals("late"), "remaining event");
        check(queue.poll() == null, "empty queue");
        System.out.println("Trap checks passed");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
