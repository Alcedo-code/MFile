package com.angel.minio.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author YangBo
 * @Date 2024/3/7 14:11
 * @Version 1.0
 */
@Data
public class Buckets {
    private String id;
    private String name;
    private String systemName;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
