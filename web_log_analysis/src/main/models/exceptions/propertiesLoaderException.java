package models.exceptions;

public class propertiesLoaderException extends Exception {
    public propertiesLoaderException() {
        super("Failed to load properties!");
    }
    public propertiesLoaderException(String message) {
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
