package org.kenewstar.jdbc.testAnno;

import org.junit.Before;
import org.junit.Test;
import org.kenewstar.jdbc.core.KenewstarJdbcExecutor;
import org.kenewstar.jdbc.demo.User;
import org.kenewstar.jdbc.util.DataTableInfo;

import java.util.List;
import java.util.Map;

public class TestAnnoEntity {
    private KenewstarJdbcExecutor jdbcExecutor;
    @Before
    public void init(){
        jdbcExecutor = new KenewstarJdbcExecutor();
    }

    @Test
    public void test(){
        String tableName = DataTableInfo.getTableName(User.class);
        System.out.println(tableName);

        //测试字段名
        Map<String, String> maps = DataTableInfo.getColumnNames(User.class);
        System.out.println(maps.get("id"));
        System.out.println(maps.get("name"));
        System.out.println(maps.get("age"));

        jdbcExecutor.insert(new User(null,"kkk",12));
    }

    /**
     * 测试根据id删除用户
     */
    @Test
    public void test1(){
        int i = jdbcExecutor.deleteById(14, User.class);
        System.out.println(i);
    }
    /**
     * 测试更新
     */
    @Test
    public void test2(){
        User user = new User(16, "kenewstar", 20);
        int i = jdbcExecutor.updateById(user);
        System.out.println(i);
    }

    /**
     * 测试查询
     */
    @Test
    public void test3(){
        User user = jdbcExecutor.selectById(15, User.class);
        System.out.println(user);
    }
    @Test
    public void testSelectEntityById(){
        String sql = "select name from user where id=?";
        User user = jdbcExecutor.selectEntityById(sql, User.class, 15);
        System.out.println(user);
        System.out.println(user.getAge());
    }

    /**
     * 测试查询
     */
    @Test
    public void testSelectById(){
        User user = jdbcExecutor.selectById(15, User.class);
        System.out.println(user);
    }
    /**
     * 测试查询全部
     */
    @Test
    public void testSelectAll(){
        List<User> users = jdbcExecutor.selectAll(User.class);
        System.out.println(users);
    }
    /**
     * 测试使用了@Param注解的查询语句
     */
    @Test
    public void testSelectParam(){
        //jdbcExecutor.selectByColumn(name,User.class)
        // SQL ： select * from user where columnName = t_name.value
        // 根据用户名查询
        String sql = "select * from user where name=?";
        List<User> kenewstar = jdbcExecutor.selectListByColumns(sql, User.class, "kkk1");
        System.out.println(kenewstar);

    }



}
