public class Chapter1Completion {
    private static void check(boolean condition, String message) {
        if (!condition) { throw new AssertionError(message); }
    }
    public static void main(String[] args) {
        Room entrance = WorldBuilder.build();
        Room gallery = entrance.getExit(0);
        Room well = entrance.getExit(1);
        Room archive = gallery.getExit(1);
        check(well.getExit(0) == archive, "Original shared archive");
        check(gallery.getExit(0) == entrance, "Original gallery return");
        check(archive.getExit(0) == entrance, "Original archive return");
        check(well.getExit(1) == null, "Original empty slot");
        Room vestibule = entrance.getExit(2);
        Room bridge = vestibule.getExit(0);
        Room vault = bridge.getExit(0);
        java.util.Set<Room> rooms = java.util.Collections.newSetFromMap(
            new java.util.IdentityHashMap<Room, Boolean>());
        rooms.add(entrance); rooms.add(gallery); rooms.add(well);
        rooms.add(archive); rooms.add(vestibule); rooms.add(bridge);
        rooms.add(vault);
        check(rooms.size() == 7, "Exactly three distinct added rooms");
        check(vestibule.getExit(1) == vault, "Shared new destination");
        check(bridge.getExit(1) == null, "Empty new slot");
        check(vault.getExit(0) == entrance, "Return route");
        check(vault.getExit(1) == null, "No direct reverse passage");
        Player player = new Player(entrance);
        for (int exit : new int[]{2, 0, 0, 0}) {
            check(player.move(exit), "New wing route");
        }
        check(player.getCurrentRoom() == entrance, "Cycle returns home");
        check(player.move(2) && player.move(1), "Alternate vault route");
        check(player.getCurrentRoom() == vault, "Same vault object");
        check(!player.move(1) && !player.move(-1) && !player.move(99),
              "Invalid moves fail");
        check(player.getCurrentRoom() == vault, "Failed move is stationary");
        check(entrance.getExit(2) == vestibule, "Movement preserves links");
        System.out.println("Chapter 1 completion requirements passed");
    }
}
