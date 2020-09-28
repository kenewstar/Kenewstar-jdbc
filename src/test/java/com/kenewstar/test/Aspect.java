package com.kenewstar.test;

import org.aspectj.lang.annotation.Before;

/**
 * @author kenewstar
 * @version 1.0
 * @date 2020/9/28
 */
@org.aspectj.lang.annotation.Aspect
public class Aspect {

    @Before("execution(* com.kenewstar.test.TestJdbcExecutor.main())")
    public void before(){
        System.out.println("before.......");
    }
}
