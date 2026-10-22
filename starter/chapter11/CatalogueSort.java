/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class CatalogueSort {
    private long comparisons;

    private int compare(Treasure first, Treasure second) {
        comparisons++;
        return Integer.compare(first.value, second.value);
    }

    public long insertionSort(Treasure[] items) {
        comparisons = 0;
        // TODO: stable insertion sort using compare for key comparisons.
        return comparisons;
    }

    public long mergeSort(Treasure[] items) {
        comparisons = 0;
        Treasure[] buffer = new Treasure[items.length];
        sortRange(items, buffer, 0, items.length);
        return comparisons;
    }

    private void sortRange(Treasure[] items, Treasure[] buffer,
            int low, int high) {
        if (high - low < 2) return;
        int middle = low + (high - low) / 2;
        sortRange(items, buffer, low, middle);
        sortRange(items, buffer, middle, high);
        merge(items, buffer, low, middle, high);
    }

    private void merge(Treasure[] items, Treasure[] buffer,
            int low, int middle, int high) {
        // TODO: merge the sorted half-open ranges and copy back.
    }
}
