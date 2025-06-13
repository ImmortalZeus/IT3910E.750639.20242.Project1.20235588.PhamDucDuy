package models.exceptions;

import models.logger.secureLogger;

public class userAgentParserException extends Exception {
    public userAgentParserException() {
        super("Failed to parse user agent!");
    }
    public userAgentParserException(String message) {
        super(message);
    }
    @Override
    public final void printStackTrace() {
        secureLogger.info(this.getMessage());
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
        return new userAgentParserException(this.getMessage());
    }
}
