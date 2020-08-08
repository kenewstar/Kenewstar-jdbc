package org.kenewstar.jdbc.exception;

/**
 * 数据源为空异常
 * @author kenewstar
 */
public class KenewstarDataSourceException extends RuntimeException{

    public KenewstarDataSourceException() {
        super();
    }

    public KenewstarDataSourceException(String message) {
        super(message);
    }

    public KenewstarDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public KenewstarDataSourceException(Throwable cause) {
        super(cause);
    }

    protected KenewstarDataSourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
