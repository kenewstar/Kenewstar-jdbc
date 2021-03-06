package org.kenewstar.jdbc.exception;

/**
 * 非法页码异常
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class PageNumberIllegalException extends RuntimeException{
    public PageNumberIllegalException() {
        super();
    }

    public PageNumberIllegalException(String message) {
        super(message);
    }

    public PageNumberIllegalException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageNumberIllegalException(Throwable cause) {
        super(cause);
    }

    protected PageNumberIllegalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
