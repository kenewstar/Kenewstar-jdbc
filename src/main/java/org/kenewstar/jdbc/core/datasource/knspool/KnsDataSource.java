package org.kenewstar.jdbc.core.datasource.knspool;

import org.kenewstar.jdbc.core.datasource.KenewstarDataSource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Kns数据库连接池接口实现类
 * @author kenewstar
 * @date 2020-08-12
 * @version 0.2
 */
public class KnsDataSource extends AbstractKnsDataSource{

    private Connection connection = null;


    @Override
    public Connection getConnection() {
        connection = new KenewstarDataSource().getConnection();
        return connection;
    }


    @Override
    public void close() {
        try {
            if (connection != null){
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        // 初始化
    }

    @Override
    public void destroy() {
        // 销毁
    }


}
