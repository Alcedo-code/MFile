package com.angel.minio.config.minio;

import com.angel.minio.domain.Folder;
import com.angel.minio.util.utils.DateUtils;
import com.angel.minio.util.utils.FileTool;
import com.angel.minio.util.utils.StringUtils;
import com.angel.minio.util.web.domain.ResultEntity;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * minio配置类
 *
 * @Author yang
 * @Date 2023/1/3 14:03
 * @Version 1.0
 */
@Slf4j
@Component
public class MinioUtils {

    @Autowired
    private MinioClient minioClient;

    /**
     * 判断桶是否存在
     *
     * @param bucketName
     * @return
     */
    public boolean bucketExists(String bucketName) {
        try {
            boolean result = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            return result;
        } catch (Exception ex) {
            log.error("查询桶异常：{}", ex.toString());
            return false;
        }
    }


    /**
     * 获取全部 bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception ex) {
            log.error("异常错误：{}", ex.toString());
            return Collections.emptyList();
        }
    }

    /**
     * 创建 bucket
     *
     * @param bucketName 桶名
     */
    public void createBucket(String bucketName) {
        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
        try {
            if (bucketExists(bucketName))
                return;
            minioClient.makeBucket(makeBucketArgs);
        } catch (Exception e) {
            log.error("创建桶失败：{}", e.getMessage());
        }
    }

    /**
     * 根据 bucketName 删除信息
     *
     * @param bucketName 桶名
     */
    public void removeBucket(String bucketName) {
        try {
            if (bucketExists(bucketName)) {

                // 递归列举某个bucket下的所有文件，然后循环删除
                Iterable<Result<Item>> iterable = minioClient.listObjects(ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .recursive(true)
                        .build());

                for (Result<Item> itemResult : iterable) {
                    deleteFile(bucketName, itemResult.get().objectName());
                }
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 查询桶内数据信息
     *
     * @param folder
     * @return
     */
    public List<HashMap<String, String>> queryBucket(Folder folder) {

        Iterable<Result<Item>> objects;
        int counts = 0;
        Item item = null;
        try {
            ListObjectsArgs listObjectsArgs;
            String bucketName = folder.getBucketName();
            String url = folder.getPrefix();
            if (StringUtils.isEmpty(bucketName) || !bucketExists(bucketName)) {
                return Collections.emptyList();
            }
            if (StringUtils.isEmpty(url)) {
                listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).build();
            } else {
                listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).prefix(url).build();
            }
            // 获取文件列表
            objects = minioClient.listObjects(listObjectsArgs);
            List<HashMap<String, String>> list = new ArrayList<>();
            HashMap<String, String> hashMap;
            // 获取对象的完整元数据以获取 Content-Type
            StatObjectResponse metaData;
            for (Result<Item> result : objects) {

                hashMap = new HashMap<>();
                item = result.get();

                try {
                    if (item.objectName().equals(folder.getPrefix())) {
                        continue;
                    }
                    hashMap.put("name", item.objectName());
                    if (!item.objectName().endsWith("/")) {
                        metaData = minioClient.statObject(StatObjectArgs.builder()
                                .bucket(bucketName)
                                .object(item.objectName())
                                .build());
                        if (StringUtils.isNotNull(metaData)) {
                            hashMap.put("type", metaData.contentType());
                        } else {
                            hashMap.put("type", "");
                        }
                    } else {
                        hashMap.put("type", "");
                    }
                    String lastTime = "";
                    if (item.size() == 0) {
                        hashMap.put("size", "");
                        lastTime = "";
                    } else {
                        hashMap.put("size", (item.size() / 1024) + "KB");
                        lastTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.parseDate(item.lastModified().toString()));
                    }
                    hashMap.put("lastTime", lastTime);
                } catch (Exception ee) {
                    continue;
                }
                list.add(hashMap);
            }
            return list;
        } catch (Exception ex) {
            log.error("查询桶异常：{}", ex.toString());
            return Collections.emptyList();
        }
    }

