package models.exceptions;

import models.logger.secureLogger;

public class ip2LocationException extends Exception {
    public ip2LocationException() {
        super("Failed to retrieve location info from IP!");
    }
    public ip2LocationException(String message) {
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
        return new ip2LocationException(this.getMessage());
    }
}
