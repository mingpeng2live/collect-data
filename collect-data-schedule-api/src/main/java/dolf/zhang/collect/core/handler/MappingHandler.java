package dolf.zhang.collect.core.handler;

/**
 * {@link MappingsHandler} 做为执行handler
 * 根据 supportType 方法返回来确定执行的 {@link MappingHandler}
 *
 * @author pengming  
 * @date 2017年02月23日 下午6:16
 * @description
 */
public interface MappingHandler<T> extends WorkEventHandler<T> {

    boolean supportType(String type);

}
