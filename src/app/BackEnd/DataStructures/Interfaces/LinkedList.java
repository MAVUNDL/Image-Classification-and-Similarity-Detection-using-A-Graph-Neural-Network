package app.BackEnd.DataStructures.Interfaces;

import app.BackEnd.DataStructures.Classes.Node;
import app.BackEnd.DataStructures.Exceptions.LinkedListException;

import java.util.Iterator;

/**
 * This interface defines a Linked list
 * @param <T> type parameter for the objects stores in the list
 */
public interface LinkedList <T>{
    /**
     * @return this method checks if the list is empty or not, returns true if empty else false
     */
    boolean isEmpty();

    /**
     * @return returns the number of nodes on the list
     */
    int size();

    /**
     * This method returns the previous node before the given one
     * @param node the current node
     * @return return the previous node before this one
     */
    Node<T> prev(Node<T> node) throws LinkedListException;

    /**
     * This method returns the node after the given node
     * @param node the current node
     * @return returns the next node after this one
     */
    Node<T> next(Node<T> node) throws LinkedListException;

    /**
     * This method replaces the element stored by the given node with a new element
     * @param node the current node
     * @param item the new element
     * @return returns the old element that was stored by the node
     */
    T replace(Node<T> node, T item) throws LinkedListException;

    /**
     * This method inserts a new node after the given one on the list
     * @param node the current node
     * @param item the element to be stored by the new node added
     * @return returns the new node added to the list
     */
    Node<T> insertAfter(Node<T> node, T item) throws LinkedListException;

    /**
     * This method inserts a new node before the given one on the list
     * @param node the current node
     * @param item the element to be stored by the new node
     * @return returns the new node added to the list
     */
    Node<T> insertBefore(Node<T> node, T item) throws LinkedListException;

    /**
     * This method adds a new node at the beginning of the list
     * @param item the element to be stored by the new node
     * @return returns the new node added to the list
     */
    Node<T> addFirst(T item);

    /**
     * This method adds a new node at the end of the list
     * @param item the new element to be stored by the new node
     * @return returns the new node added to the list
     */
    Node<T> addLast(T item);

    /**
     * This method removes the given node from the list
     * @param elem the current node
     * @return return the removed element from the list
     */
    T remove(Node<T> elem) throws LinkedListException;

    /**
     * This method iterates through the list and searches for the node with the given element
     * @param elem the element to be searched
     * @return returns the node with the element
     */
    Node<T> search(T elem) throws LinkedListException;

    /**
     * @return returns the first node on the list
     */
    Node<T> first() throws LinkedListException;

    /**
     * @return returns the last node on the list
     */
    Node<T> last() throws LinkedListException;

    /**
     * @return returns an iterator for the list
     */
    Iterator<T> iterator();
}
