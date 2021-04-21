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

    /**
     * 默认路径获取实例对象
     * @return jdbcExecutor
     */
    public static JdbcExecutor getExecutor() {
        return KenewstarJdbcExecutor.getInstance(null);
    }

    /**
     * 自定义路径获取实例对象
     * @param configPath classpath:配置文件
     * @return jdbcExecutor
     */
    public static JdbcExecutor getExecutor(String configPath) {
        return KenewstarJdbcExecutor.getInstance(configPath);
    }



}
