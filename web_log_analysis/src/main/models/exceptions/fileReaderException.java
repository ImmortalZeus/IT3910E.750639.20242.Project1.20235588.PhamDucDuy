package models.exceptions;

public class fileReaderException extends Exception {
    public fileReaderException() {
        super("Failed to read file!");
    }
    public fileReaderException(String message) {
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
