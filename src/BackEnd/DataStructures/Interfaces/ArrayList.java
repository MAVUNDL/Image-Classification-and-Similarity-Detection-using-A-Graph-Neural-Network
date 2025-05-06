package BackEnd.DataStructures.Interfaces;

import BackEnd.DataStructures.Exceptions.ArrayListException;

import java.util.Comparator;
import java.util.Iterator;

public interface ArrayList <T>{
    /**
     * @return returns the number of elements in the arraylist
     */
    int size();

    /**
     * @return returns true if the arraylist is empty else false
     */
    boolean isEmpty();

    /**
     * This method retrieves the element stored at the given index in the arrayList
     * @param index the index associated with the element
     * @return returns the element at that index
     */
    T get(Integer index) throws ArrayListException;

    /**
     * This method replaces the element at the given index with a new element
     * @param index the index associated with the element
     * @param element the new element to replace the old
     */
    T set(Integer index, T element) throws ArrayListException;

    /**
     * This method adds the given element on the given index on the arraylist
     * @param index the index associated with the element
     * @param element the element to be stored at the given index
     */
    void add(Integer index, T element) throws ArrayListException;

    /**
     * This method adds the given element to the list
     * @param element given element
     */
    void add(T element);

    /**
     * This method removes the element at the given index
     *
     * @param index desired index
     */
    void remove(Integer index) throws ArrayListException;

    /**
     * This method remove the given element from the list
     * @param element the element to be removed
     */
    void remove(T element) throws ArrayListException;

    /**
     * @return returns an iterator for the list
     */
    Iterator<T> iterator();

    /**
     * This method sorts the list based on the comparator defined
     * @param comparator type of comparator
     */
    void sort(Comparator<T> comparator);
}
