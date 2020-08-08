package org.kenewstar.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 绑定SQL参数
 * @author kenewstar
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    /**
     * value值即为传递参数的名称
     * @return
     */
    String name() ;
}
