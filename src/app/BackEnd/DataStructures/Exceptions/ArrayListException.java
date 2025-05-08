package app.BackEnd.DataStructures.Exceptions;

public class ArrayListException extends RuntimeException {
    public ArrayListException(){
        super();
    }

    public ArrayListException(String message){
        super(message);
    }

    public ArrayListException(String message, Throwable cause){
        super(message, cause);
    }

    public ArrayListException(Throwable cause){
        super(cause);
    }
}
