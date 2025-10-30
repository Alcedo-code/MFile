package com.angel.minio.domain;

import lombok.Data;

/**
 * @Author YangBo
 * @Date 2024/3/7 16:40
 * @Version 1.0
 */
@Data
public class Folder {
    /**
     * 系统及桶名称
     */
    private String bucketName;
    /**
     * 前缀路径
     */
    private String prefix;
}
