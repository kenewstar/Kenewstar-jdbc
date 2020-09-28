package org.kenewstar.jdbc.exception;

/**
 * properties属性文件未找到异常
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class PropertiesFileNotFoundException extends RuntimeException{

    public PropertiesFileNotFoundException() {
        super();
    }

    public PropertiesFileNotFoundException(String message) {
        super(message);
    }

    public PropertiesFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertiesFileNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PropertiesFileNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
