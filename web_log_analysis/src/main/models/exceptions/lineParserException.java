package models.exceptions;

public class lineParserException extends Exception {
    public lineParserException() {
        super("Failed to parse line!");
    }
    public lineParserException(String message) {
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
