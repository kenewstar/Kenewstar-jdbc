package org.kenewstar.jdbc.testAnno;

import org.junit.Test;
import org.kenewstar.jdbc.demo.TestDemo01;
import org.kenewstar.jdbc.util.SqlParamInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TestParam {

    @Test
    public void test1() throws NoSuchMethodException {
        Method test =
                TestDemo01.class.getMethod("test",
                        String.class,String.class,Integer.class);
        String[] paramName = SqlParamInfo.getParamName(test);
        for (String param :paramName) {
            System.out.println(param);
        }

    }
}
