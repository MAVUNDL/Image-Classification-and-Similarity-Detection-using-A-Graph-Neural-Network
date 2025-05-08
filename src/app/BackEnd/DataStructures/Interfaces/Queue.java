package app.BackEnd.DataStructures.Interfaces;

import app.BackEnd.DataStructures.Exceptions.QueueException;

/**
 * This interface define a structure for Queue data structure
 * @param <T> type parameter of the objects to be stored by the queue
 */
public interface Queue <T>{
    /**
     * @return returns the number of elements on the queue
     */
    int size();

    /**
     * @return returns true if the queue has size = 0 otherwise false
     */
    boolean isEmpty();

    /**
     * This method locates the first element in the queue
     * @return returns the first element in the queue
     * @throws QueueException  a runtime exception if the queue is empty
     */
    T front() throws QueueException;

    /**
     * This method adds an element to the queue
     * @param object the element to be added to the queue
     */
    void enqueue(T object) throws QueueException;

    /**
     * This method removes the first element in the queue
     * @return returns the removed element
     * @throws QueueException a runtime exception if the queue is empty
     */
    T dequeue() throws QueueException;
}
