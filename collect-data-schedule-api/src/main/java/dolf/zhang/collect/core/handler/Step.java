package dolf.zhang.collect.core.handler;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 分步阶段对像
 *
 * @author pengming  
 * @date 2016年12月30日 下午6:04
 * @description
 */
public class Step {

    private List<WorkEventHandler> handlers;

    /** 表示相同的key值的handler 在disruptor中并行执行的线程数 */
    private Map<String, Integer> mappingCount;
    private List<MappingHandler> mappingHandlers;

    /**
     * Instantiates a new Step.
     */
    public Step() {
    }

    /**
     * Instantiates a new Step.
     *
     * @param handler the handler
     */
    public Step(WorkEventHandler handler) {
        if (handler != null && handlers == null) {
            this.handlers = new ArrayList<>();
        }
        handlers.add(handler);
    }

    /**
     * Instantiates a new Step.
     *
     * @param handler the handler
     */
    public Step(WorkEventHandler... handler) {
        if (ArrayUtils.isNotEmpty(handler)) {
            this.handlers = Arrays.asList(handler);
        }
    }

    /**
     * Instantiates a new Step.
     *
     * @param handlers the handler
     */
    public Step(List<WorkEventHandler> handlers) {
        this.handlers = handlers;
    }


    /**
     * Instantiates a new Step.
     *
     * @param mappingCount    the business count
     * @param mappingHandlers the business handlers
     */
    public Step(Map<String, Integer> mappingCount, List<MappingHandler> mappingHandlers) {
        this.mappingCount = mappingCount;
        this.mappingHandlers = mappingHandlers;
    }


    /**
     * Gets handlers.
     *
     * @return the handlers
     */
    public List<WorkEventHandler> getHandlers() {
        return handlers;
    }

    /**
     * Sets handlers.
     *
     * @param handlers the handlers
     */
    public void setHandlers(List<WorkEventHandler> handlers) {
        this.handlers = handlers;
    }


    /**
     * Gets business count.
     *
     * @return the business count
     */
    public Map<String, Integer> getMappingCount() {
        return mappingCount;
    }

    /**
     * Sets business count.
     *
     * @param mappingCount the business count
     */
    public void setMappingCount(Map<String, Integer> mappingCount) {
        this.mappingCount = mappingCount;
    }

    /**
     * Gets business handlers.
     *
     * @return the business handlers
     */
    public List<MappingHandler> getMappingHandlers() {
        return mappingHandlers;
    }

    /**
     * Sets business handlers.
     *
     * @param mappingHandlers the business handlers
     */
    public void setMappingHandlers(List<MappingHandler> mappingHandlers) {
        this.mappingHandlers = mappingHandlers;
    }
}
