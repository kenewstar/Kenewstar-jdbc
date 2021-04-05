package org.kenewstar.jdbc.pool.exception;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/5
 */
public class TimoutException extends RuntimeException {

    public TimoutException() {
        super();
    }

    public TimoutException(String message) {
        super(message);
    }

    public TimoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimoutException(Throwable cause) {
        super(cause);
    }

    protected TimoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
