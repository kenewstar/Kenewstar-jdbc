package org.kenewstar.jdbc.core.datasource;

import org.kenewstar.jdbc.pool.KnsDataSource;
import org.kenewstar.jdbc.util.JdbcProperties;
import org.kenewstar.jdbc.util.TypeConverter;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * 数据库连接池
 * @author kenewstar
 * @date 2020-08-12
 * @version 0.2
 */
public class ConnectionPool {
    /**
     * 声明并获取日志对象
     */
    private static final Logger log = Logger.getLogger("connectionPool");
    /**
     * 数据库连接池
     */
    private static final String C3P0    = "ComboPooledDataSource";
    private static final String DBCP2   = "BasicDataSource";
    private static final String DRUID   = "DruidDataSource";
    private static final String DEFAULT = "kns";

    private static final String DRIVER  = "driverClassName";
    private static final String URL     = "url";
    private static final String USER    = "username";
    private static final String PWD     = "password";
    private static final String SET     = "set";

    /**
     * 声明数据库连接池接口
     */
    private DataSource dataSource;
    /**
     * 默认jdbc.properties的位置在classpath路径下
     */
    private static final String JDBC_PROP_PATH = "jdbc.properties";
    /**
     * 数据库连接池类型
     */
    private static final String TYPE = "datasource.type";
    /**
     * 声明数据库连接池配置信息
     */
    private final Map<String, String> propKeyAndValue;


    public ConnectionPool() {
        this(JDBC_PROP_PATH);
    }

    public ConnectionPool(String configPath) {
        // 初始化加载jdbc.properties配置文件
        JdbcProperties.initProperties(configPath);
        // 获取数据库连接池配置信息
        propKeyAndValue = JdbcProperties.getPropKeyAndValue();

        log.info("init jdbc.properties config info ......");

        log.info(propKeyAndValue.toString());

    }

