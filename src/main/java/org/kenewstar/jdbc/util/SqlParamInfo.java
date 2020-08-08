package org.kenewstar.jdbc.util;

import org.kenewstar.jdbc.annotation.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 获取绑定@Param注解的参数信息
 * @author kenewstar
 */
public class SqlParamInfo {

    public static String[] getParamName(Method method){
        // 获取方法所有的参数
        Parameter[] parameters = method.getParameters();

        String[] values = new String[parameters.length];
        int index = 0;
        for (Parameter p: parameters){
            Param param = p.getAnnotation(Param.class);
            String value = param.name();
            values[index++] = value;
        }
        return values;
    }
}
