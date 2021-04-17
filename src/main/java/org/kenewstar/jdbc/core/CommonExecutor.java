package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.annotation.OfTable;
import org.kenewstar.jdbc.core.factory.SqlFactory;
import org.kenewstar.jdbc.core.sql.Sql;
import org.kenewstar.jdbc.core.sql.SqlKeyWord;
import org.kenewstar.jdbc.function.MapTo;
import org.kenewstar.jdbc.util.Assert;
import org.kenewstar.jdbc.util.DataTableInfo;
import org.kenewstar.jdbc.util.KenewstarUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;

/**
 * common 执行器
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/1
 */
public abstract class CommonExecutor implements JdbcExecutor {

    private static final Logger logger = Logger.getLogger("common");

    protected KenewstarStatement statement;

    public CommonExecutor() {
        this.statement = new KenewstarStatement();
    }

    public CommonExecutor(String configPath) {
        this.statement = new  KenewstarStatement(configPath);
    }

    private StringBuilder resultType(Class<?> resultType) {
        StringBuilder sql = new StringBuilder(SqlKeyWord.SELECT);
        // 获取所有属性
        Field[] fields = resultType.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(OfTable.class)) {
                OfTable ofTable = field.getAnnotation(OfTable.class);
                Class<?> entityClass = ofTable.entityClass();
                // 获取表名
                String tableName = KenewstarUtil.getTableName(entityClass);
                // 获取列名
                String columnName = ofTable.fieldName();
                if (Objects.equals(columnName, "")) {
                    columnName = field.getName();
                }
                // 拼接sql
                sql.append(tableName).append(SqlKeyWord.SPOT)
                   .append(columnName)
                   .append(SqlKeyWord.AS).append(field.getName())
                   .append(SqlKeyWord.COMMA);
            } else {
                throw new RuntimeException("ofTable not found exception");
            }

        }
        sql.setCharAt(sql.length() - 1, SqlKeyWord.BLANK_CHAR);
        return sql;
    }

    @Override
    public <T> List<T> selectList(Class<?> fromClass , Class<T> resultType, MapTo mapTo) {
        List<T> result;
        Sql sql = SqlFactory.getSql();
        sql.getSql().append(resultType(resultType));
        // from user
        sql.getSql().append(SqlKeyWord.FROM)
               .append(KenewstarUtil.getTableName(fromClass))
               .append(SqlKeyWord.BLANK);
        mapTo.conditionSql(sql);
        // 执行sql
        List<Map<String, Object>> maps
                = statement.preparedSelectExecutor(sql.getSql().toString(), sql.getParams().toArray(new Object[]{}));
        result = new ArrayList<>(Objects.isNull(maps) ? 0 : maps.size());
        // 获取所有返回类型的属性
        Field[] fields = resultType.getDeclaredFields();
        maps.forEach(resultMap -> {
            try {
                T t = resultType.newInstance();
                // 数据组装
                for (Field field : fields) {
                    field.setAccessible(Boolean.TRUE);
                    field.set(t,resultMap.get(field.getName()));
                }
                result.add(t);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        return result;

    }


    @Override
    public <T> List<T> selectList(Class<T> entityClass, MapTo mapTo) {
        Sql sql = SqlFactory.getSql();
        // 获取表名
        String tableName = DataTableInfo.getTableName(entityClass);
        Map<String, String> columnNames = DataTableInfo.getColumnNames(entityClass);

        sql.getSql().append(SqlKeyWord.SELECT);
        for (String columnName : columnNames.keySet()) {
            sql.getSql().append(columnName)
                   .append(SqlKeyWord.COMMA);
        }
        sql.getSql().setCharAt(sql.getSql().length() - 1, SqlKeyWord.BLANK_CHAR);
        sql.getSql().append(SqlKeyWord.FROM)
               .append(tableName);
        // 组装条件
        mapTo.conditionSql(sql);
        // 执行查询
        List<Map<String, Object>> maps =
                statement.preparedSelectExecutor(sql.getSql().toString(), sql.getParams().toArray(new Object[]{}));
        // 打印SQL语句
        logger.info("Executed SQL ===> "+sql.getSql().toString());
        List<T> result = new ArrayList<>(maps.size());

        maps.forEach(map -> {
            try {
                T t = entityClass.newInstance();
                for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
                    String fieldName = columnNames.get(mapEntry.getKey());
                    Field field = entityClass.getDeclaredField(fieldName);
                    field.setAccessible(Boolean.TRUE);
                    field.set(t, mapEntry.getValue());
                }
                result.add(t);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        return result;
    }

    @Override
    public int batchDelete(Class<?> entityClass, List<?> ids) {
        Assert.notNull(ids);
        // 获取表名
        String tableName = DataTableInfo.getTableName(entityClass);
        // 获取id名
        String idName = DataTableInfo.getIdName(entityClass);
        int rows;
        StringBuilder sql = new StringBuilder(SqlKeyWord.DELETE);
        sql.append(SqlKeyWord.FROM)
           .append(tableName)
           .append(SqlKeyWord.WHERE)
           .append(idName)
           .append(SqlKeyWord.EQ)
           .append(SqlKeyWord.PLACEHOLDER);
        // 组装参数
        List<List<Object>> paramList = new ArrayList<>(ids.size());
        for (Object id : ids) {
            List<Object> params = new ArrayList<>(1);
            params.add(id);
            paramList.add(params);
        }
        // 执行结果
        rows = statement.preparedBatchExecutor(sql.toString(), paramList);
        // 打印SQL语句
        logger.info("Executed SQL ===> "+sql.toString());
        return rows;
    }

    @Override
    public int batchUpdate(List<?> paramList) {
        Assert.notNull(paramList);
        if (paramList.isEmpty()) {
            return 0;
        }
        Object obj = paramList.get(0);
        // 表名
        String tableName = DataTableInfo.getTableName(obj.getClass());
        //

        StringBuilder sql = new StringBuilder(SqlKeyWord.UPDATE);
        sql.append(SqlKeyWord.SET);

        return 0;
    }

    @Override
    public int batchInsert(List<?> paramList) {
        Assert.notNull(paramList);
        if (paramList.isEmpty()) {
            return 0;
        }
        Object obj = paramList.get(0);
        Class<?> entityClass = obj.getClass();

        // 参数容器
        List<List<Object>> params = new ArrayList<>(paramList.size());
        // 表名
        String tableName = DataTableInfo.getTableName(obj.getClass());
        Map<String, String> columnNames = DataTableInfo.getColumnNames(obj.getClass());

        // 组装sql
        StringBuilder sql = new StringBuilder(SqlKeyWord.INSERT);
        sql.append(SqlKeyWord.INTO)
           .append(tableName).append(SqlKeyWord.LEFT_BRACKETS);
        StringBuilder values = new StringBuilder(SqlKeyWord.VALUES);
        values.append(SqlKeyWord.LEFT_BRACKETS);

        // 参数名
        List<String> fieldNames = new ArrayList<>(columnNames.size());
        columnNames.forEach((k, v) -> {
            fieldNames.add(v);
            sql.append(k).append(SqlKeyWord.COMMA);
            values.append(SqlKeyWord.PLACEHOLDER).append(SqlKeyWord.COMMA);
        });
        sql.setCharAt(sql.length() - 1, SqlKeyWord.RIGHT_BRACKETS_CHAR);
        values.setCharAt(values.length() - 1, SqlKeyWord.RIGHT_BRACKETS_CHAR);
        sql.append(values);

        // 组装参数
        List<Object> param;
        for (Object object : paramList) {
            param = new ArrayList<>(fieldNames.size());
            try {
                for (String fieldName : fieldNames) {
                    Field field = entityClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    param.add(field.get(object));
                }
                params.add(param);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        int rows = statement.preparedBatchExecutor(sql.toString(), params);
        // 打印SQL语句
        logger.info("Executed SQL ===> "+sql.toString());
        return rows;
    }
}


