package models.exceptions;

public class ip2LocationException extends Exception {
    public ip2LocationException() {
        super("Failed to retrieve location info from IP!");
    }
    public ip2LocationException(String message) {
        super(message);
    }
    @Override
    public void printStackTrace() {
        System.out.println(getMessage());
    }
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
