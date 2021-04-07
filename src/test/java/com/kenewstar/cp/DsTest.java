package com.kenewstar.cp;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.kenewstar.jdbc.pool.KnsDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/3
 */
public class DsTest {

    @Test
    public void test() throws SQLException, InterruptedException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///test");
        dataSource.setUsername("root");
        dataSource.setPassword("kenewstar");
        dataSource.setInitialSize(5);
        //dataSource.setMaxActive(2);
        //dataSource.setMaxWait(4000);
        long start = System.currentTimeMillis();
        System.out.println(dataSource.getConnection());
        Thread thread1 = new Thread(() -> {
            try {
                DruidPooledConnection connection1 = dataSource.getConnection();
                //connection1.prepareStatement()
                Thread.sleep(3000);
                connection1.close();
            } catch (SQLException | InterruptedException throwables) {
                throwables.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                DruidPooledConnection connection = dataSource.getConnection();
                Thread.sleep(5000);
                connection.close();
            } catch (SQLException | InterruptedException throwables) {
                throwables.printStackTrace();
            }

        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(dataSource);
    }

    @Test
    public void test2() throws SQLException {
        KnsDataSource dataSource = new KnsDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql:///test");
        dataSource.setUsername("root");
        dataSource.setPassword("kenewstar");
        dataSource.setMaxSize(2);
        Connection connection = dataSource.getConnection();
        dataSource.getConnection();
        dataSource.getConnection();

    }

    @Test
    public void test3() throws SQLException, PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql:///test");
        dataSource.setUser("root");
        dataSource.setPassword("kenewstar");
        System.out.println(dataSource.getConnection());
        BasicDataSource dataSource1 = new BasicDataSource();
    }

    @Test
    public void test4() throws Exception {
        KnsDataSource dataSource = new KnsDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql:///test");
        dataSource.setUsername("root");
        dataSource.setPassword("kenewstar");
        dataSource.setMaxSize(3);
        dataSource.setInitialSize(1);
//        dataSource.setWaitTime(9000);

        Thread thread1 = new Thread(() -> {
            try {
                Connection connection = dataSource.getConnection();
                System.out.println(Thread.currentThread().getName()+"--"+connection);
                Thread.sleep(3000);
                connection.close();
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
        },"t1");
        Thread thread2 = new Thread(() -> {
            try {
                Connection connection = dataSource.getConnection();
                System.out.println(Thread.currentThread().getName()+"--"+connection);
                Thread.sleep(1000);
                connection.close();
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }

        },"t2");
        Thread thread3 = new Thread(() -> {
            try {
                Connection connection = dataSource.getConnection();
                System.out.println(Thread.currentThread().getName()+"--"+connection);
                Thread.sleep(2000);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        },"t3");
        Thread thread4 = new Thread(() -> {
            try {
                Connection connection = dataSource.getConnection();
                System.out.println(Thread.currentThread().getName()+"--"+connection);
                Thread.sleep(2000);
                connection.close();
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }

        },"t4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

    }
    KnsDataSource dataSource;

    @Before
    public void before() {
        dataSource = new KnsDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql:///test");
        dataSource.setUsername("root");
        dataSource.setPassword("kenewstar");
//        dataSource.setMaxSize(10);
//        dataSource.setInitialSize(4);
//        dataSource.setWaitTime(6000);
    }
    @Test
    public void test5() throws InterruptedException {
        for (int i=0;i<50;i++) {
            Thread thread = new Thread(() -> {
                Connection connection = dataSource.getConnection();
                try {
                    connection.close();
                    Thread.sleep(500);
                } catch (SQLException | InterruptedException throwables) {
                    throwables.printStackTrace();
                }
            });
            thread.start();
            thread.join();
            Thread thread1 = new Thread(() -> {
                Connection connection = dataSource.getConnection();
                try {
                    connection.close();
                    Thread.sleep(500);
                } catch (SQLException | InterruptedException throwables) {
                    throwables.printStackTrace();
                }
            });
            thread1.start();
            thread1.join();
        }
    }
}
