package com.angel.minio.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName t_system_application
 */
public class TSystemApplication implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 应用编码
     */
    private String id;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 实际桶名称(生成名称)
     */
    private String bucketName;

    /**
     * 创建日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 应用编码
     */
    public String getId() {
        return id;
    }

    /**
     * 应用编码
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 系统名称
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * 系统名称
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * 实际桶名称(生成名称)
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * 实际桶名称(生成名称)
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
