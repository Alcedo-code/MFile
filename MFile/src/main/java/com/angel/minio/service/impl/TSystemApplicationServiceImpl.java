package com.angel.minio.service.impl;

import com.angel.minio.domain.Buckets;
import com.angel.minio.mapper.TSystemApplicationMapper;
import com.angel.minio.service.ITSystemApplication;
import com.angel.minio.config.minio.MinioUtils;
import com.angel.minio.domain.TSystemApplication;
import com.angel.minio.util.utils.CharacterParser;
import com.angel.minio.util.utils.DateUtils;
import com.angel.minio.util.utils.StringUtils;
import io.minio.messages.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author YangBo
 * @Date 2024/3/20 15:17
 * @Version 1.0
 */
@Service
public class TSystemApplicationServiceImpl implements ITSystemApplication {


    private static Logger logger = LoggerFactory.getLogger(TSystemApplicationServiceImpl.class);

    @Resource
    MinioUtils minioUtils;

    @Resource
    TSystemApplicationMapper systemApplicationMapper;

    CharacterParser characterParser = CharacterParser.getInstance();


    /**
     * 查询所有应用信息
     *
     * @return
     */
    @Override
    public List<Buckets> queryList() {

        List<Buckets> list = new ArrayList<>();
        Buckets buckets;
        TSystemApplication application;

        List<Bucket> resultList = minioUtils.getAllBuckets();
        Map<String, TSystemApplication> map = queryAllSystem();
        if (resultList.size() > 0) {
            for (Bucket bucket : resultList) {
                buckets = new Buckets();
                buckets.setName(bucket.name());
                application = map.get(bucket.name());
                if (StringUtils.isNull(application)) {
                    buckets.setSystemName(bucket.name());
                    buckets.setId(bucket.name());
                } else {
                    buckets.setId(application.getId());
                    buckets.setSystemName(application.getSystemName());
                }
                buckets.setCreateTime(DateUtils.parseDate(bucket.creationDate().toString()));
                list.add(buckets);
            }
        }
        return list;
    }

    /**
     * 查询所有系统应用
     *
     * @return
     */
    @Override
    public Map<String, TSystemApplication> queryAllSystem() {
        Map<String, TSystemApplication> map = new HashMap<>();
        List<TSystemApplication> resultList = systemApplicationMapper.selectByAll();
        if (resultList.size() > 0) {
            for (TSystemApplication item : resultList) {
                map.put(item.getBucketName(), item);
            }
        }
        return map;
    }

    /**
     * 保存或者更新应用
     *
     * @param systemName
     * @return
     */
    @Override
    @Transactional
    public int insertAndUpdate(String systemName) {

        TSystemApplication systemApplication = new TSystemApplication();
        try {
            String bucketName = "ga" + StringUtils.StringFilter(characterParser.getPinYinSpelling(systemName.replace("ga", ""))).toLowerCase() + DateUtils.dateTimeNowSss();
            minioUtils.createBucket(bucketName);
            minioUtils.setBucketPublicPolicy(bucketName);
            String id = StringUtils.getUUid();
            systemApplication.setId(id);
            systemApplication.setSystemName(systemName);
            systemApplication.setBucketName(bucketName);
            systemApplication.setCreateTime(DateUtils.getNowDate());
            return systemApplicationMapper.saveAndUpdate(systemApplication);
        } catch (Exception ex) {
            logger.error("创建应用异常：{}", ex.toString());
            return 0;
        }
    }


    /**
     * 删除系统
     *
     * @param bucketName
     */
    @Override
    @Transactional
    public void delSystemApplication(String bucketName) {
        try {
            minioUtils.removeBucket(bucketName);
            systemApplicationMapper.deleteByBucketName(bucketName);
        } catch (Exception e) {
            logger.error("删除应用系统异常：{}", e.toString());
        }
    }
}
