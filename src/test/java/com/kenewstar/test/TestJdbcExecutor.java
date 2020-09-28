package com.kenewstar.test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Before;
import org.junit.Test;
import org.kenewstar.jdbc.core.JdbcExecutor;
import org.kenewstar.jdbc.core.KenewstarJdbcExecutor;
import org.kenewstar.jdbc.core.KenewstarStatement;
import org.kenewstar.jdbc.core.datasource.KenewstarDataSource;
import org.kenewstar.jdbc.util.JdbcProperties;

import javax.sql.DataSource;
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




}
