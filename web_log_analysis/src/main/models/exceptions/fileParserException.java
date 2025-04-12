package models.exceptions;

public class fileParserException extends Exception {
    public fileParserException() {
        super("Failed to parse file!");
    }
    public fileParserException(String message) {
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
