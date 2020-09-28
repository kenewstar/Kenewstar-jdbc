package org.kenewstar.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 映射列名
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * 对应数据表列的名称
     * @return
     */
    String columnName() default "";

}
