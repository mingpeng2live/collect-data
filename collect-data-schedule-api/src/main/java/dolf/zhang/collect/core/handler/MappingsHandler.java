package dolf.zhang.collect.core.handler;

import dolf.zhang.collect.core.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * {@link MappingsHandler} 做为执行handler
 * 根据 supportType 方法返回来确定执行的 EventMappingHandler
 *
 * @author pengming  
 * @date 2016年12月29日 下午3:27
 * @description
 */
public class MappingsHandler<T> implements WorkEventHandler<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected List<MappingHandler<T>> mappingHandlers;

    @Override
    public void onEvent(Event<T> event, T data) throws Exception {
        for (MappingHandler mappingHandler : mappingHandlers) {
            if (mappingHandler.supportType(event.getTaskStrategyData())) {
                mappingHandler.onEvent(event, data);
                return;
            }
        }
    }

    public List<MappingHandler<T>> getMappingHandlers() {
        return mappingHandlers;
    }

    public MappingsHandler<T> setMappingHandlers(List<MappingHandler<T>> mappingHandlers) {
        this.mappingHandlers = mappingHandlers;
        return this;
    }

    public MappingsHandler<T> setMappingHandlers(MappingHandler<T>... mappingHandlers) {
        this.mappingHandlers = Arrays.asList(mappingHandlers);
        return this;
    }
}
