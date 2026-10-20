public class Inventory {
    private Item[] items = new Item[2];
    private int size;

    public int size() { return size; }
    public int capacity() { return items.length; }
    public Item get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index);
        }
        return items[index];
    }
    public boolean add(Item item) {
        if (item == null) { return false; }
        if (size == items.length) { resize(items.length * 2); }
        items[size] = item;
        size++;
        return true;
    }
    public Item remove(String id) {
        for (int index = 0; index < size; index++) {
            if (items[index].getId().equals(id)) {
                Item removed = items[index];
                for (int next = index + 1; next < size; next++) {
                    items[next - 1] = items[next];
                }
                size--;
                items[size] = null;
                return removed;
            }
        }
        return null;
    }
    private void resize(int newCapacity) {
        Item[] larger = new Item[newCapacity];
        for (int index = 0; index < size; index++) {
            larger[index] = items[index];
        }
        items = larger;
    }
    public boolean contains(String id) {
        for (int i = 0; i < size; i++) {
            if (items[i].getId().equals(id)) { return true; }
        }
        return false;
    }
}
