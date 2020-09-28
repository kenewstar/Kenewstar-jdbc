package org.kenewstar.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务管理机制
 * @author kenewstar
 * @date 2020-08-12
 * @version 0.2
 */
public interface Transaction {

    /**
     * 获取连接
     * @return 返回连接
     * @throws  SQLException 异常
     */
    Connection getConnection() throws SQLException;

    /**
     * 开启事务
     * @throws SQLException 异常
     */
    void begin() throws SQLException;

    /**
     * 事务提交
     * @throws SQLException 异常
     */
    void commit() throws SQLException;

    /**
     * 事务回滚
     * @throws SQLException 异常
     */
    void rollBack() throws SQLException;

    /**
     * 关闭连接
     * @throws SQLException 异常
     */
    void close() throws SQLException;

}
