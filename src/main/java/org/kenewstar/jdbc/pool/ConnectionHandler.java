package org.kenewstar.jdbc.pool;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/4
 */
public class ConnectionHandler {
    private static final Logger logger = Logger.getLogger("connection handler");

    public static class KnsProxyConnection implements InvocationHandler {
        private static final String CLOSE = "close";

        private final Connection connection;
        private final AbstractDataSource knsDataSource;

        public KnsProxyConnection(Connection connection, AbstractDataSource knsDataSource) {
            this.connection = connection;
            this.knsDataSource = knsDataSource;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Objects.equals(method.getName(), CLOSE)) {
                close(proxy);
                return null;
            }
            return method.invoke(connection, args);
        }

        /**
         * 释放连接
         * @param proxy 代理对象
         * @throws SQLException sql异常
         */
        public void close(Object proxy) throws SQLException {
            synchronized (knsDataSource) {
                logger.info("release connection ......");
                if (!connection.getAutoCommit()) {
                    connection.setAutoCommit(true);
                }
                knsDataSource.dataSource.add((Connection) proxy);
                knsDataSource.activeSize.decrementAndGet();
            }
        }

        /**
         * 真实关闭
         * @throws SQLException sql异常
         */
        public void directClose() throws SQLException {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
    }

    public static Connection createConnection(Connection connection, AbstractDataSource knsDataSource) {
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                new KnsProxyConnection(connection, knsDataSource));
    }
}
