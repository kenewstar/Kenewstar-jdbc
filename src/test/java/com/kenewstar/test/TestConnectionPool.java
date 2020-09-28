package com.kenewstar.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.kenewstar.jdbc.core.datasource.ConnectionPool;
import org.kenewstar.jdbc.util.JdbcProperties;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

public class TestConnectionPool {

//    static {
//        JdbcProperties.initProperties("jdbc.properties");
//    }
    @Test
    public void test1() throws SQLException {

//        Map<String, String> propKeyAndValue =
//                JdbcProperties.getPropKeyAndValue();
//        String s = propKeyAndValue.get("datasource.type");
//        System.out.println(s);

        DataSource connPool = new ConnectionPool().getConnPool();
        System.out.println(connPool);



    }
}
