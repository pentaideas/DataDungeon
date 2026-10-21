import java.util.Scanner;

public class TrapGame {
    private static void followUp(TrapQueue queue, int now,
            int delay, String name) {
        long time = (long) now + delay;
        if (time > Integer.MAX_VALUE
                || !queue.schedule((int) time, name)) {
            System.out.println("Cannot schedule follow-up: " + name);
        } else {
            System.out.println("Scheduled: " + time + ":" + name);
        }
    }

    public static void main(String[] args) {
        TrapQueue queue = new TrapQueue();
        queue.schedule(8, "seal");
        queue.schedule(2, "pressure_plate");
        queue.schedule(6, "gate_opens");
        queue.schedule(4, "bell");
        int now = 0;
        Scanner input = new Scanner(System.in);
        System.out.println("The Awakening Traps");
        System.out.println("next, peek, state, schedule <time> <name>, quit");
        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            String[] parts = line.split("\\s+", 3);
            if (line.equals("quit")) break;
            try {
                if (line.equals("next")) {
                    TrapEvent event = queue.poll();
                    if (event == null) {
                        System.out.println("No events.");
                    } else {
                        now = event.time;
                        System.out.println("Time " + now + ": " + event.name);
                        if (event.name.equals("pressure_plate")) {
                            followUp(queue, now, 1, "darts");
                            followUp(queue, now, 4, "echo");
                        }
                    }
                } else if (line.equals("peek")) {
                    System.out.println("Next: " + queue.peek());
                } else if (line.equals("state")) {
                    System.out.println(queue.heapState());
                    System.out.println("Clock: " + now + "; size: "
                        + queue.size() + "; capacity: " + queue.capacity());
                } else if (parts[0].equals("schedule")
                        && parts.length == 3) {
                    int time = Integer.parseInt(parts[1]);
                    boolean added = time >= now
                        && queue.schedule(time, parts[2]);
                    System.out.println(added
                        ? "Scheduled." : "Cannot schedule.");
                } else {
                    System.out.println("Use next, peek, state, "
                        + "schedule, or quit.");
                }
            } catch (NumberFormatException exception) {
                System.out.println("Use an integer time.");
            }
        }
    }
}
