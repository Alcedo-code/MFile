package com.angel.minio.mapper;


import com.angel.minio.domain.File;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 对数据表以及数据操作
 *
 * @author yangb
 */
public interface FileRepositoryMapper {

    File findById(String id);

    void deleteById(String id);

    int saveAndUpdate(@Param("file") File file);

    List<Map<String, String>> findFileIdAndName(@Param("ids") List<String> ids);


}
