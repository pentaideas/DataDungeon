public class Player {
    private Room currentRoom;

    public Player(Room startingRoom) {
        currentRoom = startingRoom;
    }

    public Room getCurrentRoom() { return currentRoom; }

    public boolean move(int exitIndex) {
        if (exitIndex < 0 ||
            exitIndex >= currentRoom.getExitCount()) {
            return false;
        }
        Room destination = currentRoom.getExit(exitIndex);
        if (destination == null) { return false; }
        currentRoom = destination;
        return true;
    }
}
