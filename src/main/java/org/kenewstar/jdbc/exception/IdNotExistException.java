package org.kenewstar.jdbc.exception;

/**
 * id注解不存在异常
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class IdNotExistException extends RuntimeException{

    public IdNotExistException() {
        super();
    }

    public IdNotExistException(String message) {
        super(message);
    }

    public IdNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdNotExistException(Throwable cause) {
        super(cause);
    }

    protected IdNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
