package models.exceptions;

public class fileParserException extends Exception {
    public fileParserException() {
        super("Failed to parse file!");
    }
    public fileParserException(String message) {
        super(message);
    }
    @Override
    public final void printStackTrace() {
        System.out.println(this.getMessage());
    }
    @Override
    public final StackTraceElement[] getStackTrace() {
        StackTraceElement[] fullTrace = super.getStackTrace();
        return fullTrace.length > 0 ? new StackTraceElement[]{fullTrace[0]} : new StackTraceElement[0];
    }
    @Override
    public final synchronized Throwable fillInStackTrace() {
        return this;
    }
    @Override
    protected final Object clone() {
        return null;
    }
}
