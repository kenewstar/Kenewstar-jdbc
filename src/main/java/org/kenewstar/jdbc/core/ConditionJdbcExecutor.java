package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.function.MapTo;

import java.util.List;

/**
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/7
 */
public interface ConditionJdbcExecutor {

    /**
     * 多表关联条件查询
     * @param fromClass 主表
     * @param resultType 返回类型
     * @param mapTo 条件SQL构造对象
     * @param <T> t
     * @return list
     */
    <T> List<T> selectList(Class<?> fromClass, Class<T> resultType, MapTo mapTo);

    /**
     * 单表条件查询
     * @param entityClass 实体类
     * @param mapTo 条件SQL构造对象
     * @param <T> t
     * @return list
     */
    <T> List<T> selectList(Class<T> entityClass, MapTo mapTo);

}
