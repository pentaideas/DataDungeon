import java.util.ArrayList;
import java.util.Scanner;

public class PackGame {
    public static void main(String[] args) {
        ArrayList<Item> entrance = new ArrayList<>();
        ArrayList<Item> supply = new ArrayList<>();
        for (String id : new String[]{"lantern", "rope",
                "key", "map", "relic"}) {
            supply.add(new Item(id));
        }
        Inventory pack = new Inventory();
        boolean atEntrance = true;
        Scanner input = new Scanner(System.in);
        System.out.println("look, go, take <id>, drop <id>,");
        System.out.println("inventory, quit");
        while (true) {
            ArrayList<Item> floor = atEntrance ? entrance : supply;
            System.out.print("> ");
            if (!input.hasNextLine()) { break; }
            String command = input.nextLine().trim();
            if (command.equals("quit")) { break; }
            if (command.equals("go")) {
                atEntrance = !atEntrance;
                System.out.println(atEntrance ? "Entrance"
                                               : "Supply chamber");
            } else if (command.equals("look")) {
                System.out.println(atEntrance ? "Entrance"
                                               : "Supply chamber");
                System.out.println("On the floor: " + floor);
            } else if (command.equals("inventory")) {
                for (int i = 0; i < pack.size(); i++) {
                    System.out.println(pack.get(i));
                }
                System.out.println("Size " + pack.size()
                        + ", capacity " + pack.capacity());
            } else if (command.startsWith("take ")) {
                String id = command.substring(5).trim();
                Item found = null;
                for (Item item : floor) {
                    if (item.getId().equals(id)) { found = item; }
                }
                if (found != null && pack.add(found)) {
                    floor.remove(found);
                    System.out.println("Collected " + found);
                } else { System.out.println("Cannot take that."); }
            } else if (command.startsWith("drop ")) {
                Item item = pack.remove(command.substring(5).trim());
                if (item != null) {
                    floor.add(item);
                    System.out.println("Dropped " + item);
                } else { System.out.println("Cannot drop that."); }
            } else { System.out.println("Unknown command."); }
            if (atEntrance && pack.contains("relic")) {
                System.out.println("Relic recovered! You may continue.");
            }
        }
    }
}
