package lab04;

/**
 * Custom exception class for handling errors related to the Receipt Database operations.
 */
public class ReceiptDBException extends Exception {
    public ReceiptDBException(String message, Exception e) {
        super(message, e);
    }
}
