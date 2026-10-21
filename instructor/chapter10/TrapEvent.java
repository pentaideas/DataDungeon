public class TrapEvent {
    public final int time;
    public final String name;
    public final long sequence;

    TrapEvent(int time, String name, long sequence) {
        this.time = time;
        this.name = name;
        this.sequence = sequence;
    }

    public String toString() { return time + ":" + name; }
}
