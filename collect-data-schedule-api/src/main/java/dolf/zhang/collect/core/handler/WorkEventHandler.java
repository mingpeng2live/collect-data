package dolf.zhang.collect.core.handler;

import dolf.zhang.collect.core.event.Event;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * handler 父接口, 整合work, event接口
 *
 * @author pengming  
 * @date 2017年01月05日 下午3:54
 * @description
 */
public interface WorkEventHandler<T> extends WorkHandler<Event<T>>, EventHandler<Event<T>> {

    Logger logger = LoggerFactory.getLogger(WorkEventHandler.class);

    @Override
    default void onEvent(Event<T> event) throws Exception {
        onEvent(event, event.getJobEvent());
    }

    @Override
    default void onEvent(Event<T> event, long sequence, boolean endOfBatch) throws Exception{
        logger.debug("sequence: {} endOfBatch: {}", sequence, endOfBatch);
        onEvent(event, event.getJobEvent());
    }

    /**
     * 如果要实现多线程执行同一功能请自行重写返回相同的key值
     */
    default String getKey() { return null; }

    default void setKey(String key) {}

    void onEvent(Event<T> event, T data) throws Exception;

}
