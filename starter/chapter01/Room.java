/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class Room {
    private final String name;
    private final Room[] exits;

    public Room(String name, int exitCount) {
        this.name = name;
        exits = new Room[exitCount];
    }

    public String getName() { return name; }
    public int getExitCount() { return exits.length; }

    public Room getExit(int index) {
        return exits[index];
    }

    public void setExit(int index, Room destination) {
        exits[index] = destination;
    }
}
