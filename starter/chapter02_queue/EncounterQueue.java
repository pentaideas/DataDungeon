public class EncounterQueue {
    private String[] events = new String[4];
    private int frontIndex;
    private int size;

    public int size() { return size; }
    public int capacity() { return events.length; }
    public int frontIndex() { return frontIndex; }

    public boolean enqueue(String event) {
        if (event == null) { return false; }
        // Replace fixed capacity and linear placement.
        if (size == events.length) { return false; }
        events[size] = event;
        size++;
        return true;
    }

    public String dequeue() {
        if (size == 0) { return null; }
        String removed = events[0];
        // Replace shifting with a circular front index.
        for (int index = 1; index < size; index++) {
            events[index - 1] = events[index];
        }
        size--;
        events[size] = null;
        return removed;
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
