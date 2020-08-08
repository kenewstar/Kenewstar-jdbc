package org.kenewstar.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.kenewstar.jdbc.core.KenewstarJdbcExecutor;
import org.kenewstar.jdbc.core.Page;
import org.kenewstar.jdbc.core.PageCondition;
import org.kenewstar.jdbc.core.Sort;
import org.kenewstar.jdbc.demo.User;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;

public class TestDemo1 {

    private KenewstarJdbcExecutor jdbcExecutor;
    @Before
    public void init(){
        jdbcExecutor = new KenewstarJdbcExecutor();
    }
    @Test
    public void test1(){
        long count = jdbcExecutor.count(User.class);
        System.out.println(count);


    }

    /**
     * 测试排序
     */
    @Test
    public void test2(){
        List<Sort> sorts = new ArrayList<>();
        // 排序参数1
        Sort sort = new Sort();
        sort.setOrder(Sort.DESC);
        sort.setFieldName("age");
        // 参数2,在参数1成立条件下执行参数2
        sorts.add(sort);
        Sort sort1 = new Sort();
        sort1.setOrder(Sort.ASC);
        sort1.setFieldName("name");
        sorts.add(sort1);
        // 执行查询
        List<User> users = jdbcExecutor.selectAll(User.class, new ArrayList<>());
        for (User u:users) {
            System.out.println(u);
        }
    }

    /**
     * 测试分页
     */
    @Test
    public void test3(){
        Sort sort = new Sort("age",Sort.DESC);
        List<Sort> sorts = new ArrayList<>();
        sorts.add(sort);
        // 第三页，每页5条
        PageCondition condition = new PageCondition(5,0,sorts);

        // 执行分页查询操作
        Page<User> userPage = jdbcExecutor.selectAll(User.class, condition);

        System.out.println("当前页:"+userPage.getCurrentPage());
        System.out.println("总记录数:"+userPage.getTotalRecords());
        System.out.println("总页数:"+userPage.getTotalPages());
        System.out.println("每页大小:"+userPage.getPageSize());
        for (User u: userPage.getContents()) {
            System.out.println(u);
        }

    }


}
