package org.kenewstar.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 数据库字段的id
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Id {
    /**
     * 数据表主键的名称
     * @return id名称
     */
    String value() default "";
}
