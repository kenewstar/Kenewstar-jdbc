package org.kenewstar.jdbc.core.base;

import java.lang.reflect.Field;

/**
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/18
 */
public abstract class BaseEntity {


    @Override
    public String toString() {
        Class<?> clazz = this.getClass();
        // 获取所有属性
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder toString = new StringBuilder(clazz.getSimpleName());
        toString.append('{');
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                toString.append(field.getName())
                        .append("=")
                        .append(field.get(this))
                        .append(", ");

            }
            toString.setCharAt(toString.length() - 1, '}');
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return toString.toString();
    }
}
