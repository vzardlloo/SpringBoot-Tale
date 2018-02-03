package boot.tale.exception;


public class TaleException extends RuntimeException{
    public TaleException() {
    }

    public TaleException(String message) {
        super(message);
    }

    public TaleException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaleException(Throwable cause) {
        super(cause);
    }

}
