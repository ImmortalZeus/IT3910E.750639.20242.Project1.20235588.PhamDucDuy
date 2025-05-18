package models.exceptions;

public class userAgentParserException extends Exception {
    public userAgentParserException() {
        super("Failed to parse user agent!");
    }
    public userAgentParserException(String message) {
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
