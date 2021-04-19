package org.kenewstar.jdbc.core;

import java.util.List;

/**
 * 批量执行器
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/17
 */
public interface BatchExecutor {

    /**
     * 批量删除
     * @param ids 主键集合
     * @param entityClass 实体类
     * @return rows
     */
    int batchDelete(Class<?> entityClass, List<?> ids);

    /**
     * 批量更新
     * @param paramList 参数集合
     * @return rows
     */
    int batchUpdate(List<?> paramList);

    /**
     * 批量插入
     * @param paramList 参数集合
     * @return rows
     */
    int batchInsert(List<?> paramList);

    /**
     * 批量插入
     * @param paramList 参数集合
     * @param count 每次执行多少条
     * @return rows
     */
    int batchInsert(List<?> paramList, int count);

}
