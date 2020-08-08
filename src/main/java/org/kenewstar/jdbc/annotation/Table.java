package org.kenewstar.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 对应实体类的类名
 * @author kenewstar
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * 对应数据表的名称
     * @return
     */
    String tableName() default "";

}
