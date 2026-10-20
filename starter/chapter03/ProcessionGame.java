import java.util.Scanner;

public class ProcessionGame {
    public static void main(String[] args) {
        EncounterList list = EncounterList.sample();
        Scanner input = new Scanner(System.in);
        System.out.println("The Shifting Procession");
        System.out.println("Commands: list, back, first, next, prev,");
        System.out.println("insert <name>, resolve, quit");
        show(list);
        while (input.hasNextLine()) {
            String command = input.nextLine().trim();
            if (command.equals("quit")) break;
            if (command.equals("list")) {
                System.out.println(list.forward());
            } else if (command.equals("back")) {
                System.out.println(list.backward());
            } else if (command.equals("first")) {
                System.out.println(list.first()
                    ? "Selected first." : "Empty.");
            } else if (command.equals("next")) {
                System.out.println(list.next()
                    ? "Moved." : "No next encounter.");
            } else if (command.equals("prev")) {
                System.out.println(list.previous()
                    ? "Moved." : "No previous encounter.");
            } else if (command.startsWith("insert ")) {
                String name = command.substring(7).trim();
                System.out.println(list.insertBefore(name)
                    ? "Inserted." : "Cannot insert.");
            } else if (command.equals("resolve")) {
                String name = list.removeSelected();
                System.out.println(name == null
                    ? "No encounter." : "Resolved: " + name);
            } else {
                System.out.println("Unknown command.");
            }
            show(list);
        }
    }

    private static void show(EncounterList list) {
        System.out.println("Selected: " + list.selected()
            + "; size=" + list.size());
    }
}
