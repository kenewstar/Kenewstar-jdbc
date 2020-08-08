package org.kenewstar.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 数据库字段的id
 * @author kenewstar
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Id {
    /**
     * 数据表主键的名称
     * @return
     */
    String value() default "";
}
