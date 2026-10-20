public class EncounterList {
    private static class Node {
        String name;
        Node prev, next;
        Node(String name) { this.name = name; }
    }

    private final Node dummy = new Node(null);
    private Node current = dummy;
    private int size;

    public EncounterList() {
        dummy.next = dummy;
        dummy.prev = dummy;
    }

    public int size() { return size; }
    public String selected() {
        return current == dummy ? null : current.name;
    }

    public boolean first() {
        current = dummy.next;
        return current != dummy;
    }

    public boolean next() {
        if (current == dummy || current.next == dummy) return false;
        current = current.next;
        return true;
    }

    public boolean previous() {
        if (current == dummy || current.prev == dummy) return false;
        current = current.prev;
        return true;
    }

    public boolean insertBefore(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        Node node = new Node(name);
        node.prev = current.prev;
        node.next = current;
        node.prev.next = node;
        current.prev = node;
        if (size == 0) current = node;
        size++;
        return true;
    }

    public String removeSelected() {
        if (current == dummy) return null;
        Node removed = current;
        removed.prev.next = removed.next;
        removed.next.prev = removed.prev;
        current = removed.next != dummy ? removed.next : removed.prev;
        size--;
        removed.next = null;
        removed.prev = null;
        return removed.name;
    }

    public String forward() {
        StringBuilder text = new StringBuilder("[");
        for (Node node = dummy.next; node != dummy; node = node.next) {
            if (text.length() > 1) text.append(", ");
            text.append(node.name);
        }
        return text.append("]").toString();
    }

    public String backward() {
        StringBuilder text = new StringBuilder("[");
        for (Node node = dummy.prev; node != dummy; node = node.prev) {
            if (text.length() > 1) text.append(", ");
            text.append(node.name);
        }
        return text.append("]").toString();
    }

    public static EncounterList sample() {
        EncounterList list = new EncounterList();
        for (String name : new String[] {"scout", "ogre", "gate"}) {
            Node node = new Node(name);
            node.prev = list.dummy.prev;
            node.next = list.dummy;
            node.prev.next = node;
            list.dummy.prev = node;
            list.size++;
        }
        list.first();
        return list;
    }
}
