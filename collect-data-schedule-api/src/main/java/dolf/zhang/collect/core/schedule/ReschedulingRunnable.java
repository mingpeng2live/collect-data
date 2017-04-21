package dolf.zhang.collect.core.schedule;


import dolf.zhang.collect.core.schedule.task.TaskStrategy;
import dolf.zhang.collect.core.schedule.trigger.CronTrigger;
import dolf.zhang.collect.core.schedule.trigger.SimpleTriggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.concurrent.*;


/**
 * 表达式周期任务
 *
 * @author pengming  
 * @date 2017年01月17日 上午10:59
 * @description
 */
public class ReschedulingRunnable implements Runnable, ScheduledFuture<Object> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    private final TaskStrategy scheduled;


    private CronTrigger trigger;

    private SimpleTriggerContext triggerContext = new SimpleTriggerContext();

    private final ScheduledManager executor;

    private ScheduledFuture<?> currentFuture;

    private Date scheduledExecutionTime;

    private final Object triggerContextMonitor = new Object();

    public ReschedulingRunnable(TaskStrategy scheduled, CronTrigger trigger, ScheduledManager executor) {
        Assert.notNull(scheduled, "scheduled must not be null");
        this.trigger = trigger;
        this.scheduled = scheduled;
        this.executor = executor;
    }

    public ScheduledFuture<?> schedule() {
        synchronized (this.triggerContextMonitor) {
            this.scheduledExecutionTime = this.trigger.nextExecutionTime(this.triggerContext);
            if (this.scheduledExecutionTime == null) {
                return null;
            }
            long initialDelay = this.scheduledExecutionTime.getTime() - System.currentTimeMillis();
            this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
            scheduled.setSchedule(this.currentFuture);
            return this;
        }
    }

    public void run() {
        Date actualExecutionTime = new Date();
        try {
            this.scheduled.getRunnable().run();
        } catch (Throwable ex) {
            logger.error("exec error", ex);
        }
        Date completionTime = new Date();
        synchronized (this.triggerContextMonitor) {
            this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
            if (!this.currentFuture.isCancelled()) {
                schedule();
            }
        }
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        logger.info("cancel scheduled key " + scheduled.getKey());
        synchronized (this.triggerContextMonitor) {
            return this.currentFuture.cancel(mayInterruptIfRunning);
        }
    }

    @Override
    public boolean isCancelled() {
        synchronized (this.triggerContextMonitor) {
            return this.currentFuture.isCancelled();
        }
    }

    @Override
    public boolean isDone() {
        synchronized (this.triggerContextMonitor) {
            return this.currentFuture.isDone();
        }
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        ScheduledFuture<?> curr;
        synchronized (this.triggerContextMonitor) {
            curr = this.currentFuture;
        }
        return curr.get();
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        ScheduledFuture<?> curr;
        synchronized (this.triggerContextMonitor) {
            curr = this.currentFuture;
        }
        return curr.get(timeout, unit);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        ScheduledFuture<?> curr;
        synchronized (this.triggerContextMonitor) {
            curr = this.currentFuture;
        }
        return curr.getDelay(unit);
    }

    @Override
    public int compareTo(Delayed other) {
        if (this == other) {
            return 0;
        }
        long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
        return (diff == 0 ? 0 : ((diff < 0) ? -1 : 1));
    }

    @Override
    public String toString() {
        return "ReschedulingRunnable for " + this.scheduled.getRunnable();
    }

}
