package com.angel.minio.service;

import com.angel.minio.domain.Buckets;
import com.angel.minio.domain.TSystemApplication;

import java.util.List;
import java.util.Map;

/**
 * @Author YangBo
 * @Date 2024/3/20 15:15
 * @Version 1.0
 */
public interface ITSystemApplication {
    /**
     * 查询所有应用信息
     * @return
     */
    List<Buckets> queryList();
    /**
     * 查询所有系统应用
     * @return
     */
    Map<String, TSystemApplication> queryAllSystem();

    /**
     * 保存或者更新应用
     * @param systemName
     * @return
     */
    int insertAndUpdate(String systemName);

    /**
     * 删除系统
     * @param bucketName
     */
    void delSystemApplication(String bucketName);

}
