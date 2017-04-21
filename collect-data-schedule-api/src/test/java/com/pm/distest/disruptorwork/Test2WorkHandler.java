package com.pm.distest.disruptorwork;

import dolf.zhang.collect.core.event.Event;
import com.lmax.disruptor.WorkHandler;
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
public class Test2WorkHandler implements WorkHandler<Event<Job>> {

    /**
     * The Logger.
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onEvent(Event<Job> event) throws Exception {
        logger.info("work 2 jobId: " + event.getJobEvent().getJobId());
    }
}
