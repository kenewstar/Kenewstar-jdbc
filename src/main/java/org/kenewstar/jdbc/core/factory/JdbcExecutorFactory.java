package org.kenewstar.jdbc.core.factory;

import org.kenewstar.jdbc.core.JdbcExecutor;
import org.kenewstar.jdbc.core.KenewstarJdbcExecutor;

/**
 * jdbcExecutor 工厂
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/7
 */
public class JdbcExecutorFactory {

    private static JdbcExecutor jdbcExecutor;

    public static JdbcExecutor getExecutor() {
        return new KenewstarJdbcExecutor();
    }

    public static JdbcExecutor getExecutor(String configPath) {
        return new KenewstarJdbcExecutor(configPath);
    }



}
