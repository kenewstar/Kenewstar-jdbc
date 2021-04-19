package org.kenewstar.jdbc.core.sql;

import org.kenewstar.jdbc.core.factory.SqlFactory;
import org.kenewstar.jdbc.util.DataTableInfo;
import org.kenewstar.jdbc.util.KenewstarUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Sql片段
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/18
 */
public interface SqlFragment {
    /**
     * jul日志对象
     */
    Logger LOGGER = Logger.getLogger("SqlFragment");
    /**
     * <p>构建更新Sql对象
     * 只构建对象属性值不为null的属性</p>
     * <p> update tableName set columnName=?, ... where idName=? </p>
     * @param entity 实体类对象
     * @return Sql对象
     */
    default Sql buildUpdateSqlFragment(Object entity) {
        // 获取Sql对象
        Sql sql = SqlFactory.getSql();
        StringBuilder updateSql = sql.getSql();
        List<Object> updateParams = sql.getParams();
        // 获取实体类
        Class<?> entityClass = entity.getClass();
        // 获取id
        String idName = DataTableInfo.getIdName(entityClass);
        Map<String, String> fieldAndColumn = DataTableInfo.getFieldNameAndColumnName(entityClass);
        // 获取所有属性
        Field[] fields = entityClass.getDeclaredFields();
        // 组装Sql与参数
        updateSql.append(SqlKeyWord.UPDATE)
                 .append(SqlKeyWord.SET)
                 .append(KenewstarUtil.getTableName(entityClass))
                 .append(SqlKeyWord.BLANK);
        try {
            Object idValue = null;
            for (Field field : fields) {
                field.setAccessible(true);
                // 获取属性值
                Object value = field.get(entity);
                if (Objects.equals(fieldAndColumn.get(field.getName()), idName)) {
                    idValue = field.get(entity);
                    continue;
                }
                if (Objects.nonNull(value) &&
                    !Objects.equals(fieldAndColumn.get(field.getName()), idName)) {
                    updateSql.append(fieldAndColumn.get(field.getName()))
                             .append(SqlKeyWord.EQ)
                             .append(SqlKeyWord.PLACEHOLDER)
                             .append(SqlKeyWord.COMMA);
                    updateParams.add(value);
                }
            }
            updateSql.setCharAt(updateSql.length() - 1, SqlKeyWord.BLANK_CHAR);
            updateSql.append(SqlKeyWord.WHERE)
                     .append(idName)
                     .append(SqlKeyWord.EQ)
                     .append(SqlKeyWord.PLACEHOLDER);
            updateParams.add(idValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("Execute Sql ===> " + sql.getSql().toString());
        return sql;
    }

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
