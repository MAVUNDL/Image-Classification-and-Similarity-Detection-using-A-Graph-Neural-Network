package app.BackEnd.DataStructures.Interfaces;

import app.BackEnd.DataStructures.Exceptions.MapException;

import java.util.Iterator;

/**
 * This interface defines a Map data structure
 * @param <K> type parameter for the Keys in the Map
 * @param <V> type parameter for the values in the Map
 */
public interface Map<K, V>{
    /**
     * @return returns true if the Map is empty else false
     */
    boolean isEmpty();

    /**
     * @return returns the number of elements (Pairs) in the Map
     */
    int size();

    /**
     * This method removes the value associated with the given key
     * @param key the key associated with the value
     * @return returns the removed value associated with the key
     */
    V remove(K key) throws MapException;

    /**
     * This method retrieves the value associated with the given key
     * @param key the key associated with the value
     * @return returns the value associated with key
     */
    V get(K key) throws MapException;

    /**
     * This method adds the pair to the Map
     * @param key the key associated with the value
     * @param value the value associated with the key
     */
    void put(K key, V value);

    /**
     * @return returns an iterator to iterate though the keys in the map
     */
    Iterator<K> keys() throws MapException;

    /**
     * @return returns an iterator to iterate though the values in the map
     */
    Iterator<V> values() throws MapException;

    /**
     * This method checks if the given key exists on the map
     * @param key the given key
     * @return returns true if it exists else false
     */
    boolean containsKey(K key);

    /**
     * This method checks if the given key exists on the map
     * @param value the given value
     * @return returns true if it exists else false
     */
    boolean containsValue(V value);
}
