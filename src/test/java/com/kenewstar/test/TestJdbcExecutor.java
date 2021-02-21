package com.kenewstar.test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Before;
import org.junit.Test;
import org.kenewstar.jdbc.core.JdbcExecutor;
import org.kenewstar.jdbc.core.KenewstarJdbcExecutor;
import org.kenewstar.jdbc.core.KenewstarStatement;
import org.kenewstar.jdbc.core.datasource.KenewstarDataSource;
import org.kenewstar.jdbc.transaction.JdbcTransaction;
import org.kenewstar.jdbc.transaction.Transaction;
import org.kenewstar.jdbc.util.JdbcProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TestJdbcExecutor {

    private JdbcExecutor jdbcExecutor;
    @Before
    public void init(){
        jdbcExecutor = new KenewstarJdbcExecutor();
    }

    /**
     * 测试查询全部
     */
    @Test
    public void test01(){


        List<User> users = jdbcExecutor.selectAll(User.class);
        // 遍历数据
        users.forEach(System.out::println);


    }

    @Test
    public void test02() throws SQLException {
        KenewstarStatement statement = new KenewstarStatement();
        Connection connection = statement.getConnection();
        Transaction transaction = new JdbcTransaction(connection);
        System.out.println(connection);

        transaction.begin();
        int delete = jdbcExecutor.deleteById(19, User.class);
        System.out.println(1/0);
        int delete2 = jdbcExecutor.deleteById(20, User.class);
        transaction.commit();
        transaction.close();
        System.out.println(delete+delete2);

    }

    @Test
    //@org.kenewstar.jdbc.annotation.Transaction
    public void test03(){
        Transaction transaction = jdbcExecutor.getTransaction();
        try {
            transaction.begin();
            int delete = jdbcExecutor.deleteById(19, User.class);
            int i = 10/0;
            int delete2 = jdbcExecutor.deleteById(20, User.class);
            System.out.println(delete+delete2);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollBack();
            e.printStackTrace();
        } finally {
            transaction.close();
        }

    }

    @Test
    public void test04(){

        jdbcExecutor.insert(new User(null,"kenewstar",20));


    }





}
