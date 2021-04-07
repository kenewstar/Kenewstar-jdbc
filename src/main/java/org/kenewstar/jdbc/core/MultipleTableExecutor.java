package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.annotation.OfTable;
import org.kenewstar.jdbc.core.sql.Sql;
import org.kenewstar.jdbc.core.sql.SqlKeyWord;
import org.kenewstar.jdbc.function.MapTo;
import org.kenewstar.jdbc.util.MultipleTableUtil;

import java.lang.reflect.Field;
import java.util.*;
/**
 * 多表操作执行器
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/1
 */
public class MultipleTableExecutor extends KenewstarJdbcExecutor{

    protected StringBuilder resultType(Class<?> resultType) {
        StringBuilder sql = new StringBuilder(SqlKeyWord.SELECT);
        // 获取所有属性
        Field[] fields = resultType.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(OfTable.class)) {
                OfTable ofTable = field.getAnnotation(OfTable.class);
                Class<?> entityClass = ofTable.entityClass();
                // 获取表名
                String tableName = MultipleTableUtil.getTableName(entityClass);
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

    public <T> List<T> selectList(Class<?> fromClass ,Class<T> resultType, MapTo mapTo) {
        List<T> result;
        Sql sql = new Sql();
        sql.sql.append(resultType(resultType));
        // from user
        sql.sql.append(SqlKeyWord.FROM)
               .append(MultipleTableUtil.getTableName(fromClass))
               .append(SqlKeyWord.BLANK);
        mapTo.conditionSql(sql);
        // 执行sql
        List<Map<String, Object>> maps
                = statement.preparedSelectExecutor(sql.sql.toString(), sql.params.toArray(new Object[]{}));
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


}


