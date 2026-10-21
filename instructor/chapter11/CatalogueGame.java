import java.util.Arrays;

public class CatalogueGame {
    private static Treasure[] treasures(int[] values) {
        Treasure[] result = new Treasure[values.length];
        for (int index = 0; index < values.length; index++) {
            result[index] = new Treasure("" + (char) ('A' + index),
                values[index]);
        }
        return result;
    }

    private static void experiment(String name, int[] values) {
        Treasure[] original = treasures(values);
        Treasure[] insertion = original.clone();
        Treasure[] merged = original.clone();
        CatalogueSort sorter = new CatalogueSort();
        long first = sorter.insertionSort(insertion);
        long second = sorter.mergeSort(merged);
        System.out.println(name + " input: " + Arrays.toString(original));
        System.out.println("Insertion: " + Arrays.toString(insertion));
        System.out.println("Merge: " + Arrays.toString(merged));
        System.out.println("Comparisons: insertion=" + first
            + ", merge=" + second);
    }

    public static void main(String[] args) {
        experiment("sample", new int[]{40, 10, 30, 10, 50, 20});
        experiment("sorted", new int[]{10, 20, 30, 40, 50, 60, 70, 80});
        experiment("reverse", new int[]{80, 70, 60, 50, 40, 30, 20, 10});
        experiment("equal", new int[]{10, 10, 10, 10, 10, 10, 10, 10});
        experiment("mixed", new int[]{40, 10, 70, 20, 80, 30, 60, 50});
    }
}
