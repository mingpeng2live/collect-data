package dolf.zhang.collect.core.schedule;

import dolf.zhang.collect.core.schedule.task.TaskStrategy;
import dolf.zhang.collect.core.schedule.trigger.CronTrigger;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 周期任务管理执行器
 *
 * @author pengming  
 * @date 2017年01月17日 上午10:59
 * @description
 */
public class ScheduledManager {

    private final int poolSize;

    private Map<String, TaskStrategy> scheduleds = new ConcurrentHashMap<>();

    private ScheduledExecutorService executors;

    /**
     * Instantiates a new Scheduled manager.
     *
     * @param poolSize the pool size
     */
    public ScheduledManager(int poolSize){
        if (poolSize < 1) {
            poolSize = 50;  // 默认50线程
        }
        this.poolSize = poolSize;
        this.executors = Executors.newScheduledThreadPool(poolSize);
    }


    /**
     * Add boolean.
     *
     * @param scheduled the scheduled
     * @return the boolean
     */
    public boolean add(TaskStrategy scheduled){
        Asserts.notNull(scheduled, "scheduled variable");

        Runnable runnable = scheduled.getRunnable();
        Asserts.notNull(runnable, "runnable");

        remove(scheduled.getKey()); // 如果存在相同key值的任务就删除掉
        ScheduledFuture<?> schedule = null;
        if (StringUtils.isNotEmpty(scheduled.getCron())) {
            schedule = schedule(scheduled, new CronTrigger(scheduled.getCron()));
        } else if (scheduled.isFixedDelay() != null && !scheduled.isFixedDelay()) {
            schedule = scheduleAtFixedRate(runnable, scheduled.getInitialDelay(), scheduled.getPeriod(), TimeUnit.MILLISECONDS);
        } else if (scheduled.isFixedDelay() != null && scheduled.isFixedDelay()) {
            schedule = scheduleWithFixedDelay(runnable, scheduled.getInitialDelay(), scheduled.getPeriod(), TimeUnit.MILLISECONDS);
        } else {
            schedule = schedule(runnable, scheduled.getInitialDelay(), TimeUnit.MILLISECONDS);
        }
        scheduled.setSchedule(schedule);
        scheduleds.put(scheduled.getKey(), scheduled);
        return true;
    }

    /**
     * 删除一项任务  @param key the key
     *
     * @return the boolean
     */
    public boolean remove(String key){
        Asserts.notNull(key, "key");
        if (scheduleds.containsKey(key)) {
            scheduleds.get(key).getSchedule().cancel(true);
            scheduleds.remove(key);
            return true;
        }
        return false;
    }


    /**
     * Schedule scheduled future.
     *
     * @param command      the command
     * @param initialDelay the initial delay
     * @param unit         the unit
     * @return the scheduled future
     */
    public ScheduledFuture<?> schedule(Runnable command, long initialDelay, TimeUnit unit){
        return this.executors.schedule(command, initialDelay, unit);
    }

    /**
     * Schedule at fixed rate scheduled future.
     *
     * @param command      the command
     * @param initialDelay the initial delay
     * @param period       the period
     * @param unit         the unit
     * @return the scheduled future
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit){
        return this.executors.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    /**
     * Schedule with fixed delay scheduled future.
     *
     * @param command      the command
     * @param initialDelay the initial delay
     * @param delay        the delay
     * @param unit         the unit
     * @return the scheduled future
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.executors.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }


    /**
     * Schedule scheduled future.
     *
     * @param scheduled    the task
     * @param trigger the trigger
     * @return the scheduled future
     */
    public ScheduledFuture<?> schedule(TaskStrategy scheduled, CronTrigger trigger) {
        ReschedulingRunnable schedule = new ReschedulingRunnable(scheduled, trigger, this);
        schedule.schedule();
        return schedule;
    }

    /**
     * Schedule scheduled future.
     *
     * @param task      the task
     * @param startTime the start time
     * @return the scheduled future
     */
    public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
        long initialDelay = startTime.getTime() - System.currentTimeMillis();
        return schedule(task, initialDelay, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedule at fixed rate scheduled future.
     *
     * @param task      the task
     * @param startTime the start time
     * @param period    the period
     * @return the scheduled future
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
        long initialDelay = startTime.getTime() - System.currentTimeMillis();
        return scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedule at fixed rate scheduled future.
     *
     * @param task   the task
     * @param period the period
     * @return the scheduled future
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
        return scheduleAtFixedRate(task, 0, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedule with fixed delay scheduled future.
     *
     * @param task      the task
     * @param startTime the start time
     * @param delay     the delay
     * @return the scheduled future
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
        long initialDelay = startTime.getTime() - System.currentTimeMillis();
        return scheduleWithFixedDelay(task, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedule with fixed delay scheduled future.
     *
     * @param task  the task
     * @param delay the delay
     * @return the scheduled future
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
        return scheduleWithFixedDelay(task, 0, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Shutdown.
     *
     */
    public void shutdown(){
        executors.shutdown();
        while (!executors.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets pool size.
     *
     * @return the pool size
     */
    public int getPoolSize() {
        return poolSize;
    }

    public Map<String, TaskStrategy> getScheduleds() {
        return scheduleds;
    }

    public void setScheduleds(Map<String, TaskStrategy> scheduleds) {
        this.scheduleds = scheduleds;
    }
}

