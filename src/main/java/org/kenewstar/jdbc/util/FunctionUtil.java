package org.kenewstar.jdbc.util;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/6
 */
public class FunctionUtil {
    private static final String WRITE_REPLACE = "writeReplace";
    private static final String IS  = "is";
    private static final String GET = "get";

    public static String getColumnName(Serializable column) {
        Method method;
        SerializedLambda lambda;
        String columnName = null;
        try {
            method = column.getClass().getDeclaredMethod(WRITE_REPLACE);
            method.setAccessible(Boolean.TRUE);
            lambda = (SerializedLambda) method.invoke(column);
            columnName = resolveLambda(lambda);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnName;
    }
    private static String resolveLambda(SerializedLambda lambda) {
        String methodName = lambda.getImplMethodName();
        String tableName = "";
        String simpleName = "";
        if (Objects.nonNull(methodName) && methodName.startsWith(IS)) {
            simpleName = methodName.substring(2, 3).toLowerCase() +
                         methodName.substring(3);
        }
        if (Objects.nonNull(methodName)&& methodName.startsWith(GET)) {
            simpleName = methodName.substring(3, 4).toLowerCase() +
                         methodName.substring(4);
        }
        String implClass = lambda.getImplClass();
        String className = implClass.replace('/', '.');
        try {
            Class<?> clz = Class.forName(className);
            tableName = DataTableInfo.getTableName(clz);
            Field field = clz.getDeclaredField(simpleName);
            simpleName = DataTableInfo.getColumnNameByField(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableName + "." + simpleName;
    }
}
