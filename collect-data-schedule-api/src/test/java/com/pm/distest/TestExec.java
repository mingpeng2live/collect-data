package com.pm.distest;

import cn.com.admaster.utilities.date.DateUtilities;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author pengming  
 * @date 2017年01月04日 下午5:52
 * @description
 */
public class TestExec {

    public static void main(String[] args) throws Exception {

//        ScheduledThreadPoolExecutor executors = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(2);
        ScheduledExecutorService executors = Executors.newScheduledThreadPool(2);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(DateUtilities.defaultFormatDate(new Date())  + "runn1" +  Thread.currentThread().getName());
            }
        };

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                System.out.println(DateUtilities.defaultFormatDate(new Date()) + " runn2 " + Thread.currentThread().getName());
            }
        };
        ScheduledFuture<?> scheduledFuture1 = executors.scheduleAtFixedRate(runnable, 0, 2, TimeUnit.SECONDS);

        Thread.sleep(6000);

        scheduledFuture1.cancel(true);

        Thread.sleep(1000);

        ScheduledFuture<?> scheduledFuture2 = executors.scheduleAtFixedRate(runnable2, 1, 3, TimeUnit.SECONDS);
        Thread.sleep(6000);
        executors.shutdown();

    }



}
