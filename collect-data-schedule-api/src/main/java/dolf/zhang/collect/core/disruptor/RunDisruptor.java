package dolf.zhang.collect.core.disruptor;

import dolf.zhang.collect.core.event.Event;
import dolf.zhang.collect.core.handler.*;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;


/**
 * disruptor组件.
 *
 * @author pengming  
 * @date 2016年12月29日 16:14:37
 * @description
 */
public class RunDisruptor<T> {

    /**
     * The Logger.
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String disruptorName; // 当前组件名称

    private int bufferSize = 1024; // RingBuffer size 默认1024
    private ProducerType producerType = ProducerType.MULTI; // 默认生产者模式,多生产者模式,但性能比单生产者慢
    private ThreadFactory threadFactory = Executors.defaultThreadFactory(); // 默认的线程工程创建的线程对象
    private WaitStrategy waitStrategy = new YieldingWaitStrategy(); // 默认的等待策略
    /**
     * 事件对象工厂,在初始化RingBuffer时会直接根据工厂产生对象填充好整个RingBuffer
     */
    private EventFactory<Event> eventFactory =  () -> {
        Event event = new Event();
        event.setDisruptorName(disruptorName);
        return event;
    };


    /** RingBuffer的 dsl方式 */
    private Disruptor<Event> disruptor;

    /** 统计当前ringbuffer中还有多少没处理的事件 */
    private AtomicLong count = new AtomicLong(0l);

    /**
     * 事件转换器,由外部生成的事件对象,转换成RingBuffer中已经生成的对象
     */
    private EventTranslatorOneArg<Event, T> translator = (atRing, sequence, event) -> {
        atRing.setJobEvent(event);
        count.incrementAndGet();
    };

    private EventTranslatorTwoArg<Event, T, String> translatorTwoArg = (atRing, sequence, event, data) -> {
        atRing.setJobEvent(event);
        atRing.setTaskStrategyData(data);
        count.incrementAndGet();
    };

    /**
     * 向RingBuffer 中push 事件对象.
     *
     * @param jobEvent the jobEvent
     * @author pengming
     * @date 2016年12月29日 15:51:11
     */
    public void publishEvent(T jobEvent) {
        disruptor.publishEvent(translator, jobEvent);
    }

