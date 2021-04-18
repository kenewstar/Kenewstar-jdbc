package org.kenewstar.jdbc.util;

import org.kenewstar.jdbc.annotation.Table;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/7
 */
public class KenewstarUtil {
    private static final String BLANK = "";

    /**
     * 根据实体类获取表名
     * @param clz 表的实体类
     * @return 表名
     */
    public static String getTableName(Class<?> clz) {
        Table table = clz.getAnnotation(Table.class);
        // 检查table
        Assert.notNull(table);
        String tableName = table.tableName();
        if (Objects.equals(tableName, BLANK)) {
            tableName = clz.getSimpleName().substring(0, 1).toLowerCase() +
                        clz.getSimpleName().substring(1);
        }
        return tableName;
    }

    /**
     * 获取toString字符串
     * @param obj 对象
     * @return toString
     */
    public static String getToString(Object obj) {
        Class<?> clazz = obj.getClass();
        // 获取所有属性
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder toString = new StringBuilder(clazz.getSimpleName());
        toString.append('{');
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                toString.append(field.getName())
                        .append("=")
                        .append(field.get(obj))
                        .append(", ");

            }
            toString.setCharAt(toString.length() - 1, '}');
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return toString.toString();
    }



}
