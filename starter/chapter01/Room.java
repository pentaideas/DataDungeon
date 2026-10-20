public class Room {
    private final String name;
    private final Room[] exits;

    public Room(String name, int exitCount) {
        this.name = name;
        exits = new Room[exitCount];
    }

    public String getName() { return name; }
    public int getExitCount() { return exits.length; }

    public Room getExit(int index) {
        return exits[index];
    }

    public void setExit(int index, Room destination) {
        exits[index] = destination;
    }
}
