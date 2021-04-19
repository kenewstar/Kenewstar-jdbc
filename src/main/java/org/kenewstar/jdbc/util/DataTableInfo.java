package org.kenewstar.jdbc.util;

import org.kenewstar.jdbc.annotation.Column;
import org.kenewstar.jdbc.annotation.Id;
import org.kenewstar.jdbc.annotation.Table;
import org.kenewstar.jdbc.exception.EntityAnnoException;
import org.kenewstar.jdbc.exception.IdNotExistException;
import org.kenewstar.jdbc.exception.IdNotUniqueException;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 该工具类，用于通过实体类上的注解
 * 获取数据表的相关信息(表名，列名，主键id)
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class DataTableInfo {


    /**
     * 获取类的所有属性信息以及对应的数据表列名信息
     * 将属性名与列名放入map中一一对应存储
     * @param clazz 类对象
     * @return 返回列名与属性名的map对象
     */
    public static Map<String,String> getColumnAndField(Class<?> clazz) {
        // 获取类中所有声明的字段
        Field[] fields = clazz.getDeclaredFields();
        Map<String,String> columnAndField = new HashMap<>(fields.length);
        // 遍历所有属性
        for (Field field : fields) {
            // 判断属性上是否有注解
            boolean column = field.isAnnotationPresent(Column.class);
            if (column) {
                // 属性上有该注解
                // 获取该注解
                Column col = field.getAnnotation(Column.class);
                // 通过注解获取属性columnName的值
                String columnName = col.columnName();
                // 判断columnName是否为空字符串
                if ("".equals(columnName)){
                    // 若为空串，则将属性名作为列名
                    columnName = field.getName();
                }
                //将属性名与列名放入Map中
                columnAndField.put(columnName,field.getName());
            }
        }
        //返回列名与字段名的映射信息
        return columnAndField;
    }

    /**
     * 获取该类中被@Id标注的属性，有且只有一个
     * @param clazz 类对象
     * @return 返回id名称,数据库表的id列名
     */
    public static String getIdName(Class<?> clazz) {

        List<String> ids = new ArrayList<>();

        boolean annoTable = clazz.isAnnotationPresent(Table.class);
        // 获取类的所有属性
        Field[] fields = clazz.getDeclaredFields();
        // 判断类上是否存在@Table注解
        if (annoTable){
            // 类上存在该注解
            for (Field field : fields) {
                // 遍历属性,找出所有@Id标注的属性
                Id id = field.getAnnotation(Id.class);
                //id的名称
                String idName;
                if (Objects.isNull(id)) {
                     continue;
                }
                idName = id.value();
                //idName为空串,则使用属性名作为id的名字
                if ("".equals(idName)){
                    idName = field.getName();
                }
                //添加到list中
                ids.add(idName);
            }
        }
        //判断是否有多个id注解
        if (ids.size() > 1) {
            //抛出Id注解不唯一的异常
            throw new IdNotUniqueException("the @Id annotation is not unique");
        }
        if (ids.size() == 0) {
            //抛出Id注解不存在
            throw new IdNotExistException("the @Id annotation is not exist" );
        }
        //返回唯一注解
        return ids.get(0);
    }

    /**
     * 获取属性名与列名的映射集合
     * key   : 属性名
     * value : 列名
     * @param clazz 类
     * @return 返回映射集合
     */
    public static Map<String,String> getFieldAndColumn(Class<?> clazz) {

        // 获取所有属性
        Field[] fields = clazz.getDeclaredFields();
        // 属性名为key 列名为value
        Map<String,String> fieldAndColumn = new HashMap<>(fields.length);
        // 遍历属性
        for (Field field : fields) {
            // 获取属性名
            String fieldName = field.getName();
            // 获取注解上的列名
            Column column = field.getAnnotation(Column.class);
            String columnName = column.columnName();
            if ("".equals(columnName)){
                // 列名为空串
                columnName = fieldName;
            }
            // 将属性名与列名放入Map中
            fieldAndColumn.put(fieldName, columnName);
        }
        // 返回属性与列的映射集合
        return fieldAndColumn;
    }

    /**
     * 根据反射Field获取列名
     * @param field 反射属性对象
     * @return 列名
     */
    public static String getColumnNameByField(Field field) {
        boolean annotationPresent = field.isAnnotationPresent(Column.class);
        String columnName = "";
        if (annotationPresent) {
            Column column = field.getAnnotation(Column.class);
            columnName = column.columnName();
        }
        if (Objects.equals(columnName, "")) {
            columnName = field.getName();
        }

        return columnName;
    }


}
