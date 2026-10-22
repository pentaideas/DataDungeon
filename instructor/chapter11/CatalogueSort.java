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
        for (int index = 1; index < items.length; index++) {
            Treasure saved = items[index];
            int position = index;
            while (position > 0
                    && compare(items[position - 1], saved) > 0) {
                items[position] = items[position - 1];
                position--;
            }
            items[position] = saved;
        }
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
        int left = low;
        int right = middle;
        int output = low;
        while (left < middle && right < high) {
            if (compare(items[left], items[right]) <= 0) {
                buffer[output++] = items[left++];
            } else {
                buffer[output++] = items[right++];
            }
        }
        while (left < middle) buffer[output++] = items[left++];
        while (right < high) buffer[output++] = items[right++];
        for (int index = low; index < high; index++) {
            items[index] = buffer[index];
        }
    }
}
