package org.kenewstar.jdbc.exception;

/**
 * 实体类注解异常
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class EntityAnnoException extends RuntimeException{

    public EntityAnnoException() {
        super();
    }

    public EntityAnnoException(String message) {
        super(message);
    }

    public EntityAnnoException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityAnnoException(Throwable cause) {
        super(cause);
    }

    protected EntityAnnoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
