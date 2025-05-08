package app.BackEnd.DataStructures.Classes;

/**
 * This class stores a key-value pair
 * @param <K> type parameter for the key
 * @param <V> type parameter for the value
 */
public class Entry<K, V> {
    private K key;
    private V value;

    /**
     * This constructor creates a key value pair
     * @param key key associated with the value
     * @param value value associated with the key
     */
    public Entry(K key, V value){
        this.key = key;
        this.value = value;
    }

    /**
     * @return returns the key of the key-value pair
     */
    public K getKey() {
        return key;
    }

    /**
     * @return returns the value of the key-value pair
     */
    public V getValue() {
        return value;
    }

    /**
     * This method set the key for the key-value pair
     * @param key key associated with the pair
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * This method sets the value for the key-value pair
     * @param value value associated with the pair
     */
    public void setValue(V value) {
        this.value = value;
    }
}
