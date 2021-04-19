package org.kenewstar.jdbc.exception;

/**
 * 通用异常类
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/19
 */
public class KenewstarException extends RuntimeException {

    public KenewstarException() {
        super();
    }

    public KenewstarException(String message) {
        super(message);
    }

    public KenewstarException(String message, Throwable cause) {
        super(message, cause);
    }

    public KenewstarException(Throwable cause) {
        super(cause);
    }
}
