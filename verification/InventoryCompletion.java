import java.lang.reflect.Field;

public class InventoryCompletion {
    private static void check(boolean condition, String message) {
        if (!condition) { throw new AssertionError(message); }
    }
    private static void state(Inventory pack, String... ids) throws Exception {
        check(pack.size() == ids.length, "Size");
        for (int i = 0; i < ids.length; i++) {
            check(pack.get(i).getId().equals(ids[i]), "Order at " + i);
        }
        Field field = Inventory.class.getDeclaredField("items");
        field.setAccessible(true);
        Item[] slots = (Item[]) field.get(pack);
        for (int i = ids.length; i < slots.length; i++) {
            check(slots[i] == null, "Unused slot cleared at " + i);
        }
    }
    public static void main(String[] args) throws Exception {
        Inventory pack = new Inventory();
        state(pack);
        check(pack.capacity() == 2, "Initial capacity");
        check(!pack.add(null) && pack.remove("absent") == null, "Empty safety");
        Item lantern = new Item("lantern"), rope = new Item("rope");
        Item key = new Item("key"), map = new Item("map");
        Item relic = new Item("relic");
        check(pack.add(lantern) && pack.add(rope), "First items");
        check(pack.capacity() == 2, "No early resize");
        check(pack.add(key) && pack.capacity() == 4, "First growth");
        check(pack.remove(new String("rope")) == rope, "Equality and identity");
        state(pack, "lantern", "key");
        pack.add(map); pack.add(relic);
        state(pack, "lantern", "key", "map", "relic");
        check(pack.capacity() == 4, "Hand trace capacity");
        check(pack.remove("ghost") == null, "Missing removal");
        check(pack.remove("lantern") == lantern, "First removal identity");
        check(pack.remove("relic") == relic, "Last removal identity");
        state(pack, "key", "map");
        pack.remove("key"); pack.remove("map");
        state(pack);
        check(pack.capacity() == 4, "No shrinking");
        pack.add(rope); state(pack, "rope");
        for (int count : new int[]{17, 33}) {
            Inventory growing = new Inventory();
            int copies = 0;
            for (int i = 0; i < count; i++) {
                int oldSize = growing.size(), oldCapacity = growing.capacity();
                check(growing.add(new Item("item" + i)), "Growth accepts item");
                if (growing.capacity() != oldCapacity) { copies += oldSize; }
                check(growing.capacity() == oldCapacity
                    || growing.capacity() == oldCapacity * 2, "Doubling");
            }
            check(copies == (count == 17 ? 30 : 62), "Copy-count model");
            for (int i = 0; i < count; i++) {
                check(growing.get(i).getId().equals("item" + i), "Growth order");
            }
        }
        System.out.println("Inventory completion requirements passed");
    }
}
