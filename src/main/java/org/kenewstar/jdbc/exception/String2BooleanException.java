package org.kenewstar.jdbc.exception;

/**
 * 字符串转换为boolean类型异常
 * @author kenewstar
 * @date 2020-08-12
 * @version 0.2
 */
public class String2BooleanException extends RuntimeException{
    public String2BooleanException() {
        super();
    }

    public String2BooleanException(String message) {
        super(message);
    }

    public String2BooleanException(String message, Throwable cause) {
        super(message, cause);
    }

    public String2BooleanException(Throwable cause) {
        super(cause);
    }

    protected String2BooleanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
