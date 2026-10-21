public class MazeGraph {
    private final String[] names;
    private final int[][] exits;

    // Inputs must have matching lengths and valid destination indices.
    public MazeGraph(String[] names, int[][] exits) {
        this.names = names.clone();
        this.exits = new int[exits.length][];
        for (int room = 0; room < exits.length; room++) {
            this.exits[room] = exits[room].clone();
        }
    }

    public int size() { return names.length; }
    public String name(int room) { return names[room]; }
    public int[] neighbors(int room) { return exits[room].clone(); }

    public static MazeGraph sample() {
        String[] names = {"Entrance", "Gallery", "Well", "Archive",
            "Forge", "Vault", "Exit", "Sealed chamber"};
        int[][] exits = {{1, 2}, {0, 3}, {0, 3, 4}, {1, 2, 5},
            {2, 5}, {3, 4, 6}, {5}, {}};
        return new MazeGraph(names, exits);
    }
}
