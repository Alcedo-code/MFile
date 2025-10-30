package com.angel.minio.domain;

import lombok.Data;

/**
 * @Author YangBo
 * @Date 2024/3/16 12:48
 * @Version 1.0
 */
@Data
public class File {

    private String id;
    private String fileName;
    private String mimeType;
    /*相对位置*/
    private String location;

}
