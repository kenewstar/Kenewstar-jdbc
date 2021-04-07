package org.kenewstar.jdbc.transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * jdbc事务管理机制
 * @author kenewstar
 */
public class JdbcTransaction implements Transaction{
    private final Connection connection;

    public JdbcTransaction(Connection connection){
        this.connection = connection;
    }

    @Override
    public Connection getConnection(){
        return connection;
    }

    @Override
    public void begin(){

        try{
            if (connection!=null && connection.getAutoCommit()){
                connection.setAutoCommit(false);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void commit(){
        try{
            if (connection != null && !connection.getAutoCommit()){
                // 连接不为空，且自动提交被关闭
                connection.commit();
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void rollBack() {
        try {
            if (connection != null && !connection.getAutoCommit()){
                // 连接不为空，且自动提交被关闭
                connection.rollback();
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        try{
            if (connection != null){
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}
