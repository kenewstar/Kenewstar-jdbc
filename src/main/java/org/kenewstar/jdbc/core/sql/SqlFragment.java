package org.kenewstar.jdbc.core.sql;

import org.kenewstar.jdbc.core.factory.SqlFactory;
import org.kenewstar.jdbc.util.DataTableInfo;

import java.util.Map;

/**
 * Sql片段
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/18
 */
public interface SqlFragment {

    /**
     * 构建 select sql 片段
     * @param entityClass 实体类
     * @return Sql对象
     */
    default Sql buildSelectSqlFragment(Class<?> entityClass) {
        Sql sql = SqlFactory.getSql();
        // 获取表名
        String tableName = DataTableInfo.getTableName(entityClass);
        Map<String, String> columnNames = DataTableInfo.getColumnNames(entityClass);
        // 组装sql
        sql.getSql().append(SqlKeyWord.SELECT);
        for (String columnName : columnNames.keySet()) {
            sql.getSql().append(columnName)
                    .append(SqlKeyWord.COMMA);
        }
        sql.getSql().setCharAt(sql.getSql().length() - 1, SqlKeyWord.BLANK_CHAR);
        sql.getSql().append(SqlKeyWord.FROM)
                .append(tableName);
        // 返回sql对象
        return sql;
    }

    /**
     * 构建 count 查询sql片段
     * @param entityClass 实体类
     * @return Sql 对象
     */
    default Sql buildCountSqlFragment(Class<?> entityClass) {
        Sql sql = SqlFactory.getSql();
        // 获取表名
        String tableName = DataTableInfo.getTableName(entityClass);
        sql.getSql()
                .append(SqlKeyWord.SELECT)
                .append(SqlKeyWord.COUNT)
                .append(SqlKeyWord.FROM)
                .append(tableName);
        return sql;
    }

    /**
     * 构建分页Sql
     * @param sql sql对象
     * @param pageNum 页码
     * @param pageSize 每页记录数
     */
    default void buildPageSqlFragment(Sql sql, int pageNum, int pageSize) {
        StringBuilder pageSql = sql.getSql();
        pageSql.append(SqlKeyWord.LIMIT)
                .append(SqlKeyWord.PLACEHOLDER)
                .append(SqlKeyWord.COMMA)
                .append(SqlKeyWord.PLACEHOLDER);
        // 添加参数
        // limit a, b   a--> index , b--> size
        long index = (pageNum - 1) * pageSize;
        sql.getParams().add(index);
        sql.getParams().add(pageSize);
    }

}
