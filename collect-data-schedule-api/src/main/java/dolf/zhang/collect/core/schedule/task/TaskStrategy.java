package dolf.zhang.collect.core.schedule.task;

import org.apache.http.util.Asserts;

import java.util.concurrent.ScheduledFuture;

/**
 * 任务策略对象.
 * initialDelay, period 时间单位为毫秒
 *
 * @author pengming  
 * @date 2017年02月17日 下午2:24
 * @description
 */
public class TaskStrategy {

    private String key;  // 任务唯一值key

    private Boolean fixedDelay; // false: 周期时间任务, true: 延迟周期时间任务[即等待上次执行完成后延迟的周期时间]
    private int initialDelay;  // 第一次执行延迟时间, 单位毫秒
    private int period;  // 表示间隔周期, 单位毫秒

    private String cron; // 运行表达式 与其他设置不同用

    private String data; // 数据, 用户自定义该策略的数据,比如该策略的类型,用于构建什么事件类型;

    private Runnable runnable; // 需要执行的线程代码

    private ScheduledFuture<?> schedule;

    public TaskStrategy(String key){
        this.key = key;
    }

    /**
     * Instantiates a new Task strategy.
     *
     * @param key      the key
     * @param runnable the runnable
     */
    public TaskStrategy(String key, Runnable runnable) {
        Asserts.notNull(key, "key");
        Asserts.notNull(runnable, "runnable");
        this.key = key;
        this.runnable = runnable;
    }

    /**
     * 周期表达式任务, 如: <ul><li>8 * * * * *</li></ul>  @param key the key
     *
     * @param runnable the runnable
     * @param cron     the cron
     */
    public TaskStrategy(String key, Runnable runnable, String cron) {
        Asserts.notNull(key, "key");
        Asserts.notNull(runnable, "runnable");
        this.key = key;
        this.runnable = runnable;
        this.cron = cron;
    }

    /**
     * 一次延时任务  @param key the key
     *
     * @param runnable     the runnable
     * @param initialDelay the initial delay
     */
    public TaskStrategy(String key, Runnable runnable, int initialDelay) {
        Asserts.notNull(key, "key");
        Asserts.notNull(runnable, "runnable");
        Asserts.check(initialDelay >= 0, "initialDelay less than zero");

        this.key = key;
        this.runnable = runnable;
        this.initialDelay = initialDelay;
    }

    /**
     * 周期时间任务  @param key the key
     *
     * @param runnable     the runnable
     * @param initialDelay the initial delay
     * @param period       the period
     */
    public TaskStrategy(String key, Runnable runnable, int initialDelay, int period) {
        Asserts.notNull(key, "key");
        Asserts.notNull(runnable, "runnable");
        Asserts.check(period > 0, "period less than zero");
        Asserts.check(initialDelay >= 0, "initialDelay less than zero");

        this.key = key;
        this.runnable = runnable;
        this.initialDelay = initialDelay;
        this.period = period;
        this.fixedDelay = false;
    }

    /**
     * 通过fixedDelay 来控制,默认:false 表示周期时间任务, true: 表示延时周期时间任务  @param key the key
     *
     * @param runnable     the runnable
     * @param initialDelay the initial delay
     * @param period       the period
     * @param fixedDelay   the fixed delay
     */
    public TaskStrategy(String key, Runnable runnable, int initialDelay, int period, Boolean fixedDelay) {
        Asserts.notNull(key, "key");
        Asserts.notNull(runnable, "runnable");
        Asserts.check(period > 0, "period less than zero");
        Asserts.check(initialDelay >= 0, "initialDelay less than zero");

        this.key = key;
        this.runnable = runnable;
        this.initialDelay = initialDelay;
        this.period = period;
        this.fixedDelay = fixedDelay;
    }

    /**
     * Gets cron.
     *
     * @return the cron
     */
    public String getCron() {
        return cron;
    }

    /**
     * Sets cron.
     *
     * @param cron the cron
     */
    public void setCron(String cron) {
        this.cron = cron;
    }

    /**
     * Gets initial delay.
     *
     * @return the initial delay
     */
    public int getInitialDelay() {
        return initialDelay;
    }

    /**
     * Sets initial delay.
     *
     * @param initialDelay the initial delay
     */
    public TaskStrategy setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
        return this;
    }

    /**
     * Gets json.
     *
     * @return the json
     */
    public String getData() {
        return data;
    }

    /**
     * Sets json.
     *
     * @param data the json
     */
    public TaskStrategy setData(String data) {
        this.data = data;
        return this;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public TaskStrategy setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Gets period.
     *
     * @return the period
     */
    public int getPeriod() {
        return period;
    }

    /**
     * Sets period.
     *
     * @param period the period
     */
    public TaskStrategy setPeriod(int period) {
        this.period = period;
        return this;
    }

    /**
     * Is fixed rate boolean.
     *
     * @return the boolean
     * @author pengming
     */
    public Boolean isFixedDelay() {
        return fixedDelay;
    }

    /**
     * Sets fixed rate.
     *
     * @param fixedDelay the fixed rate
     */
    public TaskStrategy setFixedDelay(boolean fixedDelay) {
        this.fixedDelay = fixedDelay;
        return this;
    }

    /**
     * Gets runnable.
     *
     * @return the runnable
     */
    public Runnable getRunnable() {
        return runnable;
    }

    /**
     * Sets runnable.
     *
     * @param runnable the runnable
     */
    public TaskStrategy setRunnable(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }


    /**
     * Gets schedule.
     *
     * @return the schedule
     */
    public ScheduledFuture<?> getSchedule() {
        return schedule;
    }

    /**
     * Sets schedule.
     *
     * @param schedule the schedule
     */
    public TaskStrategy setSchedule(ScheduledFuture<?> schedule) {
        this.schedule = schedule;
        return this;
    }
}
