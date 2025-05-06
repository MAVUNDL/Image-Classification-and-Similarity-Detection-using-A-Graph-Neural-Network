package BackEnd.DataStructures.Classes;

/**
 * This class defines a node for a linkedList
 * @param <T> type parameter of the object's datatype
 */
public class Node <T> {
    /**
     * class variables
     */
    private T element;
    private Node<T> next;

    /**
     * This constructor creates a node for a linkedlist
     * @param next pointer to the next node in the list
     * @param element the element to be stored by the current node
     */
    public Node(Node<T> next, T element){
        this.next = next;
        this.element = element;
    }

    /**
     * @return return the element store by this node
     */
    public T element(){
        return this.element;
    }

    /**
     * @return returns the pointer to the next element in the list
     */
    public Node<T> getNext(){
        return this.next;
    }

    /**
     * This method set the element to stored by th node
     * @param element element to be stored
     */
    public void setElement(T element){
        this.element = element;
    }

    /**
     * This method sets the pointer to the next node after this one
     * @param next the next node in the list
     */
    public void setNext(Node<T> next){
        this.next = next;
    }

}
