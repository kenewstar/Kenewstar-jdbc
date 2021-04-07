package org.kenewstar.jdbc.pool;

import org.kenewstar.jdbc.pool.exception.RejectConnectionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/3
 */
public class KnsDataSource extends AbstractDataSource {

    private static final Logger logger = Logger.getLogger("knsDataSource");
    private static final long SECOND = 1000;
    private final Lock lock = new ReentrantLock();
    /**
     * 构造器初始化连接池
     */
    public KnsDataSource() {

    }

    @Override
    public Connection getConnection(){
        validPoolSize();
        validInitDataSource();
        waitForConnect();
        lock.lock();
        try {
            if (dataSource.isEmpty() && activeSize.get() < maxSize) {
                activeSize.incrementAndGet();
                return newProxyConnection();
            }
            rejectConnection();
            // 获取并移除连接池中的连接
            Connection connection = dataSource.remove();
            activeSize.incrementAndGet();

            return refresh(connection);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
        return null;
    }

    private void validInitDataSource() {
        if (! initStatus) {
            synchronized (this) {
                try {
                    if (! initStatus) {
                        initDataSource();
                        initStatus = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("all")
    private void waitForConnect() {
        if (waitTime < 0) waitTime = 0;
        int i = (int) (waitTime/SECOND);
        while (activeSize.get() >= maxSize && i > 0) {
            try {
                logger.info(Thread.currentThread().getName() + "---" + i);

                Thread.sleep(SECOND);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                i--;
            }
        }
    }

    /**
     * 拒绝连接
     */
    private void rejectConnection() {
        if (activeSize.get() >= maxSize) {
            throw new RejectConnectionException("connection size is max");
        }
    }

    /**
     * 刷新数据源
     * @param connection 数据源获取的连接
     * @return 连接
     * @throws SQLException exception
     */
    private Connection refresh(Connection connection) throws SQLException, ClassNotFoundException {
        if (connection != null && connection.isClosed()) {
            logger.info("dataSource refresh ......");
            connection = newProxyConnection();
            // 刷新数据源中的连接
            int newIndex = 0;
            int oldSize = dataSource.size();
            if (minIdle > maxSize) {
                minIdle = maxSize;
            }
            while (oldSize > 0) {
                Connection conn = dataSource.poll();
                // 关闭多余空闲连接
                if (conn != null && !conn.isClosed() && newIndex < minIdle) {
                    ConnectionHandler.KnsProxyConnection knsProxyConnection =
                            (ConnectionHandler.KnsProxyConnection) conn;
                    knsProxyConnection.directClose();
                }
                if (conn == null && newIndex < minIdle) {
                    dataSource.add(newProxyConnection());
                }
                if (conn != null && conn.isClosed() && newIndex < minIdle) {
                    dataSource.add(newProxyConnection());
                }

                newIndex ++;
                oldSize --;
            }
        }
        return connection;
    }




}
