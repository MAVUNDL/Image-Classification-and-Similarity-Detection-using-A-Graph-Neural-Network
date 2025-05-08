package app.BackEnd.DataStructures.Exceptions;

/**
 * This just extends the Runtime exception class to define our custom run time exception on an empty Queue
 */
public class QueueException extends RuntimeException{

    public QueueException(){
        super();
    }

    public QueueException(String message){
        super(message);
    }

    public QueueException(String message, Throwable cause){
        super(message, cause);
    }

    public QueueException(Throwable cause){
        super(cause);
    }
}
