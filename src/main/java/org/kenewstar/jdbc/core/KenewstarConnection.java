package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.core.datasource.KenewstarDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接
 * @author kenewstar
 */
public class KenewstarConnection {

    private static final String JDBC_PROP = "jdbc.properties";

    private KenewstarDataSource dataSource;

    public KenewstarConnection() {
        this.dataSource = new KenewstarDataSource();
        //固定properties路径
        this.dataSource.setKenewstarDataSource(JDBC_PROP);
    }

    /**
     * 加载驱动
     */
    private void loadDriver(){
        //加载驱动
        try {
            Class.forName(dataSource.getDriverClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     * @return 返回连接
     */
    public Connection getConnection() {
        //加载驱动
        loadDriver();
        //获取连接
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    dataSource.getUrl(),
                    dataSource.getUsername(),
                    dataSource.getPassword()
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //返回连接
        return connection;

    }

}
