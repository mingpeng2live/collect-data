package com.pm.distest;

import java.util.Date;
import java.util.Map;

/**
 * @author dolf
 * @description
 * @date 16/12/13
 */
public class Job {

    /**任务id*/
    private String jobId;

    /**平台*/
    private String platform ;
    /**店铺id*/
    private String shopId ;
    /**任务状态*/
    private String status;
    /**任务类型*/
    private String type;
    /**任务创建时间*/
    private Date created;
    /**任务级别*/
    private Integer priority;
    /**任务验证时间*/
    private Date checkedDate;
    /**任务提交时间*/
    private Date submittedDate;
    /**任务失效时间*/
    private Date invalidedDate;
    /**任务执行数据*/
    private Object dataSource;
    /**参数*/
    private Map<String, Object> arguments;

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    public Date getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Object getDataSource() {
        return dataSource;
    }

    public void setDataSource(Object dataSource) {
        this.dataSource = dataSource;
    }

    public Date getInvalidedDate() {
        return invalidedDate;
    }

    public void setInvalidedDate(Date invalidedDate) {
        this.invalidedDate = invalidedDate;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
