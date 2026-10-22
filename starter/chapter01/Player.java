/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class Player {
    private Room currentRoom;

    public Player(Room startingRoom) {
        currentRoom = startingRoom;
    }

    public Room getCurrentRoom() { return currentRoom; }

    public boolean move(int exitIndex) {
        if (exitIndex < 0 ||
            exitIndex >= currentRoom.getExitCount()) {
            return false;
        }
        Room destination = currentRoom.getExit(exitIndex);
        if (destination == null) { return false; }
        currentRoom = destination;
        return true;
    }
}
