package BackEnd.DataStructures.Classes;

import BackEnd.DataStructures.Exceptions.LinkedListException;
import BackEnd.DataStructures.Interfaces.LinkedList;

import java.util.Iterator;

/**
 * This class creates an arraylist of positions
 * @param <T> type parameter for each object stored in the list
 */
public class LinkedListDS<T> implements LinkedList<T> {
    /*
        class variables
     */
    Node<T> head;
    Node<T> tail;
    int size;

    public LinkedListDS(){
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        this.head.setNext(this.tail);
        this.size = 0;
    }

    /**
     * @return this method checks if the list is empty or not, returns true if empty else false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return returns the number of nodes on the list
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * This method returns the previous node before the given one
     *
     * @param node the current node
     * @return return the previous node before this one
     */
    @Override
    public Node<T> prev(Node<T> node) throws LinkedListException {
        if(head.getNext() == tail){
            throw new LinkedListException("This operation cannot be performed because the linkedList is empty");
        }

        if(node == head){
            throw new LinkedListException("There's no previous node from the head");
        }

        Node<T> iterator = this.head;
        while(iterator != null && iterator.getNext() != null){
            if(iterator.getNext() == node){
                return iterator;
            }
            iterator = iterator.getNext();
        }
        throw new LinkedListException("The node is not the list");
    }

    /**
     * This method returns the node after the given node
     *
     * @param node the current node
     * @return returns the next node after this one
     */
    @Override
    public Node<T> next(Node<T> node) throws LinkedListException {
        if(head.getNext() == tail){
            throw new LinkedListException("This operation cannot be performed because the linkedList is empty");
        }

        if(node == null){
            throw new LinkedListException("This element is null and cannot be used for this operation");
        }

        if(node == tail){
            return null;
        }
        return node.getNext();
    }

    /**
     * This method replaces the element stored by the given node with a new element
     *
     * @param node the current node
     * @param item the new element
     * @return returns the old element that was stored by the node
     */
    @Override
    public T replace(Node<T> node, T item) throws LinkedListException {
        if(head.getNext() == tail){
            throw new LinkedListException("This operation cannot be performed because the linkedList is empty");
        }

        if(node == null){
            throw new LinkedListException("This element is null and cannot be used for this operation");
        }

        Node<T> iterator = head;
        while (iterator != null && iterator.getNext() != null){
            if(iterator.equals(node)){
                T oldElement = iterator.element();
                node.setElement(item);
                return oldElement;
            }
            iterator = iterator.getNext();
        }
        throw new LinkedListException("This node does not exist on the linkedList");
    }

    /**
     * This method inserts a new node after the given one on the list
     *
     * @param node the current node
     * @param item the element to be stored by the new node added
     * @return returns the new node added to the list
     */
    @Override
    public Node<T> insertAfter(Node<T> node, T item) throws LinkedListException {
        if(head.getNext() == tail){
            throw new LinkedListException("This operation cannot be performed because the linkedList is empty");
        }

        if(node == null){
            throw new LinkedListException("This element is null and cannot be used for this operation");
        }

        if(node == tail){
            throw new LinkedListException("You cannot add a new node after the tail");
        }

        if(!existsOnList(node)){
            throw new LinkedListException("This node does not exist on the linkedList");
        }

        Node<T> newNode = new Node<>(node.getNext(), item);
        node.setNext(newNode);
        this.size++;
        return newNode;
    }

    /**
     * This method inserts a new node before the given one on the list
     *
     * @param node the current node
     * @param item the element to be stored by the new node
     * @return returns the new node added to the list
     */
    @Override
    public Node<T> insertBefore(Node<T> node, T item) throws LinkedListException {
        if(head.getNext() == tail){
            throw new LinkedListException("This operation cannot be performed because the linkedList is empty");
        }

        if(node == null){
            throw new LinkedListException("This element is null and cannot be used for this operation");
        }

        Node<T> prev = this.head;
        while (prev != null && prev.getNext() != null){
            if(prev.getNext() == node){
                Node<T> newNode = new Node<>(prev.getNext(), item);
                prev.setNext(newNode);
                this.size++;
                return newNode;
            }
            prev = prev.getNext();
        }
        throw new LinkedListException("This node does not exist on the linkedList");
    }

