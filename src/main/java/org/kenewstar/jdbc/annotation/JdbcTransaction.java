package org.kenewstar.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 事务管理注解
 * @author kenewstar
 * @version 1.0
 * @date 2020/9/28
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JdbcTransaction {

}
