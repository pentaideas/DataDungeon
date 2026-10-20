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
        // Replace this capacity limit with growth.
        if (size == items.length) { return false; }
        items[size] = item;
        size++;
        return true;
    }
    public Item remove(String id) {
        // Implement search and ordered removal.
        return null;
    }
    public boolean contains(String id) {
        for (int i = 0; i < size; i++) {
            if (items[i].getId().equals(id)) { return true; }
        }
        return false;
    }
}
