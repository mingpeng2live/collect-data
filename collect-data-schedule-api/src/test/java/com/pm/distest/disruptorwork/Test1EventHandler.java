package com.pm.distest.disruptorwork;

import dolf.zhang.collect.core.event.Event;
import com.lmax.disruptor.EventHandler;
import com.pm.distest.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 清理队列中事件对象存在的数据,方便垃圾回收,与
 * {@link Event} 中属性jobEvent 合用
 * @author pengming  
 * @date 2016年12月29日 下午3:27
 * @description
 */
public class Test1EventHandler implements EventHandler<Event<Job>> {


    /**
     * The Logger.
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void onEvent(Event<Job> ringEvent, long sequence, boolean endOfBatch) throws Exception {
        logger.info("event 1 seq " + sequence + " jobId: " + ringEvent.getJobEvent().getJobId());
    }
}
