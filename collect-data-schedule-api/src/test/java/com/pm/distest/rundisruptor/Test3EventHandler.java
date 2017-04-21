package com.pm.distest.rundisruptor;

import dolf.zhang.collect.core.event.Event;
import dolf.zhang.collect.core.handler.WorkEventHandler;
import com.pm.distest.Job;

/**
 * 清理队列中事件对象存在的数据,方便垃圾回收,与
 * {@link Event} 中属性jobEvent 合用
 * @author pengming  
 * @date 2016年12月29日 下午3:27
 * @description
 */
public class Test3EventHandler implements WorkEventHandler<Job> {

    private String groupKey;

    @Override
    public String getKey() {
        return groupKey;
    }

    public void setKey(String groupKey) {
        this.groupKey = groupKey;
    }

    @Override
    public void onEvent(Event<Job> event, Job job) throws Exception {
//        LockSupport.parkNanos(RandomUtilities.getNum(0, 20));
        logger.info(" 3 name jobId: " + job.getJobId());
    }

}


