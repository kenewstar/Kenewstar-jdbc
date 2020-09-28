package org.kenewstar.jdbc.util;

import org.kenewstar.jdbc.exception.PropertiesFileNotFoundException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 获取jdbcProperties的属性值
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class JdbcProperties {
    /**
     * 用于存储读取后的properties中的键值对
     */
    private static Map<String,String> propKeyAndValue = new HashMap<>();

    /**
     * 初始化properties,
     * 目的是为了加载properties中的属性值,存入Map中
     * @param path properties文件的classpath路径
     */
    public static void initProperties(String path){

        //创建properties对象
        Properties prop = new Properties();
        //声明文件输入流
        InputStream is = null;
        try {
            //读取properties文件
            is = ClassLoader.getSystemResourceAsStream(path);
            //path路径下找不到该文件，则is为null,加载输入流时会报NPE
            if (is==null){
                //抛出为找到异常
                throw new PropertiesFileNotFoundException(
                        "properties file not found,path: "+path);
            }
            //加载properties配置文件
            prop.load(is);

        }  catch (IOException e) {
            e.printStackTrace();
        }
        //获取properties中所有的key
        Enumeration<?> propKeys = prop.propertyNames();
        //propKeys中的值
        while (propKeys.hasMoreElements()){
            //获取一个key
            String propKey = (String) propKeys.nextElement();
            //获取key对应的value
            String propValue = prop.getProperty(propKey);

            //将键值对存入Map中
            propKeyAndValue.put(propKey,propValue);

        }


    }

    /**
     * 获取properties的键值对信息
     * @return 返回键值对的Map信息
     */
    public static Map<String, String> getPropKeyAndValue() {
        return propKeyAndValue;
    }
}