    public void publishEvent(T jobEvent, String data){
        disruptor.getRingBuffer().publishEvent(translatorTwoArg, jobEvent, data);
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public AtomicLong getCount() {
        return count;
    }

    /** 获取未完成的事件数 */
    public long getRemainingNumber() {
        return this.count.get();
    }


    public RunDisruptor(){
    }

    public RunDisruptor(int bufferSize){
        this.bufferSize = bufferSize;
    }

    public RunDisruptor(int bufferSize, List<Step> steps){
        this.bufferSize = bufferSize;
        setSteps(steps);
    }

    public RunDisruptor(int bufferSize, Step... steps){
        this.bufferSize = bufferSize;
        setSteps(steps);
    }

    /**
     * 所有handler
     */
    private List<Step> steps = Collections.EMPTY_LIST;

    public RunDisruptor init() {
        Asserts.notNull(steps, "steps");
        EventHandlerGroup<Event> group = null;
        disruptor = new Disruptor<>(eventFactory, bufferSize, threadFactory, producerType, waitStrategy);

        for (Step step : steps) {
            checkStepNotNull(step);

            List<WorkEventHandler<T>> handlers = new ArrayList<>();
            Map<String, List<WorkEventHandler<T>>> workStep = new HashMap<>();
            Map<String, List<MappingsHandler<T>>> mappingsStep = new HashMap<>();

            getHandlers(step, handlers, workStep, mappingsStep);

            if (group == null) {
                if (CollectionUtils.isNotEmpty(handlers)) {
                    group = disruptor.handleEventsWith(handlers.toArray(new WorkEventHandler[0]));
                }
                if (MapUtils.isNotEmpty(workStep)) {
                    for (List<WorkEventHandler<T>> workHandlers : workStep.values()) {
                        WorkEventHandler[] weh = workHandlers.toArray(new WorkEventHandler[0]);
                        group = group == null ? disruptor.handleEventsWithWorkerPool(weh) : group.addWorkNew(weh);
                    }
                }
                if (MapUtils.isNotEmpty(mappingsStep)) {
                    for (List<MappingsHandler<T>> mappingsHandlers : mappingsStep.values()) {
                        MappingsHandler[] weh = mappingsHandlers.toArray(new MappingsHandler[0]);
                        group = group == null ? disruptor.handleEventsWithWorkerPool(weh) : group.addWorkNew(weh);
                    }
                }
            } else {
                EventHandlerGroup<Event> groupStep = null;
                if (CollectionUtils.isNotEmpty(handlers)) {
                    groupStep = group.then(handlers.toArray(new WorkEventHandler[0]));
                }
                if (MapUtils.isNotEmpty(workStep)) {
                    for (List<WorkEventHandler<T>> workHandlers : workStep.values()) {
                        WorkEventHandler[] weh = workHandlers.toArray(new WorkEventHandler[0]);
                        groupStep = groupStep != null ? groupStep.addWork(weh) : group.thenHandleEventsWithWorkerPool(weh);
                    }
                }
                if (MapUtils.isNotEmpty(mappingsStep)) {
                    for (List<MappingsHandler<T>> mappingsHandlers : mappingsStep.values()) {
                        MappingsHandler[] weh = mappingsHandlers.toArray(new MappingsHandler[0]);
                        groupStep = groupStep != null ? groupStep.addWork(weh) : group.thenHandleEventsWithWorkerPool(weh);
                    }
                }
                group = groupStep;
            }
        }

        /** 清理数据及计数 */
        CounterHandler counterHandler = new CounterHandler();
        counterHandler.setRunDisruptor(this);
        group.then(counterHandler);

        disruptor.start();
        logger.info("disruptor: {} start", disruptorName);
        return this;
    }

    private void checkStepNotNull(Step step) {
        if (CollectionUtils.isEmpty(step.getHandlers()) &&
                (MapUtils.isEmpty(step.getMappingCount()) &&
                        CollectionUtils.isEmpty(step.getMappingHandlers()))) {
            Asserts.notNull(step, "step");
        }
    }

    private void getHandlers(Step step, List<WorkEventHandler<T>> handlers,
                             Map<String, List<WorkEventHandler<T>>> workStep,
                             Map<String, List<MappingsHandler<T>>> mappingsStep) {
        if (CollectionUtils.isNotEmpty(step.getHandlers())) {
            step.getHandlers().forEach(handler -> {
                String groupKey = handler.getKey();
                if (StringUtils.isEmpty(groupKey)) {
                    handlers.add(handler);
                } else {
                    if (!workStep.containsKey(groupKey)) {
                        workStep.put(groupKey, new ArrayList<>());
                    }
                    List<WorkEventHandler<T>> workEventHandlers = workStep.get(groupKey);
                    workEventHandlers.add(handler);
                }
            });
        }
        if (MapUtils.isNotEmpty(step.getMappingCount()) && CollectionUtils.isNotEmpty(step.getMappingHandlers())) {

            Map<String, List<MappingHandler<T>>> keyGroupMapping = new HashMap<>();
            step.getMappingHandlers().forEach(mappingHandler -> {
                String groupKey = mappingHandler.getKey();
                if (StringUtils.isEmpty(groupKey)) {
                    handlers.add(new MappingsHandler<T>().setMappingHandlers(mappingHandler));
                } else {
                    if (!keyGroupMapping.containsKey(groupKey)) {
                        keyGroupMapping.put(groupKey, new ArrayList<>());
                    }
                    List<MappingHandler<T>> mappingHandlers = keyGroupMapping.get(groupKey);
                    mappingHandlers.add(mappingHandler);
                }
            });

            if (MapUtils.isNotEmpty(keyGroupMapping)) {
                Map<String, Integer> mappingCount = step.getMappingCount();
                keyGroupMapping.forEach((groupKey, mappingHandlers) -> {
                    int handlerCount = MapUtils.getIntValue(mappingCount, groupKey);
                    Asserts.check(handlerCount > 0, "key: " + groupKey + " handlerCount less than zero");

                    List<MappingsHandler<T>> execHandlers = new ArrayList<>(handlerCount);
                    for (int i = 0; i < handlerCount; i++) {
                        execHandlers.add(new MappingsHandler<T>().setMappingHandlers(mappingHandlers));
                    }
                    mappingsStep.put(groupKey, execHandlers);
                });
            }
        }

        if (CollectionUtils.isEmpty(handlers) && MapUtils.isEmpty(workStep) && MapUtils.isEmpty(mappingsStep)) {
            throw new RuntimeException("step all handlers is null");
        }
    }

    /**
     * Gets steps.
     *
     * @return the steps
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * Sets steps.
     *
     * @param steps the steps
     */
    public void setSteps(List<Step> steps) {
        Asserts.notNull(steps, "steps");
        this.steps = steps;
    }

    /**
     * Sets steps.
     *
     * @param handlers the handlers
     */
    public void setSteps(Step... handlers) {
        Asserts.notNull(handlers, "handler");
        this.steps = Arrays.asList(handlers);
    }

    /**
     * Gets the component name.
     *
     * @return the component name
     */
    public String getDisruptorName() {
        return disruptorName;
    }

    /**
     * Sets the component name.
     *
     * @param disruptorName the new component name
     */
    public void setDisruptorName(String disruptorName) {
        this.disruptorName = disruptorName;
    }


    /**
     * Gets buffer size.
     *
     * @return the buffer size
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * Sets buffer size.
     *
     * @param bufferSize the buffer size
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * Gets producer type.
     *
     * @return the producer type
     */
    public ProducerType getProducerType() {
        return producerType;
    }

    /**
     * Sets producer type.
     *
     * @param producerType the producer type
     */
    public void setProducerType(ProducerType producerType) {
        this.producerType = producerType;
    }

    /**
     * Gets thread factory.
     *
     * @return the thread factory
     */
    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    /**
     * Sets thread factory.
     *
     * @param threadFactory the thread factory
     */
    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    /**
     * Gets wait strategy.
     *
     * @return the wait strategy
     */
    public WaitStrategy getWaitStrategy() {
        return waitStrategy;
    }

    /**
     * Sets wait strategy.
     *
     * @param waitStrategy the wait strategy
     */
    public void setWaitStrategy(WaitStrategy waitStrategy) {
        this.waitStrategy = waitStrategy;
    }

    /**
     * 关闭disruptor
     *
     * @author pengming
     * @date 2017年01月03日 15:42:30
     */
    public void shutdown() {
        if (disruptor != null) {
            disruptor.shutdown();
        }
        logger.info("disruptor: {} shutdown", disruptorName);
    }
}

