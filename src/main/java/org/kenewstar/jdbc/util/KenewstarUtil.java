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
     * 根据实体类获取对应表的名称
     * <p> Table 注解标注的实体类 </p>
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





}
