package BackEnd.DataStructures.Classes;

import BackEnd.DataStructures.Exceptions.MapException;
import BackEnd.DataStructures.Interfaces.ArrayList;
import BackEnd.DataStructures.Interfaces.LinkedList;
import BackEnd.DataStructures.Interfaces.Map;

import java.nio.ByteBuffer;
import java.util.Iterator;

public class MapDS<K, V> implements Map<K, V> {
    private Object[] elements;
    private int size;
    private int capacity;

    public MapDS(){
        this.size = 0;
        this.capacity = 100;
        this.elements = allocateMemory(capacity);
    }

    /**
     * @return returns true if the Map is empty else false
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * @return returns the number of elements (Pairs) in the Map
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * This method removes the value associated with the given key
     *
     * @param key the key associated with the value
     * @return returns the removed value associated with the key
     */
    @Override
    public V remove(K key) throws MapException {
        if(size == 0){
            throw new MapException("The map is empty");
        }

        int index = (int) hash(key);
        LinkedList<Entry<K,V>> listAtIndex = (LinkedList<Entry<K,V>>) this.elements[index];
        Iterator<Entry<K,V>> iterator = listAtIndex.iterator();
        while (iterator.hasNext()){
            Entry<K,V> entry = iterator.next();
            if(entry.getKey().equals(key)){
                V value = entry.getValue();
                iterator.remove();
                this.size--;
                return value;
            }
        }
        return null;
    }

    /**
     * This method retrieves the value associated with the given key
     *
     * @param key the key associated with the value
     * @return returns the value associated with key
     */
    @Override
    public V get(K key) throws MapException{
        if(size == 0){
            throw new MapException("The map is empty");
        }

        int index = (int) hash(key);
        LinkedList<Entry<K,V>> listAtIndex = (LinkedList<Entry<K,V>>) this.elements[index];
        Iterator<Entry<K,V>> iterator = listAtIndex.iterator();
        while (iterator.hasNext()){
            Entry<K,V> entry = iterator.next();
            if(entry.getKey().equals(key)){
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * This method adds the pair to the Map
     *
     * @param key   the key associated with the value
     * @param value the value associated with the key
     */
    @Override
    public void put(K key, V value) {
        int index = (int) hash(key);
        LinkedList<Entry<K,V>> listAtIndex = (LinkedList<Entry<K,V>>) this.elements[index];

        boolean foundFlag = false;

        if (listAtIndex != null && !listAtIndex.isEmpty()) {
            Iterator<Entry<K,V>> iterator = listAtIndex.iterator();
            while (iterator.hasNext()){
                Entry<K,V> entry = iterator.next();
                if(entry.getKey().equals(key)){
                    entry.setValue(value);
                    foundFlag = true;
                    break;
                }
            }
        }

        if(!foundFlag){
            assert listAtIndex != null;
            listAtIndex.addLast(new Entry<>(key, value));
            this.size++;
        }
    }

    /**
     * @return returns an iterator to iterate though the keys in the map
     */
    @Override
    public Iterator<K> keys()  throws MapException{
        if(size == 0){
            throw new MapException("The map is empty");
        }

        ArrayList<K> keys = new ArrayListDS<>();
        for(int i = 0; i < this.elements.length; i++){
            LinkedList<Entry<K,V>> listPerIndex = (LinkedList<Entry<K,V>>) this.elements[i];
            if(!listPerIndex.isEmpty()){
                Iterator<Entry<K,V>> iterator = listPerIndex.iterator();
                while(iterator.hasNext()){
                    Entry<K, V> entry = iterator.next();
                    keys.add(entry.getKey());
                }
            }
        }
        return keys.iterator();
    }

    /**
     * @return returns an iterator to iterate though the values in the map
     */
    @Override
    public Iterator<V> values() throws MapException {
        if(size == 0){
            throw new MapException("The map is empty");
        }

        ArrayList<V> values = new ArrayListDS<>();
        for(int i = 0; i < this.elements.length; i++){
            LinkedList<Entry<K,V>> listPerIndex = (LinkedList<Entry<K,V>>) this.elements[i];
            if(!listPerIndex.isEmpty()){
                Iterator<Entry<K,V>> iterator = listPerIndex.iterator();
                while(iterator.hasNext()){
                    Entry<K, V> entry = iterator.next();
                    values.add(entry.getValue());
                }
            }
        }
        return values.iterator();
    }

    /**
     * This method checks if the given key exists on the map
     *
     * @param key the given key
     * @return returns true if it exists else false
     */
    @Override
    public boolean containsKey(K key) {
        if(isEmpty()){
            return false;
        }

        int index = (int) hash(key);
        LinkedList<Entry<K,V>> listAtIndex = (LinkedList<Entry<K,V>>) this.elements[index];
        if(listAtIndex.isEmpty()){
            return false;
        }

        Iterator<Entry<K,V>> iterator = listAtIndex.iterator();
        while (iterator.hasNext()) {
            Entry<K,V> entry = iterator.next();
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if the given key exists on the map
     *
     * @param value the given value
     * @return returns true if it exists else false
     */
    @Override
    public boolean containsValue(V value) {
        for (int i = 0; i < this.elements.length; i++) {
            LinkedList<Entry<K, V>> listAtIndex = (LinkedList<Entry<K, V>>) this.elements[i];
            Iterator<Entry<K, V>> iterator = listAtIndex.iterator();
            while (iterator.hasNext()) {
                Entry<K, V> entry = iterator.next();
                if (entry.getValue().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method creates an array of given size, each index contains a linkedList of positions
     * @param size the desired size
     * @return returns the new array of linkedlist
     */
    private Object[] allocateMemory(int size){
        Object[] objects = new Object[size];
        for(int i = 0; i < objects.length; i++){
            objects[i] = new LinkedListDS<Entry<K, V>>();
        }
        return objects;
    }

    /**
     * This method hashes the given string
     * @param input the input string
     * @return returns the hash code for the string
     */
    private long hash(String input){
        return hash(input.getBytes());
    }

    /**
     * This method hashes the given integer
     * @param input the input integer
     * @return return the hash code for the integer
     */
    private long hash(int input){
        byte[] bytes = ByteBuffer.allocate(4).putInt(input).array();
        return hash(bytes);
    }

    /**
     * This method  calculates a hash code using the djb2 hash function for byte inputs
     * @param input sequence of bytes
     * @return returns a hash value
     */
    private long hash(byte[] input){
        long hash = 5381;
        for(int i = 0; i < input.length; i++){
            hash = ((hash << 5) + hash) + input[i];
        }
        return hash;
    }

    /**
     * This method hashes the given key using appropriate methods
     * @param key the key for the pair
     * @return returns the hash code for the key
     */
    private long hash(K key){
        if(key instanceof Integer){
            return Math.abs(hash((int) key)) % this.capacity;
        }

        if(key instanceof String){
            return Math.abs(hash((String) key)) % this.capacity;
        }

        return Math.abs((long) key.hashCode()) % capacity;
    }
}
