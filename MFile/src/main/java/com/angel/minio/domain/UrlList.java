package com.angel.minio.domain;

import lombok.Data;

/**
 * @Author YangBo
 * @Date 2024/3/7 16:40
 * @Version 1.0
 */
@Data
public class UrlList {
    /**
     * 前缀路径
     */
    private String prefix;
    /**
     * 是否文件夹
     */
    private boolean recursive;
}
