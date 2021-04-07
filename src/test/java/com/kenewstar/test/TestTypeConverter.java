package com.kenewstar.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import org.kenewstar.jdbc.core.datasource.ConnectionPool;
import org.kenewstar.jdbc.util.TypeConverter;

import javax.sql.DataSource;
import java.sql.SQLException;

public class TestTypeConverter {

    @Test
    public void test(){
        boolean flag = TypeConverter.isInt("1002q");
        System.out.println(flag);

        flag = TypeConverter.isBoolean("false");
        System.out.println(flag);

        Boolean fff = TypeConverter.str2Boolean("false");
        System.out.println(fff);

        new BasicDataSource();
    }
    @Test
    public void test1(){
        ComboPooledDataSource dataSource = (ComboPooledDataSource)new ConnectionPool().getConnPool();

        System.out.println(dataSource);
    }

    @Test
    public void test2(){
        BasicDataSource basicDataSource = (BasicDataSource) new ConnectionPool().getConnPool();

        System.out.println(basicDataSource);
        System.out.println(basicDataSource.getDefaultAutoCommit());
        System.out.println(basicDataSource.getMaxTotal());
        System.out.println(basicDataSource.getMaxWaitMillis());
        System.out.println(basicDataSource.getTimeBetweenEvictionRunsMillis());
    }
    @Test
    public void test3(){
        DruidDataSource dataSource = (DruidDataSource) new ConnectionPool().getConnPool();

        System.out.println(dataSource);
        System.out.println(dataSource.getInitialSize());
        System.out.println(dataSource.getMaxActive());

    }
    @Test
    public void test4() throws SQLException {

    }
}
