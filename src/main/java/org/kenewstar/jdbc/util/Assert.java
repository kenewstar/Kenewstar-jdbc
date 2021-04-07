package org.kenewstar.jdbc.util;

import java.util.Objects;

/**
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/7
 */
public class Assert {

    public static void notNull(Object obj) {
        if (Objects.isNull(obj)) {
            throw new  NullPointerException("the obj is not exist");
        }
    }



}
