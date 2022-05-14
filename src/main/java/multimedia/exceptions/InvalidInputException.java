package multimedia.exceptions;

/**
 * Exception class that is thrown when the input scenarios contain invalid input
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String errorMessage) {
        super(errorMessage);
    }
}
