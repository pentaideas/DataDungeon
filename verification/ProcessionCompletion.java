import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Random;

public class ProcessionCompletion {
    static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    static Object field(Object object, String name) throws Exception {
        Field f = object.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(object);
    }
    static void state(EncounterList list, ArrayList<String> model, int cursor)
            throws Exception {
        check(list.size() == model.size(), "size");
        check(list.forward().equals(model.toString()), "forward");
        ArrayList<String> reverse = new ArrayList<>(model);
        Collections.reverse(reverse);
        check(list.backward().equals(reverse.toString()), "backward");
        check(java.util.Objects.equals(list.selected(),
            cursor < 0 ? null : model.get(cursor)), "selection");
        Object dummy = field(list, "dummy");
        Object node = dummy;
        IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
        do {
            check(seen.put(node, true) == null, "cycle before dummy");
            Object next = field(node, "next"), prev = field(node, "prev");
            check(next != null && prev != null, "null link");
            check(field(next, "prev") == node, "next.prev reciprocity");
            check(field(prev, "next") == node, "prev.next reciprocity");
            node = next;
            check(seen.size() <= model.size() + 1, "too many nodes");
        } while (node != dummy);
        check(seen.size() == model.size() + 1, "node count");
        check(seen.containsKey(field(list, "current")), "cursor membership");
    }
    public static void main(String[] args) throws Exception {
        EncounterList list = new EncounterList();
        ArrayList<String> model = new ArrayList<>();
        int cursor = -1;
        state(list, model, cursor);
        check(!list.insertBefore(null) && !list.insertBefore("  "), "invalid name");
        check(list.removeSelected() == null, "empty removal");
        check(!list.first() && !list.next() && !list.previous(), "empty navigation");
        Random random = new Random(73);
        for (int i = 0; i < 2000; i++) {
            int operation = random.nextInt(5);
            if (operation == 0) {
                String name = "encounter" + (i % 7);
                Object old = field(list, "current");
                check(list.insertBefore(name), "insert return");
                if (cursor < 0) { model.add(name); cursor = 0; }
                else {
                    model.add(cursor, name); cursor++;
                    check(field(list, "current") == old, "preserve selected identity");
                }
            } else if (operation == 1) {
                Object old = field(list, "current");
                String expected = cursor < 0 ? null : model.remove(cursor);
                check(java.util.Objects.equals(list.removeSelected(), expected), "removed value");
                if (expected != null) {
                    check(field(old, "next") == null && field(old, "prev") == null,
                        "detach removed node");
                }
                if (model.isEmpty()) cursor = -1;
                else if (cursor == model.size()) cursor--;
            } else if (operation == 2) {
                boolean can = cursor >= 0 && cursor + 1 < model.size();
                check(list.next() == can, "next return");
                if (can) cursor++;
            } else if (operation == 3) {
                boolean can = cursor > 0;
                check(list.previous() == can, "previous return");
                if (can) cursor--;
            } else {
                check(list.first() == !model.isEmpty(), "first return");
                cursor = model.isEmpty() ? -1 : 0;
            }
            state(list, model, cursor);
        }
        list = EncounterList.sample();
        check(list.forward().equals("[scout, ogre, gate]"), "sample");
        list.next();
        Object ogre = field(list, "current");
        check(list.insertBefore("healer"), "insert healer");
        check(field(list, "current") == ogre, "same ogre node");
        check(list.removeSelected().equals("ogre"), "resolve ogre");
        check(list.selected().equals("gate"), "successor selected");
        check(list.forward().equals("[scout, healer, gate]"), "mission order");
        check(list.backward().equals("[gate, healer, scout]"), "mission backward");
        check(list.removeSelected().equals("gate"), "tail removal");
        check(list.selected().equals("healer"), "tail chooses predecessor");
        list.first(); list.removeSelected(); list.removeSelected();
        check(list.size() == 0 && list.selected() == null, "drained");
        check(list.insertBefore("torchbearer"), "reuse empty");
        check(list.selected().equals("torchbearer"), "singleton selected");
        System.out.println("Procession completion checks passed");
    }
}
