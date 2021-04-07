package org.kenewstar.jdbc.exception;

/**
 * 注解Id不唯一异常
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class IdNotUniqueException extends RuntimeException{

    public IdNotUniqueException() {
        super();
    }

    public IdNotUniqueException(String message) {
        super(message);
    }

    public IdNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdNotUniqueException(Throwable cause) {
        super(cause);
    }

    protected IdNotUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
