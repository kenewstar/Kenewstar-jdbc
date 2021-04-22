package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.annotation.OfTable;
import org.kenewstar.jdbc.core.factory.SqlFactory;
import org.kenewstar.jdbc.core.page.Page;
import org.kenewstar.jdbc.core.page.PageCondition;
import org.kenewstar.jdbc.core.sql.Sql;
import org.kenewstar.jdbc.core.sql.SqlFragment;
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
public abstract class CommonExecutor implements JdbcExecutor, SqlFragment {

    private static final Logger logger = Logger.getLogger("common");

    protected KenewstarStatement statement;

    public CommonExecutor() {
        this.statement = new KenewstarStatement();
    }

    public CommonExecutor(String configPath) {
        this.statement = new  KenewstarStatement(configPath);
    }

    /**
     * 多表返回类型
     * @param resultType 返回类型
     * @return sql
     */
    protected StringBuilder resultType(Class<?> resultType) {
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

    /**
     * 记录数统计执行器
     * @param sql sql对象
     * @return count 获取记录总数
     */
    protected long sqlCountExecutor(Sql sql) {
        List<Map<String, Object>> count =
                statement.preparedSelectExecutor(sql.getSql().toString(),
                        sql.getParams().toArray(new Object[]{}));
        return (long) count.get(0).get(SqlKeyWord.COUNT);
    }

    /**
     * Sql执行返回结果映射执行器
     * @param sql sql对象
     * @param entityClass 实体类
     * @param <T> t
     * @return list
     */
    protected  <T> List<T> sqlResultExecutor(Sql sql, Class<T> entityClass) {
        // 打印Sql语句
        logger.info("Executed SQL ===> "+sql.getSql().toString());
        // 执行查询
        List<Map<String, Object>> maps =
                statement.preparedSelectExecutor(sql.getSql().toString(),
                        sql.getParams().toArray(new Object[]{}));
        // 结果
        List<T> result = new ArrayList<>(maps.size());

        Map<String, String> columnAndField = DataTableInfo.getColumnAndField(entityClass);
        maps.forEach(map -> {
            try {
                T t = entityClass.newInstance();
                for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
                    String fieldName = columnAndField.get(mapEntry.getKey());
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
    public <T> List<T> selectList(Class<?> fromClass, Class<T> resultType, MapTo mapTo) {

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
        List<T> result = new ArrayList<>(maps.size());
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
        // 构建Sql前缀对象
        Sql sql = buildSelectSqlFragment(entityClass);
        // 组装条件
        mapTo.conditionSql(sql);
        // 返回结果
        return sqlResultExecutor(sql, entityClass);
    }


    @Override
    public <T> Page<T> selectList(Class<T> entityClass, MapTo mapTo, PageCondition condition) {
        // 构建Sql select 片段
        Sql sql = buildSelectSqlFragment(entityClass);
        // 构建 count(*) 查询片段
        Sql countSql = buildCountSqlFragment(entityClass);
        // 构建sql查询条件
        mapTo.conditionSql(sql);
        mapTo.conditionSql(countSql);
        // 分页结果对象
        Page<T> pageList = new Page<>(condition.getPageNumber(), condition.getPageSize());

        // 构建sql 分页
        buildPageSqlFragment(sql, pageList.getPageNum(), pageList.getPageSize());

        // 设置总记录数
        pageList.setTotal(sqlCountExecutor(countSql));
        // 执行查询
        List<T> result = sqlResultExecutor(sql, entityClass);
        pageList.setRows(result);

        long pages = pageList.getTotal() / pageList.getPageSize();
        pageList.setPages(pageList.getTotal() % pageList.getPageSize() == 0 ? pages : pages + 1);
        // 返回分页结果
        return pageList;
    }


    @Override
    public int batchDelete(Class<?> entityClass, List<?> ids) {
        Assert.notNull(ids);
        // 构建删除Sql语句
        Sql sql = buildDeleteSqlFragment(entityClass);
        StringBuilder deleteSql = sql.getSql();
        // 组装参数
        List<List<Object>> paramList = new ArrayList<>(ids.size());
        for (Object id : ids) {
            List<Object> params = new ArrayList<>(1);
            params.add(id);
            paramList.add(params);
        }
        // 打印SQL语句
        logger.info("Executed SQL ===> " + deleteSql.toString());
        // 执行结果
        return statement.preparedBatchExecutor(deleteSql.toString(), paramList, 0);
    }


    @Override
    public int batchUpdate(List<?> paramList) {
        Assert.notNull(paramList);
        if (paramList.isEmpty()) {
            return 0;
        }
        paramList.forEach(obj -> {
            // 构建Sql对象
            Sql sql = buildUpdateSqlFragment(obj);
            statement.preparedUpdateExecutor(sql.getSql().toString(),
                    sql.getParams().toArray(new Object[]{}));

        });
        return paramList.size();
    }


    @Override
    public int batchInsert(List<?> paramList) {
        return batchInsert(paramList, 0);
    }


    @Override
    public int batchInsert(List<?> paramList, int count) {
        Assert.notNull(paramList);
        if (paramList.isEmpty()) {
            return 0;
        }
        Object obj = paramList.get(0);
        Class<?> entityClass = obj.getClass();

        // 参数容器
        List<List<Object>> params = new ArrayList<>(paramList.size());
        // 表名
        String tableName = KenewstarUtil.getTableName(obj.getClass());
        Map<String, String> columnAndField = DataTableInfo.getColumnAndField(obj.getClass());

        // 组装sql
        StringBuilder sql = new StringBuilder(SqlKeyWord.INSERT);
        sql.append(SqlKeyWord.INTO)
                .append(tableName).append(SqlKeyWord.LEFT_BRACKETS);
        StringBuilder values = new StringBuilder(SqlKeyWord.VALUES);
        values.append(SqlKeyWord.LEFT_BRACKETS);

        // 参数名
        List<String> fieldNames = new ArrayList<>(columnAndField.size());
        columnAndField.forEach((k, v) -> {
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
        int rows = statement.preparedBatchExecutor(sql.toString(), params, count);
        // 打印SQL语句
        logger.info("Executed SQL ===> "+sql.toString());
        return rows;
    }


}


