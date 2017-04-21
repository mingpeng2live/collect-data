package com.pm.distest.disruptorwork;

import dolf.zhang.collect.core.event.Event;
import dolf.zhang.collect.core.util.ConstantProcess;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.pm.distest.Job;

import java.util.concurrent.Executors;

/**
 * @author pengming  
 * @date 2016年12月30日 下午3:06
 * @description
 */
public class TestWorkHandler {

    public static void main(String[] args) {

        EventTranslatorOneArg<Event<Job>, Job> translator = (atRing, sequence, event) -> {
            atRing.setJobEvent(event);
        };

        Disruptor<Event<Job>> disruptor = new Disruptor<>(Event::new, ConstantProcess.DISRUPTOR,
                Executors.defaultThreadFactory(), ProducerType.SINGLE, new BlockingWaitStrategy());


        Test1EventHandler testEventHandler = new Test1EventHandler();
        Test2EventHandler test2EventHandler = new Test2EventHandler();


        Test1WorkHandler test1WorkHandler = new Test1WorkHandler();
        Test2WorkHandler test2WorkHandler = new Test2WorkHandler();
        Test3WorkHandler test3WorkHandler = new Test3WorkHandler();
        Test4WorkHandler test4WorkHandler = new Test4WorkHandler();

//        EventHandlerGroup<Event<Job>> g1 = disruptor.handleEventsWithWorkerPool(test1WorkHandler, test2WorkHandler);
//        EventHandlerGroup<Event<Job>> g2 = disruptor.handleEventsWithWorkerPool(test3WorkHandler, test4WorkHandler);
//        disruptor.handleEventsWith(testEventHandler);
//        ;

        disruptor.handleEventsWithWorkerPool(test1WorkHandler, test2WorkHandler)
//                .addWorkNew(test3WorkHandler, test4WorkHandler)
                .addNew(testEventHandler)
                .then(test2EventHandler)
                .addWork(test3WorkHandler, test4WorkHandler);


        disruptor.start();

        for (int i = 0; i < 5; i++) {
            Job event = new Job();
            event.setJobId("job " + i);
            disruptor.publishEvent(translator, event);
        }


        disruptor.shutdown();

    }
}

