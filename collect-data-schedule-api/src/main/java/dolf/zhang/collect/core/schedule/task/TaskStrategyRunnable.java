package dolf.zhang.collect.core.schedule.task;

import dolf.zhang.collect.core.disruptor.RunDisruptor;

/**
 * 任务策略实际执行线程接口, 生成事件对象push到disruptor中
 *
 * @author pengming  
 * @date 2017年02月28日 上午10:47
 * @description
 */
public interface TaskStrategyRunnable extends Runnable {

    void setTaskStrategy(TaskStrategy taskStrategy);

    void setRunDisruptor(RunDisruptor runDisruptor);

    void setEventClass(Class eventClass);
}
