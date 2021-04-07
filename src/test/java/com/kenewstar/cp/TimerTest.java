package com.kenewstar.cp;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/4
 */
public class TimerTest {

    static class ClearConnection implements Runnable {

        @Override
        public void run() {
            System.out.println(new Date());
        }
    }
    @Test
    public void test1() {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate(new ClearConnection(),
                2,3,TimeUnit.SECONDS);

    }

    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate(new ClearConnection(),
                2,3,TimeUnit.SECONDS);
        System.out.println(12);
    }
}
