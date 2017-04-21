package dolf.zhang.collect.core.schedule.task;

import dolf.zhang.collect.core.disruptor.RunDisruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的任务策略实际执行线程,生成事件push到disruptor中
 *
 * @author pengming  
 * @date 2017年02月28日 上午10:56
 * @description
 */
public class DefaultTaskStrategyRunnable<T> implements TaskStrategyRunnable {

    private TaskStrategy taskStrategy;

    private RunDisruptor<T> runDisruptor;

    private Class<T> eventClass;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void setTaskStrategy(TaskStrategy taskStrategy) {
        this.taskStrategy = taskStrategy;
    }

    @Override
    public void run() {
        try {
            T t = eventClass.newInstance();
            runDisruptor.publishEvent(t, taskStrategy.getData());
        } catch (Exception e) {
            logger.error("publish event error", e);
        }
    }


    public TaskStrategy getTaskStrategy() {
        return taskStrategy;
    }

    public RunDisruptor<T> getRunDisruptor() {
        return runDisruptor;
    }

    public void setRunDisruptor(RunDisruptor runDisruptor) {
        this.runDisruptor = runDisruptor;
    }

    public Class<T> getEventClass() {
        return eventClass;
    }

    public void setEventClass(Class eventClass) {
        this.eventClass = eventClass;
    }
}
