/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class Inventory {
    private Item[] items = new Item[2];
    private int size;

    public int size() { return size; }
    public int capacity() { return items.length; }
    public Item get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index);
        }
        return items[index];
    }
    public boolean add(Item item) {
        if (item == null) { return false; }
        // Replace this capacity limit with growth.
        if (size == items.length) { return false; }
        items[size] = item;
        size++;
        return true;
    }
    public Item remove(String id) {
        // Implement search and ordered removal.
        return null;
    }
    public boolean contains(String id) {
        for (int i = 0; i < size; i++) {
            if (items[i].getId().equals(id)) { return true; }
        }
        return false;
    }
}
