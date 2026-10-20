import java.util.ArrayDeque;
import java.util.Random;

public class QueueCompletion {
    private static void check(boolean condition, String message) {
        if (!condition) { throw new AssertionError(message); }
    }
    public static void main(String[] args) {
        EncounterQueue queue = new EncounterQueue();
        check(queue.dequeue() == null && queue.peek() == null, "Empty safety");
        check(!queue.enqueue(null), "Null rejected");
        for (String name : new String[]{"A", "B", "C", "D"}) {
            check(queue.enqueue(name), "Initial fill");
        }
        check(queue.dequeue().equals("A"), "First encounter");
        check(queue.dequeue().equals("B"), "Second encounter");
        queue.enqueue("E"); queue.enqueue("F");
        check(queue.debugState().equals("[E, F, C, D] front=2 size=4"),
              "Wrapped full state");
        queue.enqueue("G");
        check(queue.debugState().equals(
            "[C, D, E, F, G, null, null, null] front=0 size=5"),
              "Wrapped resize preserves logical order");
        check(queue.peek().equals("C") && queue.size() == 5, "Peek preserves size");
        for (String name : new String[]{"C", "D", "E", "F", "G"}) {
            check(queue.dequeue().equals(name), "Drain order");
        }
        check(queue.debugState().equals(
            "[null, null, null, null, null, null, null, null] front=5 size=0"),
              "Cleared slots and retained front");
        check(queue.dequeue() == null && queue.frontIndex() == 5, "Empty unchanged");
        queue.enqueue("H"); check(queue.dequeue().equals("H"), "Reuse");
        int retainedCapacity = queue.capacity();
        for (int i = 0; i < 24; i++) {
            queue.enqueue("repeat" + i);
            check(queue.dequeue().equals("repeat" + i), "Repeated wraparound");
            check(queue.capacity() == retainedCapacity, "Wraparound needs no growth");
        }
        ArrayDeque<String> model = new ArrayDeque<>();
        queue = new EncounterQueue();
        Random random = new Random(42);
        for (int step = 0; step < 1000; step++) {
            if (random.nextInt(3) != 0) {
                String value = "event" + step;
                check(queue.enqueue(value), "Stress enqueue");
                model.addLast(value);
            } else {
                check(java.util.Objects.equals(queue.dequeue(), model.pollFirst()),
                      "Stress FIFO");
            }
            check(queue.size() == model.size(), "Stress size");
            check(java.util.Objects.equals(queue.peek(), model.peekFirst()), "Stress peek");
            int offset = 0;
            for (String value : model) {
                check(queue.get(offset++).equals(value), "Stress logical order");
            }
        }
        System.out.println("Queue completion requirements passed");
    }
}
