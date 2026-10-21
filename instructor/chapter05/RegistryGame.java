import java.util.Scanner;

public class RegistryGame {
    public static void main(String[] args) {
        ArtifactRegistry registry = ArtifactRegistry.sample();
        Scanner input = new Scanner(System.in);
        System.out.println("The Keeper's Registry");
        System.out.println("Commands: find <id>, register <id> <room>,");
        System.out.println("claim <id>, buckets, quit");
        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            if (line.equals("quit")) break;
            String[] parts = line.split("\\s+", 3);
            if (line.equals("buckets")) {
                System.out.println(registry.bucketState());
                System.out.println("Size: " + registry.size());
            } else if (parts.length == 2 && parts[0].equals("find")) {
                String room = registry.get(parts[1]);
                System.out.println(room == null
                    ? "Not registered." : "Location: " + room);
            } else if (parts.length == 3
                    && parts[0].equals("register")) {
                System.out.println(registry.register(parts[1], parts[2])
                    ? "Registered." : "Cannot register.");
            } else if (parts.length == 2 && parts[0].equals("claim")) {
                String room = registry.remove(parts[1]);
                System.out.println(room == null
                    ? "Not registered." : "Claimed from: " + room);
            } else {
                System.out.println("Unknown command.");
            }
        }
    }
}
