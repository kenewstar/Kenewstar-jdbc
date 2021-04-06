package org.kenewstar.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 对应实体类的类名
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * 对应数据表的名称
     * @return 表名
     */
    String tableName() default "";

}
