package org.kenewstar.jdbc.core.datasource;

import org.kenewstar.jdbc.util.JdbcProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * 存储数据库连接信息
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class KenewstarDataSource {

    public static final String DRIVER_CLASS_NAME = "driverClassName";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    /**
     * 默认jdbc.properties的位置在classpath路径下
     */
    private static final String JDBC_PROP_PATH = "jdbc.properties";
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

    static {
        //初始化properties文件
        JdbcProperties.initProperties(JDBC_PROP_PATH);
    }
    /**
     * 无参构造器
     */
    public KenewstarDataSource(){
        //从jdbc.properties文件中获取内容
        Map<String, String> dataSource = JdbcProperties.getPropKeyAndValue();
        //设置数据源的四个属性
        setDriverClassName((String) dataSource.get(DRIVER_CLASS_NAME));
        setUrl((String) dataSource.get(URL));
        setUsername((String) dataSource.get(USERNAME));
        setPassword((String) dataSource.get(PASSWORD));
    }

    //====================================================================//

    /**
     * 加载驱动
     */
    private void loadDriver(){
        //加载驱动
        try {
            Class.forName(getDriverClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     * @return 返回连接
     */
    public Connection getConnection() {
        //加载驱动
        loadDriver();
        //获取连接
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    getUrl(),
                    getUsername(),
                    getPassword()
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //返回连接
        return connection;

    }

    //==========================================================================//

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
