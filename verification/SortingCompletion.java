import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class SortingCompletion {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void verify(int[] values) {
        Treasure[] original = new Treasure[values.length];
        for (int index = 0; index < values.length; index++) {
            original[index] = new Treasure("item-" + index, values[index]);
        }
        Treasure[] expected = original.clone();
        Arrays.sort(expected, Comparator.comparingInt(item -> item.value));
        CatalogueSort sorter = new CatalogueSort();
        Treasure[] insertion = original.clone();
        Treasure[] merged = original.clone();
        long first = sorter.insertionSort(insertion);
        long second = sorter.mergeSort(merged);
        for (int index = 0; index < values.length; index++) {
            check(insertion[index] == expected[index], "insertion stable identity");
            check(merged[index] == expected[index], "merge stable identity");
            check(original[index].value == values[index], "source mutated");
        }
        long n = values.length;
        check(first >= 0 && first <= n * (n - 1) / 2, "insertion count bound");
        int levels = n < 2 ? 0 : 64 - Long.numberOfLeadingZeros(n - 1);
        check(second >= 0 && second <= n * levels, "merge count bound");
        check(sorter.insertionSort(new Treasure[0]) == 0, "counter reset");
        check(sorter.mergeSort(new Treasure[0]) == 0, "empty merge");
    }

    public static void main(String[] args) {
        verify(new int[]{});
        verify(new int[]{1});
        verify(new int[]{2, 1});
        verify(new int[]{1, 1, 1, 1, 1});
        verify(new int[]{Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 0});
        Random random = new Random(1111);
        for (int trial = 0; trial < 300; trial++) {
            int[] values = new int[random.nextInt(65)];
            for (int index = 0; index < values.length; index++) {
                values[index] = random.nextInt(15) - 7;
            }
            verify(values);
        }
        for (int length : new int[]{8, 16, 32, 64}) {
            Treasure[] sorted = new Treasure[length];
            Treasure[] reverse = new Treasure[length];
            for (int index = 0; index < length; index++) {
                sorted[index] = new Treasure("s" + index, index);
                reverse[index] = new Treasure("r" + index, length - index);
            }
            CatalogueSort sorter = new CatalogueSort();
            check(sorter.insertionSort(sorted) == length - 1, "sorted count");
            check(sorter.insertionSort(reverse) == (long) length * (length - 1) / 2,
                "reverse count");
            long mergeCount = (long) length * Integer.numberOfTrailingZeros(length) / 2;
            check(sorter.mergeSort(sorted) == mergeCount, "merge exact count");
        }
        System.out.println("Sorting completion passed");
    }
}
