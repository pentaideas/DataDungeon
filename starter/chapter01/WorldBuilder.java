/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class WorldBuilder {
    public static Room build() {
        Room entrance = new Room("Entrance", 3);
        Room gallery = new Room("Gallery", 2);
        Room well = new Room("Well", 2);
        Room archive = new Room("Archive", 1);

        entrance.setExit(0, gallery);
        entrance.setExit(1, well);
        gallery.setExit(0, entrance);
        gallery.setExit(1, archive);
        well.setExit(0, archive);
        archive.setExit(0, entrance);

        // Add your new rooms and connections here.
        return entrance;
    }
}
