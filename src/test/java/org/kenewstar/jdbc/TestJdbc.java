package org.kenewstar.jdbc;

import org.kenewstar.jdbc.core.datasource.KenewstarDataSource;

import java.sql.*;

public class TestJdbc {

    public static void main(String[] args) throws ClassNotFoundException,SQLException{
        //设置KenewstarDataSource
        KenewstarDataSource dataSource = new KenewstarDataSource();
        dataSource.setKenewstarDataSource("jdbc.properties");
        //加载驱动
         Class.forName(dataSource.getDriverClassName());
        //获取连接
        Connection connection = DriverManager.getConnection(
                dataSource.getUrl(),
                dataSource.getUsername(),
                dataSource.getPassword());
        //预处理
        PreparedStatement ps = connection.prepareStatement("select * from user where id = 3");
            //执行查询
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
        }




    }

}
