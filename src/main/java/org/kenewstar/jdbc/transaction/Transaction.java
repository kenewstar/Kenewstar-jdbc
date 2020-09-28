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
     */
    Connection getConnection();

    /**
     * 开启事务
     */
    void begin();

    /**
     * 事务提交
     */
    void commit();

    /**
     * 事务回滚
     */
    void rollBack();

    /**
     * 关闭连接
     */
    void close();

}
