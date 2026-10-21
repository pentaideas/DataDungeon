import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Random;
import java.util.TreeMap;

public class DirectoryCompletion {
    static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }
    static Object field(Object object, String name) throws Exception {
        Field f = object.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(object);
    }
    static Object[] links(Object node) throws Exception {
        return (Object[]) field(node, "next");
    }
    static IdentityHashMap<Object, Boolean> structure(RoomDirectory directory,
            TreeMap<Integer, String> model, TreeMap<Integer, Integer> heights)
            throws Exception {
        Object sentinel = field(directory, "sentinel");
        IdentityHashMap<Object, Boolean> base = new IdentityHashMap<>();
        for (int level = 0; level <= RoomDirectory.MAX_HEIGHT; level++) {
            ArrayList<Integer> expected = new ArrayList<>();
            for (int key : model.keySet()) {
                if (heights.get(key) >= level) expected.add(key);
            }
            ArrayList<Integer> actual = new ArrayList<>();
            Object node = links(sentinel)[level];
            IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
            while (node != null) {
                check(seen.put(node, true) == null, "cycle");
                int key = (Integer) field(node, "number");
                check(model.containsKey(key), "unexpected key");
                check(links(node).length == heights.get(key) + 1, "height");
                check(field(node, "name").equals(model.get(key)), "name");
                if (level == 0) base.put(node, true);
                else check(base.containsKey(node), "same object at each level");
                actual.add(key);
                node = links(node)[level];
            }
            check(actual.equals(expected), "level membership " + level);
        }
        check(directory.size() == model.size(), "size");
        return base;
    }
    static void query(RoomDirectory directory, TreeMap<Integer, String> model,
            int target) {
        Integer key = model.ceilingKey(target);
        String expected = key == null ? null : key + ":" + model.get(key);
        check(java.util.Objects.equals(directory.ceiling(target), expected),
            "ceiling " + target);
        expected = model.containsKey(target) ? target + ":" + model.get(target) : null;
        check(java.util.Objects.equals(directory.exact(target), expected),
            "exact " + target);
    }
    public static void main(String[] args) throws Exception {
        RoomDirectory directory = RoomDirectory.sample();
        check(directory.ceiling(35).equals("40:archive"), "initial ceiling");
        check(directory.searchSteps() == 2, "top-down path for 35");
        check(directory.ceiling(65) == null, "above maximum");
        check(directory.searchSteps() == 2, "top-down path for 65");
        check(directory.exact(20).equals("20:well"), "equality");
        check(directory.add(35, "sanctuary", 2), "mission insert");
        check(directory.searchSteps() == 1, "insertion preserves latest query count");
        check(directory.ceiling(31).equals("35:sanctuary"), "mission query");
        check(directory.searchSteps() == 2, "mission path");
        check(!directory.add(35, "replacement", 3), "duplicate rejected");
        check(directory.exact(35).equals("35:sanctuary"), "not replaced");
        check(directory.levels().equals("L3: 40\nL2: 20 35 40\n"
            + "L1: 20 30 35 40 60\nL0: 10 20 30 35 40 50 60"), "mission levels");

        directory = new RoomDirectory();
        check(directory.add(7, "tower", 3), "tall insertion into empty");
        check(directory.levels().equals("L3: 7\nL2: 7\nL1: 7\nL0: 7"),
            "singleton on all levels");
        check(directory.ceiling(8) == null && directory.searchSteps() == 1,
            "singleton upper boundary");
        check(directory.exact(7).equals("7:tower"), "singleton exact");
        directory = new RoomDirectory();
        TreeMap<Integer, String> model = new TreeMap<>();
        TreeMap<Integer, Integer> heights = new TreeMap<>();
        query(directory, model, 0);
        check(!directory.add(1, null, 0), "null name");
        check(!directory.add(1, " ", 0), "blank name");
        check(!directory.add(1, "a", -1), "negative height");
        check(!directory.add(1, "a", 4), "height too large");
        structure(directory, model, heights);
        Random random = new Random(404);
        int[] extremes = {Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
        IdentityHashMap<Object, Boolean> previous = new IdentityHashMap<>();
        for (int i = 0; i < 1000; i++) {
            int key = i < 3 ? extremes[i] : random.nextInt(401) - 200;
            int height = i % 4;
            String name = " room" + i + " ";
            boolean expected = !model.containsKey(key);
            check(directory.add(key, name, height) == expected, "add return");
            if (expected) { model.put(key, name); heights.put(key, height); }
            IdentityHashMap<Object, Boolean> now = structure(directory, model, heights);
            for (Object node : previous.keySet()) {
                check(now.containsKey(node), "existing identity preserved");
            }
            previous = now;
            query(directory, model, key);
            query(directory, model, random.nextInt(501) - 250);
        }
        String before = directory.levels();
        check(!directory.add(999, "bad", 4), "invalid nonempty insertion");
        check(before.equals(directory.levels()), "invalid leaves links unchanged");
        structure(directory, model, heights);
        System.out.println("Directory completion checks passed");
    }
}
