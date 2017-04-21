package dolf.zhang.collect.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


/**
 * 常量类.
 *
 * @author pengming  
 * @date 2017年01月04日 11:09:27
 * @description
 */
public class ConstantProcess {

    /** 日志. */
    private static Logger logger = LoggerFactory.getLogger(ConstantProcess.class);

//    /** 备用机制查询数据的限制. */
//    public static final Integer SPARE_LIMIT = Integer.parseInt(ObjectUtils.toString(getProperty("spare.limit"), "1"));

//    /** Process监听的队列. */
//    public static final String QUEUE_PROCESS_HANDLER = getProperty("QUEUE_PROCESS_HANDLER");
//
//    /** 通知SP有新数据进去对列表. */
//    public static final String QUEUE_CHANNEL_SP_DATA = getProperty("QUEUE_CHANNEL_SP_DATA");
//
//    /** Process MQ 检测. */
//    public static final String QUEUE_PROCESS_CHECK = getProperty("QUEUE_PROCESS_CHECK");

    /**
     * 被锁定的数据状态.
     */
    public static final int ONPROCESSING_STATUS = 4;

    /**
     * 处理完的状态.
     */
    public static final int PROCESSING_END_STATUS = 1;

    /**
     * 处理失败的状态返款成功.
     */
    public static final int FAIL_BACKBALANCE_SUC = 50;

    /**
     * 处理失败的状态返款失败.
     */
    public static final int FAIL_BACKBALANCE_ERR = 51;

    /**
     * The constant DISRUPTOR.
     */
//    /** disruptor */
//    public static final int DISRUPTOR = Integer.parseInt(ObjectUtils.toString(getProperty("ringbuffersize"), "64"));
    public static final int DISRUPTOR = 1024;
//
//    /** 事务中分批入库数量 */
//    public static final int INSERTNUM = Integer.parseInt(ObjectUtils.toString(getProperty("insertnum"), "100"));
//
//    /** 入库最小数量 */
//    public static final int TRANSITNUM = Integer.parseInt(ObjectUtils.toString(getProperty("transitnum"), "5000"));
//
//    /** 备用任务启动间隔时间*/
//    public static final int SPARETIME = Integer.parseInt(ObjectUtils.toString(getProperty("sparetime"), "30000"));
//
//    /** 主任务启动间隔时间*/
//    public static final int INSERTTIME = Integer.parseInt(ObjectUtils.toString(getProperty("inserttime"), "5000"));

    /**
     * 数据库字典表获取process服务器的数量
     */
    public static final Integer PROCESSNUMTYPE = 31;


    /**
     * if true linux else false windows.
     */
    public static final boolean SYS_TYPE = System.getProperty("os.name").equals("Linux");

    /**
     * ip地址.
     */
    public static String IP;

    static {
        try {
            if (!SYS_TYPE){
                IP = InetAddress.getLocalHost().getHostAddress();
            } else {
                boolean bFindIP = false;
                InetAddress address = null;
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    NetworkInterface ni = netInterfaces.nextElement();
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        address = ips.nextElement();
                        if (address.isSiteLocalAddress() && !address.isLoopbackAddress() // 127.开头的都是lookback地址
                                && address.getHostAddress().indexOf(":") == -1) {
                            bFindIP = true;
                            IP = address.getHostAddress();
                            break;
                        }
                    }
                }
            }
            if (null == IP){
                throw new Exception();
            }
        } catch (Exception e) {
            logger.error("获取服务IP地址失败", e);
        }
    }

    /**
     * 应用部署地址.
     */
    public static final String DEPLOY_PATH = System.getProperty("user.dir");

    /**
     * 系統对应转译符.
     */
    public static final String ESCAPE_CHAR = SYS_TYPE ? "" : "\\";

    /**
     * serid 服务ID.
     */
    public static final String SERID = IP.replaceAll("\\.", "-") + DEPLOY_PATH.replaceAll(ESCAPE_CHAR+File.separator, "-");

    /**
     * The constant Path_x86.
     */
//请求数据写文件
    public static String Path_x86 = "c:" + File.separator + "csFiles";


//    public static Long parseLong(Object longs){
//    	return Long.parseLong(ObjectUtils.toString(longs, "0"));
//    }
//
//    public static Integer parseInt(Object ints){
//    	return Integer.parseInt(ObjectUtils.toString(ints, "0"));
//    }

}
