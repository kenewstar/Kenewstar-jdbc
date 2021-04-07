package org.kenewstar.jdbc.function;

import java.io.Serializable;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/6
 */
@FunctionalInterface
public interface FunctionColumn<T, V> extends Serializable {
    /**
     *
     * @param source t
     * @return v
     */
    V get(T source);
}
