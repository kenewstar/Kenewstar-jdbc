package org.kenewstar.jdbc.demo;

import org.kenewstar.jdbc.annotation.Param;
import org.kenewstar.jdbc.core.KenewstarJdbcExecutor;

import java.util.List;

/**
 * @author kenewstar
 */
public class TestDemo01 {

    public void test(@Param(name = "ttt") String t,
                     @Param(name = "username") String name,
                     @Param(name = "userId") Integer id){

        System.out.println("test.............");
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        KenewstarJdbcExecutor jdbcExecutor = new KenewstarJdbcExecutor();
        User user = jdbcExecutor.selectById(17, User.class);
        User user1 = jdbcExecutor.selectById(18, User.class);
        User user2 = jdbcExecutor.selectById(19, User.class);
        User user3 = jdbcExecutor.selectById(20, User.class);
        User user4 = jdbcExecutor.selectById(21, User.class);
        User user5 = jdbcExecutor.selectById(22, User.class);
        System.out.println(user);

        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }

}
