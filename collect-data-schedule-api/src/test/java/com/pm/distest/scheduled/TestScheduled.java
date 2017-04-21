package com.pm.distest.scheduled;

import dolf.zhang.collect.core.schedule.ScheduledManager;
import dolf.zhang.collect.core.schedule.task.TaskStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pengming  
 * @date 2017年02月21日 下午2:09
 * @description
 */
public class TestScheduled {

    protected static Logger logger = LoggerFactory.getLogger(TestScheduled.class);

    public static void main(String[] args) throws Exception {
        ScheduledManager scheduledManager = new ScheduledManager(30);

        TaskStrategy  ts = new TaskStrategy("a", () -> {
            logger.info("a" + System.currentTimeMillis());
        });
        ts.setInitialDelay(2000);
        ts.setPeriod(2000);
        scheduledManager.add(ts);


        ts = new TaskStrategy("b", () -> {
            logger.info("b" + System.currentTimeMillis());
        });
        ts.setCron("*/5 * * * * *");
        scheduledManager.add(ts);


        Thread.sleep(10000);
        scheduledManager.remove("a");

        Thread.sleep(30000);
        scheduledManager.remove("b");



    }

}
