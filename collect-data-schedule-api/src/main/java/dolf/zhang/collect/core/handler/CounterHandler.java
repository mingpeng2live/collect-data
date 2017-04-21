package dolf.zhang.collect.core.handler;

import dolf.zhang.collect.core.disruptor.RunDisruptor;
import dolf.zhang.collect.core.event.Event;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 清理队列中事件对象存在的数据,方便垃圾回收,与
 * {@link Event} 中属性jobEvent 合用
 * @author pengming  
 * @date 2016年12月29日 下午3:27
 * @description
 */
public class CounterHandler implements EventHandler<Event> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private RunDisruptor runDisruptor;

    public void setRunDisruptor(RunDisruptor runDisruptor) {
        this.runDisruptor = runDisruptor;
    }

    @Override
    public void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception {
        // 清理
        event.setJobEvent(null);
        runDisruptor.getCount().decrementAndGet(); // 用于统计当前Buffer中还存在多少未处理的事件, 这里处理完减一
        logger.debug("clear jobEvent" + sequence);
    }

}
