import java.util.Scanner;

public class ArchiveGame {
    public static void main(String[] args) {
        DoorArchive archive = DoorArchive.sample();
        Scanner input = new Scanner(System.in);
        System.out.println("Archive of Doors");
        System.out.println("find <number>, add <number> <room>, list, "
            + "stats, clear, quit");
        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            String[] parts = line.split("\\s+", 3);
            if (line.equals("quit")) break;
            try {
                if (parts[0].equals("find") && parts.length == 2) {
                    String room = archive.get(Integer.parseInt(parts[1]));
                    System.out.println(room == null
                        ? "No door." : "Destination: " + room);
                } else if (parts[0].equals("add") && parts.length == 3) {
                    boolean added = archive.add(
                        Integer.parseInt(parts[1]), parts[2]);
                    System.out.println(added ? "Added." : "Cannot add.");
                } else if (line.equals("list")) {
                    System.out.println("Doors: " + archive.orderedDoors());
                } else if (line.equals("stats")) {
                    System.out.println("Size: " + archive.size()
                        + "; height: " + archive.height());
                } else if (line.equals("clear")) {
                    archive = new DoorArchive();
                    System.out.println("Archive cleared.");
                } else {
                    System.out.println("Use find, add, list, stats, "
                        + "clear, or quit.");
                }
            } catch (NumberFormatException exception) {
                System.out.println("Use an integer door number.");
            }
        }
    }
}
