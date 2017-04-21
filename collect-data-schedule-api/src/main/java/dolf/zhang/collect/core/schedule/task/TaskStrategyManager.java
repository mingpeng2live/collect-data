package dolf.zhang.collect.core.schedule.task;

import dolf.zhang.collect.core.disruptor.RunDisruptor;
import dolf.zhang.collect.core.schedule.ScheduledManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务策略管理器,集成任务策略与disruptor框架
 *
 * @author pengming  
 * @date 2017年03月01日 下午5:22
 * @description
 */
public class TaskStrategyManager {

    private ScheduledManager scheduledManager;

    private Class<? extends TaskStrategyRunnable> taskStrategyRunnable;

    private RunDisruptor runDisruptor;

    private Class eventClass;

    private Logger logger = LoggerFactory.getLogger(getClass());


    public boolean add(TaskStrategy taskStrategy) throws Exception {
        if (this.taskStrategyRunnable == null)
            this.taskStrategyRunnable = DefaultTaskStrategyRunnable.class;
        TaskStrategyRunnable taskStrategyRunnable = this.taskStrategyRunnable.newInstance();
        taskStrategyRunnable.setTaskStrategy(taskStrategy);
        taskStrategyRunnable.setRunDisruptor(runDisruptor);
        taskStrategyRunnable.setEventClass(eventClass);
        taskStrategy.setRunnable(taskStrategyRunnable); // 设置当前任务策略具体处理任务的线程
        return scheduledManager.add(taskStrategy);
    }

    public boolean remove(String key) {
        return scheduledManager.remove(key);
    }

    public Class<? extends TaskStrategyRunnable> getTaskStrategyRunnable() {
        return taskStrategyRunnable;
    }

    public void setTaskStrategyRunnable(Class<? extends TaskStrategyRunnable> taskStrategyRunnable) {
        this.taskStrategyRunnable = taskStrategyRunnable;
    }

    public ScheduledManager getScheduledManager() {
        return scheduledManager;
    }

    public void setScheduledManager(ScheduledManager scheduledManager) {
        this.scheduledManager = scheduledManager;
    }

    public RunDisruptor getRunDisruptor() {
        return runDisruptor;
    }

    public void setRunDisruptor(RunDisruptor runDisruptor) {
        this.runDisruptor = runDisruptor;
    }

    public Class getEventClass() {
        return eventClass;
    }

    public void setEventClass(Class eventClass) {
        this.eventClass = eventClass;
    }

    public void shutdown(){
        if (scheduledManager != null)
            scheduledManager.shutdown();
        if (runDisruptor != null)
            runDisruptor.shutdown();
    }

}
