package BackEnd.DataStructures.Exceptions;

public class MapException extends RuntimeException{
    public MapException(){
        super();
    }

    public MapException(String message){
        super(message);
    }

    public MapException(String message, Throwable cause){
        super(message, cause);
    }

    public MapException(Throwable cause){
        super(cause);
    }
}
