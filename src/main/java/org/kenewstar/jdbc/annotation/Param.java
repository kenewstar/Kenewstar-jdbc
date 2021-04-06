package org.kenewstar.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 绑定SQL参数
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    /**
     * value值即为传递参数的名称
     * @return 参数名称
     */
    String name() ;
}
