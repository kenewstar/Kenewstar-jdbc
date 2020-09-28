package org.kenewstar.jdbc.core.datasource.knspool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 框架自带的连接池(自实现简单连接池)
 * @author kenewstar
 * @date 2020-08-12
 * @version 0.2
 */
public abstract class AbstractKnsDataSource implements DataSource {
    /**
     * 获取一个连接
     * @return 返回一个连接对象
     */
    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    /**
     * 初始化数据库连接池
     */
    public abstract void init();

    /**
     * 释放一个连接
     */
    public abstract void close();

    /**
     * 销毁数据库连接池
     */
    public abstract void destroy();




}
