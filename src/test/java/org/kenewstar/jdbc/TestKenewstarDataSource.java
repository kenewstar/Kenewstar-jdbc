package org.kenewstar.jdbc;

import org.kenewstar.jdbc.core.datasource.KenewstarDataSource;

public class TestKenewstarDataSource {

    public static void main(String[] args) {
        KenewstarDataSource dataSource = new KenewstarDataSource();
        //设置数据源
        dataSource.setKenewstarDataSource("jdbc.properties");

        //获取数据源
        KenewstarDataSource ds = dataSource.getKenewstarDataSource();

        System.out.println(ds.getDriverClassName());
        System.out.println(ds.getUsername());
        System.out.println(ds.getUrl());
        System.out.println(ds.getPassword());
    }
}