    /**
     * 查询桶内文件列表
     *
     * @param bucketName
     * @param prefix
     * @return
     */
    public Iterable<Result<Item>> getListObjects(String bucketName, String prefix) {
        try {

            ListObjectsArgs listObjectsArgs;
            // 获取文件列表
            Iterable<Result<Item>> iterable;

            if (bucketExists(bucketName)) {

                if (StringUtils.isEmpty(prefix)) {
                    listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).recursive(true).build();
                } else {
                    listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).recursive(true).prefix(prefix).build();
                }
                iterable = minioClient.listObjects(listObjectsArgs);

            } else {
                iterable = null;
            }
            return iterable;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * 获取不递归的文件列表对象
     * @param bucketName
     * @param prefix
     * @return
     */
    public Iterable<Result<Item>> getListObjectsNoRecursive(String bucketName, String prefix) {
        try {
            ListObjectsArgs listObjectsArgs;
            // 获取文件列表
            Iterable<Result<Item>> iterable;
            if (bucketExists(bucketName)) {
                if (StringUtils.isEmpty(prefix)) {
                    listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).build();
                } else {
                    listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).build();
                }
                iterable = minioClient.listObjects(listObjectsArgs);
            } else {
                iterable = null;
            }
            return iterable;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * 判断文件是否存在
     *
     * @param bucketName 存储桶
     * @param objectName 对象
     * @return true：存在
     */
    public boolean doesObjectExist(String bucketName, String objectName) {
        StatObjectArgs args = StatObjectArgs.builder().bucket(bucketName).object(objectName).build();
        try {
            minioClient.statObject(args);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 判断文件夹是否存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件夹名称（去掉/）
     * @return true：存在
     */
    public boolean doesFolderExist(String bucketName, String objectName) {
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(objectName)
                .recursive(false)
                .build();
        boolean exist = false;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(args);
            for (Result<Item> result : results) {
                Item item = result.get();
                if (!item.isDir())
                    continue;
                if (objectName.equals(item.objectName())) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }


    /**
     * 按照宽高输出图片 字节
     * @param bucketName
     * @param objectName
     * @param queryParams
     * @return
     */
    public byte[] getInputByteObject(String bucketName,String objectName, Map<String, String> queryParams) {
        try {
            // 构建HTTP请求参数
            GetObjectArgs objectArgs = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .extraQueryParams(queryParams)
                    .build();
            // 获取图片对象并处理
            byte[] content = new byte[0];
            try(InputStream inputStream = minioClient.getObject(objectArgs)){
                content = FileTool.toByteArray(inputStream);
            }
            return content;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * Minio文件上传
     *
     * @param file       文件实体
     * @param fileName   修饰过的文件名 非源文件名
     * @param bucketName 所存文件夹（桶名）
     * @return
     */
    public ResultEntity<Map<String, Object>> minioUpload(MultipartFile file, String folderName, String fileName, String bucketName) {
        ResultEntity<Map<String, Object>> resultEntity = new ResultEntity<Map<String, Object>>();
        try {
            InputStream inputStream = file.getInputStream();
            PutObjectArgs objectArgs;
            if (StringUtils.isEmpty(folderName) || folderName.equals("/")) {
                objectArgs = PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(file.getContentType()).build();
            } else {
                objectArgs = PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(folderName + "/" + fileName)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(file.getContentType()).build();
            }
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
            return resultEntity;
        } catch (Exception e) {
            e.printStackTrace();
            resultEntity.setCode(1);
            resultEntity.setMsg("上传失败");
            return resultEntity;
        }
    }


    /**
     * 通过 MultipartFile ，上传文件
     *
     * @param bucketName 存储桶
     * @param file       文件
     * @param objectName 对象名
     */
    public ObjectWriteResponse putObject(String bucketName, MultipartFile file, String objectName, String contentType) {
        try {
            InputStream inputStream = file.getInputStream();
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            return minioClient.putObject(args);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            log.error("putObject方法上传文件异常：{}", e.toString());
            return null;
        }
    }

    /**
     * 上传本地文件
     *
     * @param bucketName 存储桶
     * @param objectName 对象名称
     * @param fileName   本地文件路径
     */
    public ObjectWriteResponse putObject(String bucketName, String objectName, String fileName) {
        try {
            UploadObjectArgs args = UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(fileName)
                    .build();
            return minioClient.uploadObject(args);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            log.error("putObject方法上传文件异常：{}", e.toString());
            return null;
        }
    }

    /**
     * 通过流上传文件
     *
     * @param bucketName  存储桶
     * @param objectName  文件对象
     * @param inputStream 文件流
     */
    public ObjectWriteResponse putObjectByStream(String bucketName, String objectName, InputStream inputStream, String contentType) {
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType).build();
            return minioClient.putObject(args);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            log.error("putObjectByStream方法上传文件异常：{}", e.toString());
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (Exception ex) {
                log.error("文件流关闭异常：{}", ex.toString());
            }
        }
    }

    /**
     * 创建文件夹或目录
     *
     * @param bucketName 存储桶
     * @param objectName 目录路径
     */
    public ObjectWriteResponse putDirObject(String bucketName, String objectName) {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                .build();
        try {
            return minioClient.putObject(args);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            log.error("putDirObject方法上传文件异常：{}", e.toString());
            return null;
        }
    }


    /**
     * 获取⽂件外链
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @param expires    过期时间 <=7
     */
    public String getObjectUrl(String bucketName, String objectName, Integer expires) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(expires)    // 单位：秒
                .build();
        try {
            return minioClient.getPresignedObjectUrl(args);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | InvalidKeyException | NoSuchAlgorithmException | IOException | XmlParserException | ServerException e) {
            log.error("getObjectUrl方法上传文件异常：{}", e.toString());
            return null;
        }
    }

    /**
     * 获取⽂件外链
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @param duration   过期时间
     * @param unit       过期时间的单位
     */
    public String getObjectUrl(String bucketName, String objectName, int duration, TimeUnit unit) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(duration, unit)
                .build();
        try {
            return minioClient.getPresignedObjectUrl(args);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | InvalidKeyException | NoSuchAlgorithmException | IOException | XmlParserException | ServerException e) {
            log.error("getObjectUrl方法上传文件异常：{}", e.toString());
            return null;
        }
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @return ⼆进制流
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        return minioClient.getObject(args);
    }


    /**
     * 下载文件
     *
     * @param bucketName
     * @param path
     * @param response
     * @return
     */
    public InputStream downloadFile(String bucketName, String path, HttpServletResponse response) {
        try {
            InputStream file = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(path).build());
            String filename;
            if (StringUtils.isNotEmpty(path)) {
                filename = path.split("/")[path.split("/").length - 1];
            } else {
                filename = StringUtils.getUUid();
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = file.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            file.close();
            servletOutputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除某个文件
     *
     * @param bucketName 桶名称
     * @param fileName   文件名称
     * @return
     */
    public boolean deleteFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除文件目录
     *
     * @param folder
     */
    public void deleteFolder(Folder folder) {
        try {
            // 遍历并删除指定前缀的所有对象
            ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                    .bucket(folder.getBucketName())
                    .prefix(folder.getPrefix())
                    .build();
            Iterable<Result<Item>> objects = minioClient.listObjects(listObjectsArgs);
            for (Result<Item> result : objects) {
                Item item = result.get();
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(folder.getBucketName())
                                .object(item.objectName())
                                .build());
                log.info("Deleted object: {}", item.objectName());
                // 检查是否为"虚拟文件夹"（即以"/"结尾的对象）
                if (item.objectName().endsWith("/") && !item.objectName().equals(folder.getPrefix())) {
                    // 如果是，则递归删除该"文件夹"下的对象
                    Folder folderNew = new Folder();
                    folderNew.setBucketName(folder.getBucketName());
                    folderNew.setPrefix(item.objectName());
                    deleteFolder(folderNew);
                }
            }

        } catch (Exception ex) {
            log.error("删除目录异常：{}", ex.toString());
        }
    }

    /**
     * 根据文件路径得到预览文件绝对地址
     *
     * @param bucketName
     * @param url
     * @return
     */
    public String getPreviewFileUrl(String bucketName, String url) {
        try {
            /**
             Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
             PutObjectArgs args = PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
             Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().prefix(folderName+"/").bucket(bucketName).build());
             return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(folderName + "/" + fileName).build());
             Iterable<Result<Item>> results =
             minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
             for (Result<Item> result : results) {
             Item item = result.get();
             System.out.println(item.lastModified() + "\t" + item.size() + "\t" + item.objectName());
             }
             **/

            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(url).method(Method.GET).build());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 设置桶公有
     */
    public void setBucketPublicPolicy(String bucketName) {
        try {
            String policy = "{\"Version\":\"2012-10-17\"," +
                    "\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":" +
                    "{\"AWS\":[\"*\"]},\"Action\":[\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"," +
                    "\"s3:GetBucketLocation\"],\"Resource\":[\"arn:aws:s3:::" + bucketName +
                    "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\"],\"Resource\":[\"arn:aws:s3:::" +
                    bucketName +
                    "/*\"]}]}";
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build());
        } catch (Exception ex) {
            log.error("setBucketPublicPolicy方法设置桶权限异常:{}", ex.toString());
        }
    }


}

