package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.core.datasource.ConnectionPool;
import org.kenewstar.jdbc.core.datasource.KenewstarDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 处理sql语句
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class KenewstarStatement {
    /**
     * 声明连接
     */
    private Connection connection = null;
    /**
     * 声明预处理对象
     */
    private PreparedStatement ps = null;
    /**
     * 声明返回结果集对象
     */
    private ResultSet rs = null;
    /**
     * 获取数据库连接池
     */
    private static final DataSource DATASOURCE =
            new ConnectionPool().getConnPool();

    /**
     * 从数据库连接池获取连接
     */
    private Connection getConnection(){
        try {
            connection = DATASOURCE.getConnection();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }
    /**
     * 增删改执行器
     * @param sql Sql语句
     * @param params 可变参数
     * @return 返回执行影响的行数
     */
    public int preparedUpdateExecutor(String sql,Object...params){
        // 获取一个连接
        connection = getConnection();
        int flag = 0;
        try {
            //执行sql预处理
            ps =  connection.prepareStatement(sql);
            //传递参数
            int index = 0;
            for (Object param : params){
                ps.setObject(++index,param);
            }
            //执行sql
            flag = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(ps,connection);
        }
        return flag;
    }

    /**
     * 查询执行器
     * @param sql SQL查询语句
     * @param params 查询条件参数
     * @return 返回查询结果集
     */
    public List<Map<String,Object>> preparedSelectExecutor(String sql, Object...params){
        // 获取一个连接
        connection = getConnection();
        // 创建一个List<Map>对象封装返回结果
        List<Map<String,Object>> result = null;
        try {
            // 执行SQL预处理
            ps = connection.prepareStatement(sql);
            //传递参数
            int index = 0;
            for (Object param : params){
                ps.setObject(++index,param);
            }
            // 执行查询
            rs = ps.executeQuery();
            // 取出结果集
            index = 1;
            // 实例化返回结果对象
            result = new ArrayList<Map<String, Object>>(rs.getRow());
            Map<String,Object> map = null;
            // 遍历结果集，将结果集封装到List中
            while (rs.next()){
                map = new HashMap<>();
                // 获取结果集对象元数据
                ResultSetMetaData metaData = rs.getMetaData();
                // 遍历每一行的属性
                for (int i=1;i<=metaData.getColumnCount();i++){
                    String columnName = metaData.getColumnLabel(i);
                    map.put(columnName,rs.getObject(columnName));
                }
                // 将map存入List中
                result.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            close(ps,connection,rs);
        }
        return result;
    }

    /**
     * 关闭资源
     * @param ps
     * @param conn
     */
    public void close(PreparedStatement ps,Connection conn){
        close(ps,conn,null);
    }
    /**
     * 关闭资源
     * @param ps
     * @param conn
     * @param rs
     */
    public void close(PreparedStatement ps,Connection conn,ResultSet rs){
        try {
            if ( ps != null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs != null){
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
