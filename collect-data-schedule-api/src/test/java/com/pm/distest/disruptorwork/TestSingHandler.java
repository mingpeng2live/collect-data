package com.pm.distest.disruptorwork;

import dolf.zhang.collect.core.event.Event;
import dolf.zhang.collect.core.util.ConstantProcess;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.pm.distest.Job;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

/**
 * @author pengming  
 * @date 2016年12月30日 下午3:06
 * @description
 */
public class TestSingHandler {

    public static void main(String[] args) {

        EventTranslatorOneArg<Event<Job>, Job> translator = (atRing, sequence, event) -> {
            atRing.setJobEvent(event);
        };

        Disruptor<Event<Job>> disruptor = new Disruptor<>(Event::new, ConstantProcess.DISRUPTOR,
                Executors.defaultThreadFactory(), ProducerType.SINGLE, new BlockingWaitStrategy());


        Test1EventHandler testEventHandler = new Test1EventHandler();

        disruptor.handleEventsWith(testEventHandler);


        disruptor.start();


        CyclicBarrier db = new CyclicBarrier(3);

        new Thread(() -> {
            try {
                db.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 20; i++) {
                Job event = new Job();
                event.setJobId("job " + i);
                disruptor.publishEvent(translator, event);
            }
        }).start();
        new Thread(() -> {
            try {
                db.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 20; i < 30; i++) {
                Job event = new Job();
                event.setJobId("job " + i);
                disruptor.publishEvent(translator, event);
            }
        }).start();
        new Thread(() -> {
            try {
                db.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 30; i < 40; i++) {
                Job event = new Job();
                event.setJobId("job " + i);
                disruptor.publishEvent(translator, event);
            }
        }).start();




        disruptor.shutdown();

    }
}

