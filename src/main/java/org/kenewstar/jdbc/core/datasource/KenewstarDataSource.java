package org.kenewstar.jdbc.core.datasource;

import org.kenewstar.jdbc.exception.KenewstarDataSourceException;
import org.kenewstar.jdbc.util.JdbcProperties;

import java.util.Map;

/**
 * 存储jdbc相关连接信息
 * @author kenewstar
 */
public class KenewstarDataSource {

    private static final String  DRIVER_CLASS_NAME = "driverClassName";
    private static final String URL = "url";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    /**
     * jdbc驱动
     */
    private String driverClassName;
    /**
     * jdbcUrl
     */
    private String url;
    /**
     * jdbc连接用户名
     */
    private String username;
    /**
     * jdbc连接密码
     */
    private String password;

    private KenewstarDataSource dataSource;
    /**
     * 无参构造器
     */
    public KenewstarDataSource(){ }

    /**
     * 全参构造器
     * @param driverClassName 驱动
     * @param url 连接
     * @param username 用户名
     * @param password 密码
     */
    public KenewstarDataSource(String driverClassName, String url, String username, String password) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;

    }

    /**
     * 通过设置properties属性文件的位置，设置数据源
     * @param path properties文件位置
     */
    public void setKenewstarDataSource(String path){
        //初始化properties文件
        JdbcProperties.initProperties(path);
        //从jdbc.properties文件中获取内容
        Map<String, String> dataSource = JdbcProperties.getPropKeyAndValue();

        //设置数据源的四个属性
        this.setDriverClassName(dataSource.get(DRIVER_CLASS_NAME));
        this.setUrl(dataSource.get(URL));
        this.setUsername(dataSource.get(USERNAME));
        this.setPassword(dataSource.get(PASSWORD));
        //提供给dataSource属性，以便对外开放
        this.dataSource = new KenewstarDataSource(driverClassName,url,username,password);
    }


    public KenewstarDataSource getKenewstarDataSource(){
        return dataSource;
    }

    /**
     * getter && setter 方法
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
