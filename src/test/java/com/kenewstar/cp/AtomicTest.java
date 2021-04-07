package com.kenewstar.cp;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/5
 */
public class AtomicTest {
    public AtomicInteger i = new AtomicInteger(0);
    public int i2 = 0;
    @Test
    public void test1() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 20000; j++) {
                i.incrementAndGet();
                i2++;
            }
        });
        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 20000; j++) {
                i.decrementAndGet();
                i2++;
            }
        });
        Thread t3 = new Thread(() -> {
            for (int j = 0; j < 20000; j++) {
                i.incrementAndGet();
                i2++;
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();

        System.out.println(i.get());
        System.out.println(i2);
    }
}
