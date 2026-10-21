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
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (!earlier(events[index], events[parent])) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void trickleDown(int index) {
        while (index < size / 2) {
            int child = 2 * index + 1;
            int right = child + 1;
            if (right < size && earlier(events[right], events[child])) {
                child = right;
            }
            if (!earlier(events[child], events[index])) break;
            swap(index, child);
            index = child;
        }
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