    /**
     * 获取连接池对象(对外提供)
     * @return 返回一个连接池对象
     */
    public DataSource getConnPool() {
        // 获取数据库连接池的类型
        String dsType = propKeyAndValue.get(TYPE);
        // 通过反射实例化连接池对象
        Class<?> type = KnsDataSource.class;
        try {
            if (Objects.nonNull(dsType) && !"".equals(dsType)) {
                type = Class.forName(dsType);
            }
            // 获取指定连接池
            switch (type.getSimpleName()) {
                case C3P0:
                    // 判断数据库连接池是否是c3p0
                    dataSource = getC3p0DataSource(type);
                    break;
                case DBCP2:
                    // 判断数据库连接池是否是dbcp2
                    dataSource = getDbcp2DataSource(type);
                    break;
                case DRUID:
                    // 判断数据库连接池是否是druid
                    dataSource = getDruidDataSource(type);
                    break;
                default:
                    // 未使用上述连接池,则使用KnsDataSource
                    dataSource = getDefaultDataSource();
            }
            log.info("dataSource type ：" + dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    /**
     * 获取c3p0连接池的对象
     * @param pool 连接池的类类型
     * @return 返回数据库连接池
     */
    private DataSource getC3p0DataSource(Class<?> pool){
        Object c3p0 = null;
        try {
            // 创建一个对象
            c3p0 = pool.newInstance();
            // 设置连接信息
            Method driver = pool.getMethod("setDriverClass", String.class);
            driver.invoke(c3p0, propKeyAndValue.get(DRIVER));
            Method url = pool.getMethod("setJdbcUrl", String.class);
            url.invoke(c3p0, propKeyAndValue.get(URL));
            Method user = pool.getMethod("setUser", String.class);
            user.invoke(c3p0, propKeyAndValue.get(USER));
            Method pwd = pool.getMethod("setPassword", String.class);
            pwd.invoke(c3p0, propKeyAndValue.get(PWD));

            // 设置c3p0连接池的一些其他属性
            setConnPoolProperties(pool, c3p0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (DataSource) c3p0;
    }

    /**
     * 获取dbcp2连接池对象
     * @param pool 连接池的类类型
     * @return 返回连接池
     */
    private DataSource getDbcp2DataSource(Class<?> pool){
        Object dbcp2 = null;
        try {
            // 创建一个连接池对象
            dbcp2 = pool.newInstance();

            // 设置dbcp2连接池的属性
            setConnPoolProperties(pool,dbcp2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回 DataSource
        return (DataSource) dbcp2;
    }

    /**
     * 获取druid连接池对象
     * @param pool 连接池的类类型
     * @return 返回连接池
     */
    private DataSource getDruidDataSource(Class<?> pool){
        Object druid = null;
        try {
            // 创建一个连接池对象
            druid = pool.newInstance();

            // 设置druid连接池的属性
            setConnPoolProperties(pool, druid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (DataSource) druid;
    }

    private DataSource getDefaultDataSource() {
        KnsDataSource dataSource = new KnsDataSource();
        // 设置连接属性
        dataSource.setDriverClass(propKeyAndValue.get(DRIVER));
        dataSource.setJdbcUrl(propKeyAndValue.get(URL));
        dataSource.setUsername(propKeyAndValue.get(USER));
        dataSource.setPassword(propKeyAndValue.get(PWD));

        String initialSize = propKeyAndValue.get("initialSize");
        if (Objects.nonNull(initialSize)) {
            dataSource.setInitialSize(Integer.parseInt(initialSize));
        }

        String waitTime = propKeyAndValue.get("waitTime");
        if (Objects.nonNull(waitTime)) {
            dataSource.setWaitTime(Long.parseLong(waitTime));
        }

        String maxSize = propKeyAndValue.get("maxSize");
        if (Objects.nonNull(maxSize)) {
            dataSource.setMaxSize(Integer.parseInt(maxSize));
        }

        String minIdle = propKeyAndValue.get("minIdle");
        if (Objects.nonNull(minIdle)) {
            dataSource.setMinIdle(Integer.parseInt(minIdle));
        }

        return dataSource;
    }
    /**
     * 设置数据库连接池的属性
     * @param pool 连接池的类类型
     * @param connPool 连接池对象
     * @throws Exception 抛出的异常
     */
    private void setConnPoolProperties(Class<?> pool, Object connPool) throws Exception {
        // 获取该类下的所有public方法
        Method[] methods = pool.getMethods();
        for (Method m : methods) {
            Type[] types = m.getGenericParameterTypes();
            // 获取方法名
            String methodName = m.getName();
            // 获取set方法对应的属性名
            String fieldName = getPropNameBySetMethod(m);
            // 获取属性对应的值
            String propValue = propKeyAndValue.get(fieldName);
            // 判断值是否为null或者为空串
            if (Objects.isNull(propValue) || "".equals(propValue)){
                // 是，则跳出本次循环
                continue;
            }
            // 存在一个参数的方法，给该方法的对应的属性设置值
            if (types.length==1){
                if (types[0].equals(String.class)){
                    // 参数为String类型
                    m = pool.getMethod(methodName, String.class);
                    m.invoke(connPool, propValue);
                }else if (types[0].equals(int.class)){
                    // 参数为int类型
                    m = pool.getMethod(methodName, int.class);
                    m.invoke(connPool, Integer.parseInt(propValue));
                }else if (types[0].equals(long.class)){
                    // 参数为long类型
                    m = pool.getMethod(methodName, long.class);
                    m.invoke(connPool, Long.parseLong(propValue));
                }else if (types[0].equals(Boolean.class)){
                    // 参数为Boolean类型
                    m = pool.getMethod(methodName, Boolean.class);
                    m.invoke(connPool, TypeConverter.str2Boolean(propValue));
                }else if(types[0].equals(boolean.class)){
                    // 参数为boolean类型
                    m = pool.getMethod(methodName, boolean.class);
                    m.invoke(connPool, TypeConverter.str2Boolean(propValue));
                }
            }

        }
    }

    /**
     * 根据set方法获取属性名
     * @param method 反射方法
     * @return propName
     */
    private String getPropNameBySetMethod(Method method) {
        String methodName = method.getName();
        String propName = null;
        if (methodName.startsWith(SET)) {
            propName = methodName.substring(3,4).toLowerCase() +
                       methodName.substring(4);
        }
        return propName;
    }

}
