public class Treasure {
    public final String id;
    public final int value;

    public Treasure(String id, int value) {
        this.id = id;
        this.value = value;
    }

    public String toString() { return id + ":" + value; }
}
