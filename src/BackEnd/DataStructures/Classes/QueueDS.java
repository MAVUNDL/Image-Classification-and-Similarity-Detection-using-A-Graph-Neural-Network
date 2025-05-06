package BackEnd.DataStructures.Classes;

import BackEnd.DataStructures.Exceptions.QueueException;
import BackEnd.DataStructures.Interfaces.LinkedList;
import BackEnd.DataStructures.Interfaces.Queue;

public class QueueDS<T> implements Queue<T> {
    private final LinkedList<T> elements;

    /**
     * This constructor creates a que data structure with a linkedList as the underlying structure
     */
    public QueueDS(){
        this.elements = new LinkedListDS<>();
    }

    /**
     * @return returns the number of elements on the queue
     */
    @Override
    public int size() {
        return this.elements.size();
    }

    /**
     * @return returns true if the queue has size = 0 otherwise false
     */
    @Override
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    /**
     * This method locates the first element in the queue
     *
     * @return returns the first element in the queue
     * @throws QueueException a runtime exception if the queue is empty
     */
    @Override
    public T front() throws QueueException {
        if(isEmpty()){
            throw new QueueException("This operation cannot be performed because the Queue is empty");
        }
        return this.elements.first().element();
    }

    /**
     * This method adds an element to the queue
     *
     * @param object the element to be added to the queue
     */
    @Override
    public void enqueue(T object) throws QueueException {
        if(object == null){
            throw new QueueException("This element is null and cannot be used on the operation");
        }
        this.elements.addLast(object);
    }

    /**
     * This method removes the first element in the queue
     *
     * @return returns the removed element
     * @throws QueueException a runtime exception if the queue is empty
     */
    @Override
    public T dequeue() throws QueueException {
        if(isEmpty()){
            throw new QueueException("This operation cannot be performed because the Queue is empty");
        }
        Node<T> first = this.elements.first();
        return this.elements.remove(first);
    }
}
