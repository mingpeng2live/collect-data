package dolf.zhang.collect.core.event;

import dolf.zhang.collect.core.schedule.task.TaskStrategy;

/**
 * The type Event.
 *
 * @param <T> the type parameter
 * @author pengming  
 * @date 2016年12月29日 15:33:40
 * @description
 */
public class Event<T> {

    /**
     * 用于disruptor push事件对象时转换用的字段,当事件处理完成设置为空,方便垃圾回收.
     */
    protected T jobEvent;

    /**
     * 策略中配置的数据 {@link TaskStrategy#data}
     */
    protected String taskStrategyData;

    /**
     * 处理事件的disruptor名字
     */
    protected String disruptorName;

    /**
     * Gets component name.
     *
     * @return the component name
     */
    public String getDisruptorName() {
        return disruptorName;
    }

    /**
     * Sets component name.
     *
     * @param disruptorName the component name
     */
    public void setDisruptorName(String disruptorName) {
        this.disruptorName = disruptorName;
    }

    /**
     * Gets job event.
     *
     * @return the job event
     */
    public T getJobEvent() {
        return jobEvent;
    }

    /**
     * Sets job event.
     *
     * @param jobEvent the job event
     */
    public void setJobEvent(T jobEvent) {
        this.jobEvent = jobEvent;
    }


    /**
     * Gets data.
     *
     * @return the data
     */
    public String getTaskStrategyData() {
        return taskStrategyData;
    }

    /**
     * Sets data.
     *
     * @param taskStrategyData the data
     */
    public void setTaskStrategyData(String taskStrategyData) {
        this.taskStrategyData = taskStrategyData;
    }
}
