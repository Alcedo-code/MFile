package com.angel.minio.mapper;

import com.angel.minio.domain.TSystemApplication;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author YangBo
 * @Date 2024/3/20 14:44
 * @Version 1.0
 */
public interface TSystemApplicationMapper {

    List<TSystemApplication> selectByAll();

    int saveAndUpdate(@Param("systemApplication") TSystemApplication systemApplication);

    void deleteById(String id);

    int deleteByBucketName(String bucketName);


}
