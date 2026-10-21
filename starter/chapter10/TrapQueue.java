import java.util.Arrays;

public class TrapQueue {
    private TrapEvent[] events = new TrapEvent[4];
    private int size;
    private long nextSequence;

    public int size() { return size; }
    public int capacity() { return events.length; }
    public TrapEvent peek() { return size == 0 ? null : events[0]; }

    public boolean schedule(int time, String name) {
        if (time < 0 || name == null || name.trim().isEmpty()) return false;
        if (nextSequence == Long.MAX_VALUE) return false;
        if (size == events.length) {
            events = Arrays.copyOf(events, events.length * 2);
        }
        events[size] = new TrapEvent(time, name, nextSequence++);
        size++;
        bubbleUp(size - 1);
        return true;
    }

    public TrapEvent poll() {
        if (size == 0) return null;
        TrapEvent first = events[0];
        size--;
        events[0] = events[size];
        events[size] = null;
        if (size > 0) trickleDown(0);
        return first;
    }

    private void bubbleUp(int index) {
        // TODO: restore heap order along the path to the root.
    }

    private void trickleDown(int index) {
        // TODO: restore heap order along one downward path.
    }

    private boolean earlier(TrapEvent first, TrapEvent second) {
        if (first.time != second.time) return first.time < second.time;
        return first.sequence < second.sequence;
    }

    private void swap(int first, int second) {
        TrapEvent saved = events[first];
        events[first] = events[second];
        events[second] = saved;
    }

    public String heapState() {
        return Arrays.toString(Arrays.copyOf(events, size));
    }
}
