package org.kenewstar.jdbc.transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * jdbc事务管理机制
 * @author kenewstar
 */
public class JdbcTransaction implements Transaction{
    private Connection connection;
    private DataSource dataSource;

    public JdbcTransaction(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null){
            dataSource.getConnection();
        }
        return connection;
    }

    @Override
    public void begin() throws SQLException {
        if (connection!=null && connection.getAutoCommit()){
            connection.setAutoCommit(false);
        }
    }

    @Override
    public void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()){
            // 连接不为空，且自动提交被关闭
            connection.commit();
        }
    }

    @Override
    public void rollBack() throws SQLException {
        if (connection != null && !connection.getAutoCommit()){
            // 连接不为空，且自动提交被关闭
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null){
            connection.close();
        }
    }


}
