package app.BackEnd.DataStructures.Classes;

import app.BackEnd.DataStructures.Exceptions.ArrayListException;
import app.BackEnd.DataStructures.Interfaces.ArrayList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * This class creates an arraylist
 * @param <T> type parameter for the objects stored in the list
 */
public class ArrayListDS<T> implements ArrayList<T>, Iterable<T>{
    private T[] elements;
    private int size;
    private int length;

    /**
     * This constructor creates an arrayList
     */
    public ArrayListDS(){
        this.size = 0;
        this.length = 1;
        this.elements = allocateMemory(this.length);
    }

    private T[] allocateMemory(int length){
        return (T[]) new Object[length];
    }

    /**
     * @return returns the number of elements in the arraylist
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * @return returns true if the arraylist is empty else false
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * This method retrieves the element stored at the given index in the arrayList
     *
     * @param index the index associated with the element
     * @return returns the element at that index
     */
    @Override
    public T get(Integer index) throws ArrayListException {
        if(!isValidIndex(index)){
            throw new ArrayListException("Index out of bounds");
        }
        return this.elements[index];
    }

    /**
     * This method replaces the element at the given index with a new element
     *
     * @param index   the index associated with the element
     * @param element the new element to replace the old
     */
    @Override
    public T set(Integer index, T element) throws ArrayListException {
        if(!isValidIndex(index)){
            throw new ArrayListException("Index out of bounds");
        }
        T oldElement = this.elements[index];
        this.elements[index] = element;
        return oldElement;
    }

    /**
     * This method adds the given element on the given index on the arraylist
     *
     * @param index   the index associated with the element
     * @param element the element to be stored at the given index
     */
    @Override
    public void add(Integer index, T element) throws ArrayListException {
        if(!(index >= 0 && index <= this.size)){
            throw new ArrayListException("Index out of bounds");
        }

        if(this.size == this.length){
            expandArray();
        }

        if(this.size > 0){
            shiftRight(index);
        }
        this.elements[index] = element;
        this.size++;
    }

    /**
     * This method adds the given element to the list
     *
     * @param element given element
     */
    @Override
    public void add(T element) {
        add(this.size, element);
    }


    /**
     * This method removes the element at the given index
     *
     * @param index desired index
     */
    @Override
    public void remove(Integer index)  throws ArrayListException {
        if(!isValidIndex(index)){
            throw new ArrayListException("Index out of bounds");
        }

        if(this.size > 1){
            shiftLeft(index);
        }
        this.size--;
    }

    /**
     * This method remove the given element from the list
     *
     * @param element the element to be removed
     */
    @Override
    public void remove(T element)  throws  ArrayListException{
        remove(getIndex(element));
    }

    /**
     * This method validates the given index if its in bound or not
     * @param index current index
     * @return returns true if valid else false
     */
    private boolean isValidIndex(Integer index){
        return index < this.length && index >= 0;
    }

    /**
     * This method expands the array using the doubling strategy
     */
    private void expandArray(){
        this.length *= 2;
        T[] temp = allocateMemory(this.length);
        System.arraycopy(this.elements, 0, temp, 0,  this.size);
        this.elements = temp;
    }

    /**
     * This method shifts the elements to the right from the given index to make space
     * @param index desired index
     */
    private void shiftRight(Integer index){
        for(int i = this.size - 1; i >= index; i--){
            this.elements[i + 1] = this.elements[i];
        }
    }

    /**
     * This method shifts the elements to the left towards the given index to fill the space
     * @param index desired index
     */
    private void shiftLeft(Integer index){
        for(int i = index; i < this.size - 1; i++){
            this.elements[i] = this.elements[i +  1];
        }
    }

    private class ArrayListIterator implements Iterator<T>{
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            return (T) elements[index++];
        }
    }

    /**
     * @return returns an iterator for the list
     */
    @Override
    public Iterator<T> iterator(){
        return new ArrayListIterator();
    }

    /**
     * This method sorts the list based on the comparator defined
     *
     * @param comparator type of comparator
     */
    @Override
    public void sort(Comparator<T> comparator) {
        Arrays.sort(this.elements, 0, this.size, comparator);
    }

    private Integer getIndex(T element){
        if(element == null){
            throw new ArrayListException("This element is null, cannot be used for this operation");
        }

        for(int i = 0; i < this.size; i++){
            if(this.elements[i].equals(element)){
                return i;
            }
        }
        throw new ArrayListException("This element does not exist on the ArrayList");
    }

}
