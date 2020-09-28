package org.kenewstar.jdbc.util;

import org.kenewstar.jdbc.exception.String2BooleanException;

import java.util.Objects;

/**
 * 类型转换
 * @author kenewstar
 * @date 2020-08-12
 * @version 0.2
 */
public class TypeConverter {
    /**
     * 判断字符串是否可以转换为整数
     * @param str 字符串参数
     * @return 返回是否可转换
     */
    public static boolean isInt(String str){
        try {
            Integer.parseInt(str);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否可以转换为boolean类型
     * @param str 字符串参数
     * @return 返回是否可转换
     */
    public static boolean isBoolean(String str){
        String t = "true";
        String f = "false";
        if (Objects.equals(t,str)||Objects.equals(f,str)){
            return true;
        }
        return false;
    }

    /**
     * 将字符串转换为boolean类型
     * @param str 字符串参数
     * @return 返回是否可转换
     */
    public static boolean str2Boolean(String str){
        String t = "true";
        // 是否可转
        boolean flag = isBoolean(str);
        if (!flag){
            throw new String2BooleanException("String cannot be converted to Boolean");
        }
        return Objects.equals(str, t);
    }
}