    /**
     * This method adds a new node at the beginning of the list
     *
     * @param item the element to be stored by the new node
     * @return returns the new node added to the list
     */
    @Override
    public Node<T> addFirst(T item) {
        Node<T> newNode = new Node<>(this.head.getNext(), item);
        this.head.setNext(newNode);
        if(this.size == 0){
            newNode.setNext(this.tail);
        }
        this.size++;
        return newNode;
    }

    /**
     * This method adds a new node at the end of the list
     *
     * @param item the new element to be stored by the new node
     * @return returns the new node added to the list
     */
    @Override
    public Node<T> addLast(T item) {
        Node<T> current = this.head;
        while (current.getNext() != this.tail){
            current = current.getNext();
        }
        Node<T> newNode = new Node<>(this.tail, item);
        current.setNext(newNode);
        this.size++;
        return newNode;
    }

    /**
     * This method removes the given node from the list
     *
     * @param elem the current node
     * @return return the removed element from the list
     */
    @Override
    public T remove(Node<T> elem) throws LinkedListException {
        if(elem == null){
            throw new LinkedListException("This element is null and cannot be used for this operation");
        }

        if(head.getNext() == tail){
            throw new LinkedListException("This operation cannot be performed because the linkedList is empty");
        }

        if(elem == head){
            throw new LinkedListException("Cannot remove the head of the linkedList");
        }

        if(elem == tail){
            throw new LinkedListException("Cannot remove the tail of the linkedList");
        }

        Node<T> current = (Node<T>) first();
        Node<T> prev = (Node<T>) prev( (Node<T>) first());
        while(current != null && current.getNext() != null){
            if(current.element().equals(elem.element())){
                T elementFromNode = current.element();
                prev.setNext(current.getNext());
                this.size--;
                return elementFromNode;
            }
            prev = current;
            current = current.getNext();
        }
        throw new LinkedListException("This node does not exist on the linkedList");
    }

    /**
     * This method iterates through the list and searches for the node with the given element
     *
     * @param elem the element to be searched
     * @return returns the node with the element
     */
    @Override
    public Node<T> search(T elem) throws LinkedListException {
        if(elem == null){
            throw new LinkedListException("This element is null, and cannot be used for this operation");
        }

        if(head.getNext() == tail){
            throw new LinkedListException("This operation cannot be performed because the linkedList is empty");
        }

        Node<T> current = (Node<T>) first();
        while(current != null && current.getNext() != null){
            if(current.element().equals(elem)){
                return current;
            }
            current = current.getNext();
        }
        throw new LinkedListException("A node containing this element does not exist on the linkedList");
    }

    /**
     * @return returns the first node on the list
     */
    @Override
    public Node<T> first() throws LinkedListException {
        return next(this.head); // return the node after the head which is the first node
    }

    /**
     * @return returns the last node on the list
     */
    @Override
    public Node<T> last() throws LinkedListException {
        return prev(this.tail); // return the node before the tail which is the last node
    }

    /**
     * @return returns an iterator for the list
     */
    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    /**
     * This method checks if the given node exists on the list or not
     * @param node the current node
     * @return returns true if it exits else false
     */
    private boolean existsOnList(Node<T> node){
        if(node == head || node == tail){
            return true;
        }

        Node<T> current = this.head;
        while (current != null){
            if(current == node){
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * This inner class creates an iterator for the outer class
     */
    private class LinkedListIterator implements Iterator<T>{
        private Node<T> cursor = first();

        @Override
        public boolean hasNext(){
            return cursor != null && cursor != tail;
        }

        @Override
        public T next(){
            T element = this.cursor.element();
            this.cursor = LinkedListDS.this.next(cursor);
            return element;
        }

        @Override
        public void remove(){
            LinkedListDS.this.remove(LinkedListDS.this.prev(cursor));
        }
    }
}
