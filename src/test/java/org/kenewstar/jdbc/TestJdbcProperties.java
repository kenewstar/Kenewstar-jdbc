package org.kenewstar.jdbc;

import org.kenewstar.jdbc.util.JdbcProperties;

import java.util.Map;

/**
 * 测试jdbcProperties
 */
public class TestJdbcProperties {

    public static void main(String[] args) {

        //初始化properties
        JdbcProperties.initProperties("jdbc.properties");

        //获取properties相关数据
        Map<String, String> map = JdbcProperties.getPropKeyAndValue();

        for (String key:map.keySet()
             ) {
            System.out.println(key+"-----"+map.get(key));
        }




    }
}
