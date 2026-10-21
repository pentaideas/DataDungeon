import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

public class TrapCompletion {
    private static final Comparator<TrapEvent> ORDER =
        Comparator.comparingInt((TrapEvent event) -> event.time)
            .thenComparingLong(event -> event.sequence);

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static Object field(Object object, String name) throws Exception {
        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(object);
    }

    private static void same(TrapEvent actual, TrapEvent expected) {
        if (expected == null) {
            check(actual == null, "empty result");
        } else {
            check(actual != null && actual.time == expected.time
                && actual.name.equals(expected.name)
                && actual.sequence == expected.sequence, "event order");
        }
    }

    private static void verify(TrapQueue queue,
            PriorityQueue<TrapEvent> model) throws Exception {
        check(queue.size() == model.size(), "size");
        same(queue.peek(), model.peek());
        TrapEvent[] events = (TrapEvent[]) field(queue, "events");
        check(queue.capacity() == events.length, "capacity");
        Set<Long> expected = new HashSet<>();
        for (TrapEvent event : model) expected.add(event.sequence);
        Set<Long> found = new HashSet<>();
        for (int index = 0; index < events.length; index++) {
            if (index >= queue.size()) {
                check(events[index] == null, "unused slot retains event");
                continue;
            }
            check(events[index] != null, "hole in active array");
            check(found.add(events[index].sequence), "duplicate event");
            if (index > 0) {
                check(ORDER.compare(events[(index - 1) / 2], events[index]) <= 0,
                    "heap invariant");
            }
        }
        check(found.equals(expected), "event membership");
    }

    public static void main(String[] args) throws Exception {
        TrapQueue queue = new TrapQueue();
        PriorityQueue<TrapEvent> model = new PriorityQueue<>(ORDER);
        verify(queue, model);
        check(queue.poll() == null, "empty poll");
        check(!queue.schedule(-1, "bad"), "negative time");
        check(!queue.schedule(0, null), "null name");
        check(!queue.schedule(0, " \t "), "blank name");
        check(!queue.schedule(0, ""), "empty name");
        check((Long) field(queue, "nextSequence") == 0, "invalid consumed sequence");
        verify(queue, model);
        long sequence = 0;
        for (int time : new int[]{1, 4, 2, 9, 6, 0, Integer.MAX_VALUE}) {
            String name = " event " + time + " ";
            check(queue.schedule(time, name), "schedule boundary");
            model.add(new TrapEvent(time, name, sequence++));
            verify(queue, model);
        }
        check(queue.capacity() == 8, "growth");
        while (!model.isEmpty()) {
            same(queue.poll(), model.poll());
            verify(queue, model);
        }
        check(queue.capacity() == 8, "no shrinking");
        Random random = new Random(1010);
        for (int step = 0; step < 1500; step++) {
            if (random.nextInt(3) != 0) {
                int time = random.nextInt(21);
                String name = "trap-" + step;
                check(queue.schedule(time, name), "mixed schedule");
                model.add(new TrapEvent(time, name, sequence++));
            } else {
                same(queue.poll(), model.poll());
            }
            verify(queue, model);
        }
        while (!model.isEmpty()) {
            TrapEvent peeked = queue.peek();
            TrapEvent removed = queue.poll();
            check(peeked == removed, "peek and poll identity");
            same(removed, model.poll());
            verify(queue, model);
        }
        check(queue.schedule(5, "reused"), "empty reuse");
        same(queue.poll(), new TrapEvent(5, "reused", sequence++));
        verify(queue, model);

        TrapQueue small = new TrapQueue();
        for (int time : new int[]{1, 4, 2, 9, 6}) small.schedule(time, "x");
        check(small.heapState().equals("[1:x, 4:x, 2:x, 9:x, 6:x]"), "setup");
        check(small.poll().time == 1, "root removal");
        check(small.heapState().equals("[2:x, 4:x, 6:x, 9:x]"), "right child");
        check(small.poll().time == 2, "second removal");
        check(small.heapState().equals("[4:x, 9:x, 6:x]"), "left child");
        check(small.poll().time == 4, "third removal");
        check(small.poll().time == 6, "one child");
        check(small.poll().time == 9, "singleton");
        check(small.peek() == null && small.poll() == null, "drained");
        System.out.println("Trap completion passed");
    }
}
