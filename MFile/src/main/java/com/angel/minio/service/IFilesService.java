package com.angel.minio.service;

import com.angel.minio.domain.File;
import com.angel.minio.domain.Folder;
import com.angel.minio.domain.Folders;
import com.angel.minio.util.web.domain.ResultEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yangbo
 * @Date 2023/7/4 19:31
 * @Version 1.0
 */
public interface IFilesService {


    /**
     * 创建文件夹
     *
     * @param folder
     */
    void createFolder(Folder folder);


    /**
     * 删除文件夹
     *
     * @param folder
     */
    void removeFolder(Folder folder);

    /**
     * 上传文件
     *
     * @return
     */
    ResultEntity<Map<String, Object>> uploadFile(MultipartFile file, Folder folder);

    /**
     * zip压缩包上传
     *
     * @param file
     * @param folder
     */
    int uploadZip(MultipartFile file, Folder folder);

    /**
     * 获取文件地址
     *
     * @param folder
     * @return
     */
    String getRedFile(Folder folder);

    /**
     * 下载文件
     *
     * @param bucketName
     * @param prefix
     * @return
     */
    InputStream downloadFile(String bucketName, String prefix);

    /**
     * 删除文件
     *
     * @param bucketName
     * @param fileName
     * @return
     */
    boolean deleteFile(String bucketName, String fileName);

    /**
     * 上传本地文件列表到minio
     *
     * @param fileList
     * @param dirName
     * @param folder
     */
    void putDirObjectOrFile(List<String> fileList, String dirName, String dec, Folder folder);

    /**
     * minio文件夹下载
     *
     * @param name
     * @param bucketName
     * @param prefix
     */
    void minioDownloadZip(String name, String bucketName, String prefix);

    /**
     * 批量删除下载前缀列表
     *
     * @param folder
     */
    void deletePrefixList(Folders folder);

    /**
     * 查询指定路径文件列表
     *
     * @param folder
     * @return
     */
    List<HashMap<String, String>> queryBucket(Folder folder);

    //region ==兼容早期代码

    /**
     * 查询表文件
     *
     * @param id
     * @return
     */
    File findById(String id);

    /**
     * 文件上传
     *
     * @param file
     * @param inputStream
     * @return
     */
    File uploadFile(File file, InputStream inputStream);

    /**
     * 获取指定宽高图片
     *
     * @param objectName
     * @param width
     * @param height
     * @return
     */
    byte[] getImageByte(String objectName, int width, int height, String fileType);

    /**
     * 删除文件
     *
     * @param ids
     */
    void delFile(String[] ids);

    /**
     * 按编号列表删除
     *
     * @param ids
     * @return
     */
    List<Map<String, String>> findFileIdAndName(List<String> ids);

    //endregion 兼容早期代码 ==end


}
