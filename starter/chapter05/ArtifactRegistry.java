/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class ArtifactRegistry {
    private static class Node {
        final String id;
        final String room;
        Node next;
        Node(String id, String room, Node next) {
            this.id = id;
            this.room = room;
            this.next = next;
        }
    }

    private final Node[] buckets = new Node[5];
    private int size;

    public int size() { return size; }
    public int bucketCount() { return buckets.length; }

    private boolean valid(String text) {
        return text != null && !text.trim().isEmpty();
    }

    private int bucketIndex(String id) {
        return Math.floorMod(id.hashCode(), buckets.length);
    }

    public String get(String id) {
        if (!valid(id)) return null;
        Node node = buckets[bucketIndex(id)];
        // TODO: search the entire selected chain.
        return node != null && node.id.equals(id) ? node.room : null;
    }

    public boolean register(String id, String room) {
        // TODO: reject duplicates, then prepend one node.
        return false;
    }

    public String remove(String id) {
        // TODO: unlink only the matching node and return its room.
        return null;
    }

    public String bucketState() {
        StringBuilder text = new StringBuilder();
        for (int index = 0; index < buckets.length; index++) {
            if (index > 0) text.append("\n");
            text.append(index).append(":");
            for (Node node = buckets[index]; node != null;
                    node = node.next) {
                text.append(" ").append(node.id).append("=");
                text.append(node.room);
            }
        }
        return text.toString();
    }

    private void seed(String id, String room) {
        int index = bucketIndex(id);
        buckets[index] = new Node(id, room, buckets[index]);
        size++;
    }

    public static ArtifactRegistry sample() {
        ArtifactRegistry registry = new ArtifactRegistry();
        registry.seed("Aa", "well");
        registry.seed("BB", "forge");
        registry.seed("C", "archive");
        return registry;
    }
}
