package org.kenewstar.jdbc.core.factory;

import org.kenewstar.jdbc.core.sql.Sql;

/**
 * Sql构造对象工厂
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/7
 */
public class SqlFactory {


    public static Sql getSql() {
        return new Sql();
    }
}
