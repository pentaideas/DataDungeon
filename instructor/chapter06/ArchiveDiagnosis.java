/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class ArchiveDiagnosis {
    public static void main(String[] args) {
        DoorArchive archive = DoorArchive.sample();
        check("archive".equals(archive.get(40)), "root lookup");
        check("well".equals(archive.get(20)), "left child lookup");
        check("forge".equals(archive.get(60)), "right child lookup");
        check(archive.get(25) == null, "missing door");
        check(archive.size() == 7, "lookup preserves size");
        System.out.println("Archive diagnosis passed");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
