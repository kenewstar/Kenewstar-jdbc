package org.kenewstar.jdbc.exception;

/**
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/19
 */
public class ExceptionFactory {

    private static final StringBuilder ERROR_MESSAGE
            = new StringBuilder("\nError message: ");

    private ExceptionFactory () {}

    private static String wrapMessage(String e) {
        return ERROR_MESSAGE.append(e).toString();
    }

    public static RuntimeException wrapException(String message, Throwable e) {
        return new KenewstarException(wrapMessage(message), e);
    }

}
