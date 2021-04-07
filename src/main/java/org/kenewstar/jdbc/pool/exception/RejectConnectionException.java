package org.kenewstar.jdbc.pool.exception;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/3
 */
public class RejectConnectionException extends RuntimeException {

    public RejectConnectionException() {
        super();
    }

    public RejectConnectionException(String message) {
        super(message);
    }

    public RejectConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RejectConnectionException(Throwable cause) {
        super(cause);
    }

    protected RejectConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
