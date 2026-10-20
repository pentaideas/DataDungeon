public class EncounterQueue {
    private String[] events = new String[4];
    private int frontIndex;
    private int size;

    public int size() { return size; }
    public int capacity() { return events.length; }
    public int frontIndex() { return frontIndex; }

    public boolean enqueue(String event) {
        if (event == null) { return false; }
        if (size == events.length) { resize(events.length * 2); }
        int nextIndex = (frontIndex + size) % events.length;
        events[nextIndex] = event;
        size++;
        return true;
    }

    public String dequeue() {
        if (size == 0) { return null; }
        String removed = events[frontIndex];
        events[frontIndex] = null;
        frontIndex = (frontIndex + 1) % events.length;
        size--;
        return removed;
    }

    private void resize(int newCapacity) {
        String[] larger = new String[newCapacity];
        for (int offset = 0; offset < size; offset++) {
            larger[offset] = events[(frontIndex + offset) % events.length];
        }
        events = larger;
        frontIndex = 0;
    }

    public String peek() {
        return size == 0 ? null : events[frontIndex];
    }

    public String get(int offset) {
        if (offset < 0 || offset >= size) {
            throw new IndexOutOfBoundsException(offset);
        }
        return events[(frontIndex + offset) % events.length];
    }

    public String debugState() {
        return java.util.Arrays.toString(events)
            + " front=" + frontIndex + " size=" + size;
    }
}
