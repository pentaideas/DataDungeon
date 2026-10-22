/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class RoomDirectory {
    public static final int MAX_HEIGHT = 3;
    private static class Node {
        final int number;
        final String name;
        final Node[] next;
        Node(int number, String name, int height) {
            this.number = number;
            this.name = name;
            next = new Node[height + 1];
        }
    }
    private final Node sentinel = new Node(0, null, MAX_HEIGHT);
    private int size;
    private int searchSteps;

    public int size() { return size; }
    public int searchSteps() { return searchSteps; }

    private Node findBefore(int target) {
        searchSteps = 0;
        Node current = sentinel;
        for (int level = MAX_HEIGHT; level >= 0; level--) {
            while (current.next[level] != null
                    && current.next[level].number < target) {
                current = current.next[level];
                searchSteps++;
            }
        }
        return current;
    }

    public String ceiling(int target) {
        Node candidate = findBefore(target).next[0];
        return describe(candidate);
    }

    public String exact(int target) {
        Node candidate = findBefore(target).next[0];
        return candidate != null && candidate.number == target
            ? describe(candidate) : null;
    }

    public boolean add(int number, String name, int height) {
        if (name == null || name.trim().isEmpty()
                || height < 0 || height > MAX_HEIGHT) return false;
        Node[] predecessors = new Node[MAX_HEIGHT + 1];
        Node current = sentinel;
        for (int level = MAX_HEIGHT; level >= 0; level--) {
            while (current.next[level] != null
                    && current.next[level].number < number) {
                current = current.next[level];
            }
            predecessors[level] = current;
        }
        Node candidate = current.next[0];
        if (candidate != null && candidate.number == number) return false;
        Node added = new Node(number, name, height);
        for (int level = 0; level <= height; level++) {
            added.next[level] = predecessors[level].next[level];
            predecessors[level].next[level] = added;
        }
        size++;
        return true;
    }

    private String describe(Node node) {
        return node == null ? null : node.number + ":" + node.name;
    }

    public String levels() {
        StringBuilder text = new StringBuilder();
        for (int level = MAX_HEIGHT; level >= 0; level--) {
            if (text.length() > 0) text.append("\n");
            text.append("L").append(level).append(":");
            for (Node node = sentinel.next[level]; node != null;
                    node = node.next[level]) {
                text.append(" ").append(node.number);
            }
        }
        return text.toString();
    }

    public static RoomDirectory sample() {
        RoomDirectory directory = new RoomDirectory();
        int[] numbers = {10, 20, 30, 40, 50, 60};
        int[] heights = {0, 2, 1, 3, 0, 1};
        String[] names = {"entrance", "well", "forge",
            "archive", "bridge", "vault"};
        Node[] tails = new Node[MAX_HEIGHT + 1];
        java.util.Arrays.fill(tails, directory.sentinel);
        for (int i = 0; i < numbers.length; i++) {
            Node node = new Node(numbers[i], names[i], heights[i]);
            for (int level = 0; level < node.next.length; level++) {
                tails[level].next[level] = node;
                tails[level] = node;
            }
            directory.size++;
        }
        return directory;
    }
}
