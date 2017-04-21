package com.pm.distest.taskhandler.handler;

import dolf.zhang.collect.core.event.Event;
import dolf.zhang.collect.core.handler.MappingHandler;
import com.pm.distest.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pengming  
 * @date 2017年03月03日 上午10:46
 * @description
 */
public class TestMappingHandler211 implements MappingHandler<Job> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String key;

    private String type;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean supportType(String types) {
        return types != null && types.equals(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void onEvent(Event<Job> event, Job data) throws Exception {
        logger.info(" 211 " + event.getTaskStrategyData() + "\t" + data.getJobId());
    }

}
