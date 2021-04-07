package org.kenewstar.jdbc.pool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/3
 */
public abstract class AbstractDataSource implements DataSource {

    public static final int DEFAULT_SIZE = 0;
    public static final int DEFAULT_INIT_SIZE = 4;
    public static final int DEFAULT_MAX_SIZE = 8;
    public static final int DEFAULT_IDLE_SIZE = 4;
    public static final long DEFAULT_TIME = 5000;
    protected final Queue<Connection> dataSource = new ConcurrentLinkedQueue<>();
    protected volatile AtomicInteger activeSize  = new AtomicInteger(DEFAULT_SIZE);
    protected volatile long waitTime   = DEFAULT_TIME;
    protected volatile int initialSize = DEFAULT_INIT_SIZE;
    protected volatile int maxSize     = DEFAULT_MAX_SIZE;
    protected volatile int minIdle     = DEFAULT_IDLE_SIZE;
    protected volatile String jdbcUrl;
    protected volatile String username;
    protected volatile String password;
    protected volatile String driverClass;
    protected volatile boolean initStatus;

    public AbstractDataSource() {
    }

    public AbstractDataSource(String driverClass, String jdbcUrl, String username, String password) {
        this.driverClass = driverClass;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    protected void initDataSource() throws SQLException, ClassNotFoundException {
        validPoolSize();
        if (this.initialSize <= 0) {
            return;
        }
        validateNull(this.jdbcUrl);
        validateNull(this.driverClass);
        validateNull(this.username);
        validateNull(this.password);
        for (int i = 0; i < this.initialSize; i ++){
            // 创建连接，放入池中
            dataSource.add(newProxyConnection());
        }
    }

    public Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName(this.driverClass);
        return DriverManager.getConnection(this.jdbcUrl, this.username, this.password);
    }

    private void validateNull(Object property){
        if (Objects.isNull(property)) {
            throw new NullPointerException("dataSource property is null");
        }
    }
    protected void validPoolSize() {
        if (initialSize > maxSize) {
            throw new IllegalArgumentException("pool size set error");
        }
    }
    protected Connection newProxyConnection() throws SQLException, ClassNotFoundException {
        return ConnectionHandler.createConnection(createConnection(),this);
    }

    @Override
    public Connection getConnection() {
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
}
