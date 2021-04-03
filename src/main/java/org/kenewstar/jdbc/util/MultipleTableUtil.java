package org.kenewstar.jdbc.util;

import org.kenewstar.jdbc.annotation.Table;

import java.util.Objects;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/3
 */
public class MultipleTableUtil {

    /**
     * 获取表名
     * @param clz
     * @return
     */
    public static String getTableName(Class<?> clz) {
        Table table = clz.getAnnotation(Table.class);
        String tableName = table.tableName();
        if (Objects.equals(tableName, "")) {
            tableName = clz.getSimpleName().substring(0, 1).toLowerCase() +
                    clz.getSimpleName().substring(1);
        }
        return tableName;
    }

}
