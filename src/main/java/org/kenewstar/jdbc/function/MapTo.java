package org.kenewstar.jdbc.function;

import org.kenewstar.jdbc.core.sql.Sql;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/1
 */
@FunctionalInterface
public interface MapTo {
    /**
     * 条件SQL
     * @param sql sql构建对象
     */
    void conditionSql(Sql sql);
}
