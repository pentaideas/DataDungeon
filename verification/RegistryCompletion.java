import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Random;

public class RegistryCompletion {
    static void check(boolean ok, String label) {
        if (!ok) throw new AssertionError(label);
    }
    static Object field(Object object, String name) throws Exception {
        Field f = object.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(object);
    }
    static Map<String, Object> state(ArtifactRegistry registry,
            Map<String, String> expected) throws Exception {
        check(registry.size() == expected.size(), "size");
        check(registry.bucketCount() == 5, "bucket count");
        Object[] buckets = (Object[]) field(registry, "buckets");
        IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
        Map<String, Object> nodes = new HashMap<>();
        for (int i = 0; i < buckets.length; i++) {
            Object node = buckets[i];
            while (node != null) {
                check(seen.put(node, true) == null, "cycle or shared node");
                String id = (String) field(node, "id");
                check(nodes.put(id, node) == null, "duplicate id");
                check(Math.floorMod(id.hashCode(), 5) == i, "wrong bucket");
                check(expected.containsKey(id), "unexpected id");
                check(expected.get(id).equals(field(node, "room")), "stored room");
                check(expected.get(id).equals(registry.get(new String(id))),
                    "content equality lookup");
                node = field(node, "next");
            }
        }
        check(nodes.keySet().equals(expected.keySet()), "membership");
        return nodes;
    }
    public static void main(String[] args) throws Exception {
        check("Aa".hashCode() == "BB".hashCode(), "same hash fixture");
        check(Math.floorMod("C".hashCode(), 5) == 2, "same bucket fixture");
        ArtifactRegistry registry = ArtifactRegistry.sample();
        check(registry.bucketState().equals("0:\n1:\n2: C=archive BB=forge Aa=well\n3:\n4:"),
            "initial order");
        check("forge".equals(registry.get("BB")), "middle lookup");
        check("forge".equals(registry.remove(new String("BB"))), "middle remove");
        check(registry.bucketState().contains("2: C=archive Aa=well"), "bypass middle");
        check(!registry.register(new String("Aa"), "replacement"), "duplicate at tail");
        check("well".equals(registry.get("Aa")), "duplicate leaves value");
        check(registry.register("BB", "sanctuary"), "reuse id");
        check(registry.bucketState().contains("2: BB=sanctuary C=archive Aa=well"),
            "prepend registration");
        check("sanctuary".equals(registry.remove("BB")), "head remove");
        check("well".equals(registry.remove("Aa")), "tail remove");
        check("archive".equals(registry.remove("C")), "singleton remove");
        check(registry.remove("C") == null && registry.size() == 0, "missing removal");
        check(registry.register("C", "gate"), "reuse empty bucket");

        registry = new ArtifactRegistry();
        for (String bad : new String[] {null, "", "  "}) {
            check(registry.get(bad) == null && registry.remove(bad) == null, "invalid query");
            check(!registry.register(bad, "room"), "invalid id");
            check(!registry.register("id", bad), "invalid room");
        }
        Map<String, String> model = new HashMap<>();
        state(registry, model);
        String minHash = "polygenelubricants";
        check(minHash.hashCode() == Integer.MIN_VALUE, "minimum hash fixture");
        check(registry.register(minHash, "deep"), "negative hash insert");
        model.put(minHash, "deep");
        check(registry.register(" spaced ", " room "), "preserved spaces");
        model.put(" spaced ", " room ");
        check(registry.register("aa", "lower") && registry.register("Aa", "mixed"),
            "case-sensitive identifiers");
        model.put("aa", "lower"); model.put("Aa", "mixed");
        Map<String, Object> previous = state(registry, model);
        Random random = new Random(505);
        for (int i = 0; i < 1500; i++) {
            String id = i % 11 == 0 ? "Aa" : "tag" + random.nextInt(80);
            int operation = random.nextInt(3);
            Object removed = null;
            if (operation == 0) {
                String room = "room" + i;
                boolean expected = !model.containsKey(id);
                check(registry.register(new String(id), room) == expected, "register return");
                if (expected) model.put(id, room);
            } else if (operation == 1) {
                String expected = model.remove(id);
                if (expected != null) removed = previous.get(id);
                check(java.util.Objects.equals(registry.remove(new String(id)), expected),
                    "remove return");
            } else {
                check(java.util.Objects.equals(registry.get(new String(id)), model.get(id)),
                    "get return");
            }
            Map<String, Object> now = state(registry, model);
            for (String survivor : now.keySet()) {
                if (previous.containsKey(survivor)) {
                    check(now.get(survivor) == previous.get(survivor), "survivor identity");
                }
            }
            if (removed != null) check(field(removed, "next") == null, "detached removal");
            previous = now;
        }
        String before = registry.bucketState();
        check(!registry.register(null, "x") && !registry.register("x", " "), "invalid nonempty");
        check(before.equals(registry.bucketState()), "invalid preserves structure");
        for (String id : new HashSet<>(model.keySet())) {
            check(model.remove(id).equals(registry.remove(id)), "drain");
        }
        state(registry, model);
        System.out.println("Registry completion checks passed");
    }
}
