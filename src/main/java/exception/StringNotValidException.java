package exception;

public class StringNotValidException extends RuntimeException {
    public StringNotValidException(String message) {
        super(message);
    }
}
