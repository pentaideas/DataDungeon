/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class DoorArchive {
    private static class Node {
        final int number;
        final String room;
        Node left, right;
        Node(int number, String room) {
            this.number = number;
            this.room = room;
        }
    }

    private Node root;
    private int size;

    public int size() { return size; }

    public String get(int number) {
        Node current = root;
        while (current != null) {
            if (number == current.number) return current.room;
            current = number < current.number
                ? current.left : current.right;
        }
        return null;
    }

    public boolean add(int number, String room) {
        if (room == null || room.trim().isEmpty()) return false;
        Node parent = null;
        Node current = root;
        while (current != null) {
            if (number == current.number) return false;
            parent = current;
            current = number < current.number
                ? current.left : current.right;
        }
        Node added = new Node(number, room);
        if (parent == null) root = added;
        else if (number < parent.number) parent.left = added;
        else parent.right = added;
        size++;
        return true;
    }

    public int height() { return height(root); }

    private int height(Node node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public String orderedDoors() {
        StringBuilder result = new StringBuilder();
        appendInOrder(root, result);
        return result.toString().trim();
    }

    private void appendInOrder(Node node, StringBuilder result) {
        if (node == null) return;
        appendInOrder(node.left, result);
        result.append(node.number).append(' ');
        appendInOrder(node.right, result);
    }

    public static DoorArchive sample() {
        DoorArchive archive = new DoorArchive();
        archive.root = new Node(40, "archive");
        archive.root.left = new Node(20, "well");
        archive.root.right = new Node(60, "forge");
        archive.root.left.left = new Node(10, "gate");
        archive.root.left.right = new Node(30, "gallery");
        archive.root.right.left = new Node(50, "vault");
        archive.root.right.right = new Node(70, "observatory");
        archive.size = 7;
        return archive;
    }
}
