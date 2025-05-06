package BackEnd.DataStructures.Exceptions;

/**
 *  This just extends the Runtime exception class to define our custom run time exception on an empty LinkedList
 */
public class LinkedListException extends RuntimeException{
    public LinkedListException(){
        super();
    }

    public LinkedListException(String message){
        super(message);
    }

    public LinkedListException(String message, Throwable cause){
        super(message, cause);
    }

    public LinkedListException(Throwable cause){
        super(cause);
    }
}
