package org.kenewstar.jdbc.dao;

import org.junit.Before;
import org.junit.Test;
import org.kenewstar.jdbc.core.KenewstarJdbcExecutor;
import org.kenewstar.jdbc.core.KenewstarStatement;
import org.kenewstar.jdbc.demo.User;

import java.util.List;
import java.util.Map;

public class TestDao {

    private KenewstarJdbcExecutor executor;

    @Before
    public void init(){
        executor = new KenewstarJdbcExecutor();
    }
    //=================================================//


    /**
     * 根据实体类中的id更新实体对象
     */
    @Test
    public void testUpdateUserById(){
        String sql = "update user set name=?,age=? where id=?";
        User user = new User();
        user.setId(8);
        user.setAge(88);
        user.setName("mkkkkku");

        //执行
        int i = executor.updateEntity(sql, user.getName(), user.getAge(), user.getId());
        System.out.println(i);
    }

    @Test
    public void testDelete(){
        executor.deleteEntity("delete from user where id=?",10);
    }

    /**
     * 测试使用SQL语句执行插入
     */
    @Test
    public void testInsert(){
        String sql = "insert into user(name,age) values(?,?)";
        User user = new User();
        user.setAge(88);
        user.setName("mkkkkku");

        //执行
        int i = executor.insertEntity(sql, user.getName(), user.getAge());
        System.out.println(i);
    }

    /**
     * 测试查询执行器
     */
    @Test
    public void testSelectExecutor(){
        KenewstarStatement statement = new KenewstarStatement();
        String sql = "select * from user where id=?";
        String sql2 = "select * from user";
        List<Map<String, Object>> objects = statement.preparedSelectExecutor(sql2);
        System.out.println(objects);

    }
    /**
     * 测试查询执行器2
     */
    @Test
    public void testSelectExecutor2(){
        KenewstarStatement statement = new KenewstarStatement();
        String sql = "select id,name,age from user";

        List<User> list = executor.selectAllEntity(sql, User.class);
        List<User> users = executor.selectAll(User.class);

        System.out.println(list);
        System.out.println(users);

    }



}
