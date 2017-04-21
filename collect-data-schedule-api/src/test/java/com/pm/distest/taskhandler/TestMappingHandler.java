package com.pm.distest.taskhandler;

import dolf.zhang.collect.core.disruptor.RunDisruptor;
import dolf.zhang.collect.core.handler.MappingHandler;
import dolf.zhang.collect.core.handler.Step;
import dolf.zhang.collect.core.schedule.ScheduledManager;
import dolf.zhang.collect.core.schedule.task.TaskStrategy;
import dolf.zhang.collect.core.schedule.task.TaskStrategyManager;
import com.pm.distest.Job;
import com.pm.distest.taskhandler.handler.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pengming  
 * @date 2017年03月03日 上午11:27
 * @description
 */
public class TestMappingHandler {


    /** 三种事件类型,两步处理,每步分为互不相干的两块任务 */

    public static void main(String[] args) throws Exception {
        TestMappingHandler111 test111 = new TestMappingHandler111();
        test111.setKey("11");
        test111.setType("a");
        TestMappingHandler112 test112 = new TestMappingHandler112();
        test112.setKey("11");
        test112.setType("b");
        TestMappingHandler113 test113 = new TestMappingHandler113();
        test113.setKey("11");
        test113.setType("c");

        TestMappingHandler121 test121 = new TestMappingHandler121();
        test121.setKey("12");
        test121.setType("a");
        TestMappingHandler122 test122 = new TestMappingHandler122();
        test122.setKey("12");
        test122.setType("b");
        TestMappingHandler123 test123 = new TestMappingHandler123();
        test123.setKey("12");
        test123.setType("c");

        Map<String, Integer> mappingCount = new HashMap<>();
        mappingCount.put("11", 1);
        mappingCount.put("12", 2);

        List<MappingHandler> mappingHandlers = new ArrayList<>();
        mappingHandlers.add(test111);
        mappingHandlers.add(test112);
        mappingHandlers.add(test113);
        mappingHandlers.add(test121);
        mappingHandlers.add(test122);
        mappingHandlers.add(test123);

        Step step1 = new Step(mappingCount, mappingHandlers);



        TestMappingHandler211 test211 = new TestMappingHandler211();
        test211.setKey("21");
        test211.setType("a");
        TestMappingHandler212 test212 = new TestMappingHandler212();
        test212.setKey("21");
        test212.setType("b");
        TestMappingHandler213 test213 = new TestMappingHandler213();
        test213.setKey("21");
        test213.setType("c");

        TestMappingHandler221 test221 = new TestMappingHandler221();
        test221.setKey("22");
        test221.setType("a");
        TestMappingHandler222 test222 = new TestMappingHandler222();
        test222.setKey("22");
        test222.setType("b");
        TestMappingHandler223 test223 = new TestMappingHandler223();
        test223.setKey("22");
        test223.setType("c");


        mappingCount = new HashMap<>();
        mappingCount.put("21", 2);
        mappingCount.put("22", 2);

        mappingHandlers = new ArrayList<>();
        mappingHandlers.add(test211);
        mappingHandlers.add(test212);
        mappingHandlers.add(test213);
        mappingHandlers.add(test221);
        mappingHandlers.add(test222);
        mappingHandlers.add(test223);


        Step step2 = new Step(mappingCount, mappingHandlers);


        RunDisruptor<Job> runDisruptor = new RunDisruptor<>(2048, step1, step2).init();
        ScheduledManager scheduledManager = new ScheduledManager(100);

        TaskStrategyManager taskStrategyManager = new TaskStrategyManager();
        taskStrategyManager.setEventClass(Job.class);
        taskStrategyManager.setRunDisruptor(runDisruptor);
        taskStrategyManager.setScheduledManager(scheduledManager);

        TaskStrategy taskStrategy = new TaskStrategy("1namekey");
        taskStrategy.setCron("*/10 * * * * *");
        taskStrategy.setData("a");
        taskStrategyManager.add(taskStrategy);
        taskStrategy = new TaskStrategy("2namekey");
        taskStrategy.setCron("*/3 * * * * *");
        taskStrategy.setData("b");
        taskStrategyManager.add(taskStrategy);
        taskStrategy = new TaskStrategy("3namekey");
        taskStrategy.setCron("8,15,33 * * * * *");
        taskStrategy.setData("c");
        taskStrategyManager.add(taskStrategy);


    }

}
