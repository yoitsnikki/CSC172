/*
 * Niharika Agrawal
 * CSC 172
 */

public class URMaps<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    private Entry<K, V>[] table; // Hash table array
    private int size;           // Number of key-value pairs in the map
    private int threshold;      // Resize threshold

    // Constructor initializes with default capacity
    public URMaps() {
        table = new Entry[INITIAL_CAPACITY];
        size = 0;
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR_THRESHOLD);
    }

    // Static inner class for the map entries
    private static class Entry<K, V> {
        final K key;
        V value;
        boolean isDeleted; // Marks an entry as deleted without nullifying it

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }
    }

    // Hash function to calculate the index
    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    // Ensures capacity by resizing when needed
    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[oldTable.length * 2];
        size = 0;
        threshold = (int) (table.length * LOAD_FACTOR_THRESHOLD);

        for (Entry<K, V> entry : oldTable) {
            if (entry != null && !entry.isDeleted) {
                put(entry.key, entry.value);
            }
        }
    }

    // Adds or updates a key-value pair in the map
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int index = hash(key);

        while (table[index] != null && !table[index].isDeleted) {
            if (table[index].key.equals(key)) {
                table[index].value = value; // Update value for existing key
                return;
            }
            index = (index + 1) % table.length; // Linear probing
        }

        table[index] = new Entry<>(key, value);
        size++;
    }

    // Retrieves a value associated with a given key
    public V get(K key) {
        int index = hash(key);

        while (table[index] != null) {
            if (!table[index].isDeleted && table[index].key.equals(key)) {
                return table[index].value;
            }
            index = (index + 1) % table.length;
        }

        return null; // Key not found
    }

    // Removes a key-value pair from the map
    public V remove(K key) {
        int index = hash(key);

        while (table[index] != null) {
            if (!table[index].isDeleted && table[index].key.equals(key)) {
                table[index].isDeleted = true;
                size--;
                return table[index].value;
            }
            index = (index + 1) % table.length;
        }

        return null; // Key not found
    }

    // Checks if the map contains a given key
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // Returns the number of key-value pairs in the map
    public int size() {
        return size;
    }

    // Returns true if the map is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Clears all key-value pairs from the map
    public void clear() {
        table = new Entry[INITIAL_CAPACITY];
        size = 0;
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR_THRESHOLD);
    }

    // Retrieves all keys as a collection
    public URArrayList<K> keySet() {
        URArrayList<K> keys = new URArrayList<>();
        for (Entry<K, V> entry : table) {
            if (entry != null && !entry.isDeleted) {
                keys.add(entry.key);
            }
        }
        return keys;
    }

    // Retrieves all values as a collection
    public URArrayList<V> values() {
        URArrayList<V> values = new URArrayList<>();
        for (Entry<K, V> entry : table) {
            if (entry != null && !entry.isDeleted) {
                values.add(entry.value);
            }
        }
        return values;
    }
}
