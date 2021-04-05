package com.kenewstar.multiple;

import com.kenewstar.UserAndDeptDTO;
import org.junit.Before;
import org.junit.Test;
import org.kenewstar.jdbc.core.MultipleTableExecutor;

import java.util.List;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/1
 */
public class MultipleTableTest {
    private MultipleTableExecutor executor;

    @Before
    public void before() {
        executor = new MultipleTableExecutor();
    }

    @Test
    public void test() {
        //executor.selectListAll(User.class);

    }

    /**
     * select user.id as id
     *        user.name as name
     *        user.age as age
     *        dept.name as deptName
     * from user
     * left join dept
     *        on user.dept_id = dept.id
     *
     * where ......
     */
    @Test
    public void test2() {
        executor.selectList(User.class, UserAndDeptDTO.class, sql -> {
            sql.leftJoin(Dept.class)
               .joinEq(User.class, "dept_id", Dept.class, "id")
               .leftJoin(Company.class)
               .joinEq(User.class,"company_id",Company.class, "id")
               .where()
               .gt(User.class, "age", 10);
               //.and().eq(Dept.class, "name", "bbbb");
        }).forEach(System.out::println);

    }

    @Test
    public void testThread() throws InterruptedException {
        for (int i=0; i<10; i++) {
        Thread thread = new Thread(() -> {
            List<UserAndDeptDTO> dtoList = executor.selectList(User.class, UserAndDeptDTO.class, sql -> {
                sql.leftJoin(Dept.class)
                        .joinEq(User.class, "dept_id", Dept.class, "id")
                        .leftJoin(Company.class)
                        .joinEq(User.class, "company_id", Company.class, "id")
                        .where()
                        .gt(User.class, "age", 10);
                //.and().eq(Dept.class, "name", "bbbb");
            });
            System.out.println(dtoList.size());
        });

        thread.start();
        thread.join();
        }
    }
}
