import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

public class ArchiveCompletion {
    private static Object field(Object object, String name) throws Exception {
        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(object);
    }

    private static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }

    private static int inspect(Object node, long low, long high,
            TreeMap<Integer, String> found, Map<Integer, Object> identities,
            IdentityHashMap<Object, Boolean> seen) throws Exception {
        if (node == null) return -1;
        check(seen.put(node, true) == null, "cycle or shared child");
        int number = (Integer) field(node, "number");
        check(number > low && number < high, "whole-subtree ordering");
        found.put(number, (String) field(node, "room"));
        identities.put(number, node);
        int left = inspect(field(node, "left"), low, number,
            found, identities, seen);
        int right = inspect(field(node, "right"), number, high,
            found, identities, seen);
        return 1 + Math.max(left, right);
    }

    private static Map<Integer, Object> verify(DoorArchive archive,
            TreeMap<Integer, String> expected,
            Map<Integer, Object> previous) throws Exception {
        TreeMap<Integer, String> found = new TreeMap<>();
        Map<Integer, Object> identities = new TreeMap<>();
        int height = inspect(field(archive, "root"), Long.MIN_VALUE,
            Long.MAX_VALUE, found, identities, new IdentityHashMap<>());
        check(found.equals(expected), "stored entries");
        check(archive.size() == expected.size(), "size");
        check(archive.height() == height, "height");
        StringBuilder order = new StringBuilder();
        for (Map.Entry<Integer, String> entry : expected.entrySet()) {
            check(Objects.equals(archive.get(entry.getKey()), entry.getValue()),
                "lookup " + entry.getKey());
            order.append(entry.getKey()).append(' ');
        }
        check(order.toString().trim().equals(archive.orderedDoors()), "inorder");
        for (Integer key : previous.keySet()) {
            check(previous.get(key) == identities.get(key), "node replaced");
        }
        return identities;
    }

    public static void main(String[] args) throws Exception {
        DoorArchive archive = new DoorArchive();
        TreeMap<Integer, String> expected = new TreeMap<>();
        Map<Integer, Object> identities = verify(archive, expected,
            new TreeMap<>());
        check(archive.get(0) == null, "empty lookup");
        check(!archive.add(1, null), "null value");
        check(!archive.add(1, " \t "), "blank value");
        check(!archive.add(1, ""), "empty value");
        int[] boundary = {0, Integer.MIN_VALUE, Integer.MAX_VALUE, -1, 1};
        for (int key : boundary) {
            check(archive.add(key, " room " + key + " "), "boundary add");
            expected.put(key, " room " + key + " ");
            identities = verify(archive, expected, identities);
            check(!archive.add(key, "replacement"), "duplicate");
            identities = verify(archive, expected, identities);
        }
        Random random = new Random(606);
        for (int step = 0; step < 800; step++) {
            int key = random.nextInt(201) - 100;
            if (random.nextBoolean()) {
                String room = "room-" + step;
                boolean added = !expected.containsKey(key);
                check(archive.add(key, room) == added, "model add");
                if (added) expected.put(key, room);
            } else {
                check(Objects.equals(archive.get(key), expected.get(key)),
                    "model lookup");
            }
            identities = verify(archive, expected, identities);
        }
        Object root = field(archive, "root");
        check(!archive.add(999, null), "invalid on populated tree");
        check(!archive.add(999, "\t"), "blank on populated tree");
        check(archive.get(999) == null, "absent lookup");
        verify(archive, expected, identities);
        check(root == field(archive, "root"), "root preserved");

        DoorArchive balanced = new DoorArchive();
        DoorArchive ascending = new DoorArchive();
        for (int key : new int[]{40, 20, 60, 10, 30, 50, 70}) {
            check(balanced.add(key, "door"), "balanced insertion");
        }
        for (int key = 10; key <= 70; key += 10) {
            check(ascending.add(key, "door"), "ascending insertion");
        }
        check(balanced.height() == 2, "balanced height");
        check(ascending.height() == 6, "ascending height");
        check(balanced.orderedDoors().equals(ascending.orderedDoors()),
            "same keys, different shapes");
        check(balanced.add(25, "sanctuary"), "interior leaf insertion");
        check("sanctuary".equals(balanced.get(25)), "new leaf lookup");
        check(balanced.height() == 3, "new leaf height");
        System.out.println("Archive completion passed");
    }
}
