package com.kenewstar.test;

import org.kenewstar.jdbc.core.JdbcExecutor;
import org.kenewstar.jdbc.core.KenewstarJdbcExecutor;

/**
 * @author kenewstar
 * @version 1.0
 * @date 2020/9/28
 */
public class ClientThread extends Thread{

    private final JdbcExecutor jdbcExecutor;
    public ClientThread(JdbcExecutor jdbcExecutor){
        this.jdbcExecutor = jdbcExecutor;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        User user = new User(null, "kenewstar", 20);
        jdbcExecutor.insert(user);
    }

    public static void main(String[] args) {

        for (int i=0;i<10;i++){
            ClientThread thread = new ClientThread(new KenewstarJdbcExecutor());
            thread.start();
        }
    }
}
