package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.core.datasource.ConnectionPool;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 *
 * 处理sql语句
 * @author kenewstar
 * @date 2020-08-08
 * @version 1.0
 */
public class KenewstarStatement {

    /**
     * 获取数据库连接池
     */
    private final DataSource dataSource;
    /**
     * 使用本地线程声明连接
     */
    private final ThreadLocal<Connection> connection;
    /**
     * 执行数量
     */
    private static final int EXECUTOR_COUNT = 100;

    public KenewstarStatement() {
        dataSource = new ConnectionPool().getConnPool();
        connection = new ThreadLocal<>();
    }

    public KenewstarStatement(String configPath) {
        dataSource = new ConnectionPool(configPath).getConnPool();
        connection = new ThreadLocal<>();
    }


    /**
     * 从数据库连接池获取连接
     * @return conn
     */
    public Connection getConnection(){
        Connection conn = connection.get();
        try {
            if (conn == null){
                conn = dataSource.getConnection();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            connection.set(conn);
        }
        return conn;
    }
    /**
     * 增删改执行器
     * @param sql Sql语句
     * @param params 可变参数
     * @return 返回执行影响的行数
     */
    public int preparedUpdateExecutor(String sql, Object ...params) {
        PreparedStatement ps = null;
        // 获取一个连接
        Connection conn = getConnection();
        int flag = 0;
        try {
            //执行sql预处理
            ps =  conn.prepareStatement(sql);
            //传递参数
            int index = 0;
            for (Object param : params) {
                ps.setObject(++ index, param);
            }
            //执行sql
            flag = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, ps);
        }
        return flag;
    }

    /**
     * 批量Sql执行器
     * @param sql sql语句
     * @param paramList 批量参数
     * @return rows
     */
    public int preparedBatchExecutor(String sql, List<List<Object>> paramList) {
        PreparedStatement ps = null;
        // 获取一个连接
        Connection conn = getConnection();
        int count = paramList.size();
        try {
            // 预处理
            ps = conn.prepareStatement(sql);
            // 批量设置参数
            for (int i = 1; i <= count; i++) {
                // 获取参数
                List<Object> params = paramList.get(i - 1);
                for (int j = 1; j <= params.size(); j++) {
                    ps.setObject(j, params.get(j - 1));
                }
                ps.addBatch();
                if (i % EXECUTOR_COUNT == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            // 执行剩余
            if (count % EXECUTOR_COUNT != 0) {
                ps.executeBatch();
                ps.clearBatch();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(null, ps);
        }
        return count;
    }

    /**
     * 查询执行器
     * @param sql SQL查询语句
     * @param params 查询条件参数
     * @return 返回查询结果集
     */
    public List<Map<String,Object>> preparedSelectExecutor(String sql, Object ...params) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        // 获取一个连接
        Connection conn = getConnection();
        // 创建一个List<Map>对象封装返回结果
        List<Map<String,Object>> result = new ArrayList<>(0);
        try {
            // 执行SQL预处理
            ps = conn.prepareStatement(sql);
            //传递参数
            int index = 0;
            for (Object param : params){
                ps.setObject(++ index, param);
            }
            // 执行查询
            rs = ps.executeQuery();
            // 取出结果集
            // 实例化返回结果对象
            result = new ArrayList<>(rs.getRow());
            Map<String,Object> map;
            // 遍历结果集，将结果集封装到List中
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                map = new HashMap<>(columnCount);
                // 获取结果集对象元数据
                ResultSetMetaData metaData = rs.getMetaData();
                // 遍历每一行的属性
                for (int i = 1;i <= columnCount; i++){
                    String columnName = metaData.getColumnLabel(i);
                    map.put(columnName, rs.getObject(columnName));
                }
                // 将map存入List中
                result.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            close(rs, ps);
        }
        return result;
    }

    /**
     * 关闭资源
     * @param rs resultSet
     * @param ps preparedStatement
     */
    public void close(ResultSet rs, PreparedStatement ps) {
        Connection conn = connection.get();
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                // 事物是否自动提交
                if (conn.getAutoCommit()) {
                    conn.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connection.remove();
        }

    }

}
