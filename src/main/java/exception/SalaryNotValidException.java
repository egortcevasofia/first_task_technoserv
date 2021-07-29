package exception;

public class SalaryNotValidException extends RuntimeException{
    public SalaryNotValidException(String message) {
        super(message);
    }
}
