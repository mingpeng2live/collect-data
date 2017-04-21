package com.pm.distest.taskhandler;

import dolf.zhang.collect.core.disruptor.RunDisruptor;
import dolf.zhang.collect.core.handler.Step;
import dolf.zhang.collect.core.schedule.ScheduledManager;
import dolf.zhang.collect.core.schedule.task.TaskStrategy;
import dolf.zhang.collect.core.schedule.task.TaskStrategyManager;
import com.pm.distest.Job;
import com.pm.distest.taskhandler.handler.Test1EventHandler;
import com.pm.distest.taskhandler.handler.Test2EventHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author pengming  
 * @date 2017年03月02日 上午11:23
 * @description
 */
public class TestTaskStrategy {

    private TaskStrategyManager taskStrategyManager;


    @Before
    public void init(){

        Test1EventHandler test1EventHandler = new Test1EventHandler();
        test1EventHandler.setKey("11");
        Test2EventHandler test2EventHandler = new Test2EventHandler();
        test2EventHandler.setKey("11");
        Step step = new Step(test1EventHandler, test2EventHandler);

        RunDisruptor<Job> runDisruptor = new RunDisruptor<>(2048, step).init();
        ScheduledManager scheduledManager = new ScheduledManager(100);

        taskStrategyManager = new TaskStrategyManager();
        taskStrategyManager.setEventClass(Job.class);
        taskStrategyManager.setRunDisruptor(runDisruptor);
        taskStrategyManager.setScheduledManager(scheduledManager);
    }

    @Test
    public void testTaskStrategyAdd(){
        try {
            TaskStrategy taskStrategy = new TaskStrategy("a");
            taskStrategy.setCron("*/10 * * * * *");
            taskStrategy.setData("a");
            taskStrategyManager.add(taskStrategy);
            taskStrategy = new TaskStrategy("b");
            taskStrategy.setCron("*/2 * * * * *");
            taskStrategy.setData("b");
            taskStrategyManager.add(taskStrategy);
            Thread.sleep(60000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTaskStrategyRemove(){
        try {
            TaskStrategy taskStrategy = new TaskStrategy("a");
            taskStrategy.setCron("*/10 * * * * *");
            taskStrategy.setData("a");
            taskStrategyManager.add(taskStrategy);
            taskStrategy = new TaskStrategy("b");
            taskStrategy.setCron("*/2 * * * * *");
            taskStrategy.setData("b");
            taskStrategyManager.add(taskStrategy);
            Thread.sleep(30000);
            taskStrategyManager.remove("b");
            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @After
    public void stop(){
        taskStrategyManager.shutdown();
    }

}
