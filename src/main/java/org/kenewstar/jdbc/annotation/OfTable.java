package org.kenewstar.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 所属表
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OfTable {
    /**
     * 表对应的实体类
     * @return 类
     */
    Class<?> entityClass();

    /**
     * 属性名
     * @return 属性名
     */
    String fieldName() default "";
}
