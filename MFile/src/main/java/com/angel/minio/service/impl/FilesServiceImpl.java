package com.angel.minio.service.impl;

import com.angel.minio.domain.Folder;
import com.angel.minio.domain.Folders;
import com.angel.minio.mapper.FileRepositoryMapper;
import com.angel.minio.service.IFilesService;
import com.angel.minio.config.minio.MinioUtils;
import com.angel.minio.domain.UrlList;
import com.angel.minio.util.utils.DateUtils;
import com.angel.minio.util.utils.ServletUtils;
import com.angel.minio.util.utils.StringUtils;
import com.angel.minio.util.utils.ZipUtil;
import com.angel.minio.util.web.domain.ResultEntity;
import io.minio.Result;
import io.minio.messages.Item;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author yangbo
 * @Date 2024/3/8 19:38
 * @Version 1.0
 */
@Service
public class FilesServiceImpl implements IFilesService {

    private static Logger logger = LoggerFactory.getLogger(FilesServiceImpl.class);

    @Resource
    MinioUtils minioUtils;

    @Resource
    FileRepositoryMapper fileRepositoryMapper;

    /**
     * 获取上传文件路径
     */
    @Value("${part.uploadPath}")
    private String uploadPath;

    @Value("${minio.bucketName}")
    private String bucketName;


    /**
     * 创建文件夹
     *
     * @param folder
     */
    @Override
    public void createFolder(Folder folder) {
        String url = folder.getPrefix();
        String bucketName = folder.getBucketName();
        minioUtils.putDirObject(bucketName, url);
    }

    /**
     * 删除文件夹
     *
     * @param folder
     */
    @Override
    public void removeFolder(Folder folder) {
        minioUtils.deleteFolder(folder);
    }

    /**
     * 上传文件
     *
     * @return
     */
    @Override
    public ResultEntity<Map<String, Object>> uploadFile(MultipartFile file, Folder folder) {
        // fileName为空，说明要使用源文件名上传
        String fileName = file.getOriginalFilename();
        String bucketName = folder.getBucketName();
        String prefix = folder.getPrefix();
        ResultEntity<Map<String, Object>> result = minioUtils.minioUpload(file, prefix, fileName, bucketName);
        return result;
    }

    /**
     * zip压缩包上传
     *
     * @param file
     * @param folder
     */
    @Override
    public int uploadZip(MultipartFile file, Folder folder) {
        try {
            // 创建临时目录
            String tempPath = new File(uploadPath).getPath() + File.separatorChar + "temp" + File.separatorChar;
            //如果临时文件不存在则创建
            File path = new File(tempPath);
            if (!path.exists()) {
                path.mkdirs();
            }
            //获取文件名（包括后缀）
            String sourceName = file.getOriginalFilename();
            String filePath = tempPath + sourceName;
            File savePath = new File(filePath);
            file.transferTo(savePath);
            //解压目标文件夹名(源zip文件去掉后缀)
            String dec = sourceName.substring(0, sourceName.lastIndexOf('.'));
            //解压目标文件夹路径
            String decPath = new File(uploadPath).getPath() + File.separatorChar + dec;
            // 获取解压出来的文件名 不带后缀
            List<String> fileNames = ZipUtil.unZip(savePath, decPath);
            putDirObjectOrFile(fileNames, decPath, dec, folder);
            logger.info("list:{}", fileNames);
            //解析完成   删除本次解析中生成的文件  删除此目录下的所有文件
            ZipUtil.deleteDirectory(new File(uploadPath + File.separatorChar + dec).getPath());
            //删除本身zip包
            ZipUtil.deleteDirectory(new File(uploadPath + File.separatorChar + "temp").getPath());
        } catch (Exception ex) {
            logger.error("上传压缩包异常：{}", ex.toString());
            return 0;
        }
        return 1;
    }


    /**
     * 获取文件地址
     *
     * @param folder
     * @return
     */
    @Override
    public String getRedFile(Folder folder) {
        String outUrl;
        try {
            String bucketName = folder.getBucketName();
            String prefix = folder.getPrefix();
            outUrl = minioUtils.getPreviewFileUrl(bucketName, prefix);
        } catch (Exception ex) {
            outUrl = "获取文件地址异常";
            logger.error("读取文件异常：{}", ex.toString());
        }
        return outUrl;
    }


    /**
     * 下载文件
     *
     * @param bucketName
     * @param prefix
     * @return
     */
    @Override
    public InputStream downloadFile(String bucketName, String prefix) {
        HttpServletResponse response = ServletUtils.getResponse();
        return minioUtils.downloadFile(bucketName, prefix, response);
    }


    /**
     * 删除文件
     *
     * @param bucketName
     * @param fileName
     * @return
     */
    @Override
    public boolean deleteFile(String bucketName, String fileName) {
        boolean flag = minioUtils.deleteFile(bucketName, fileName);
        return flag;
    }

    /**
     * 上传本地文件列表到minio
     *
     * @param fileList
     * @param dirName
     * @param folder
     */
    @Override
    public void putDirObjectOrFile(List<String> fileList, String dirName, String dec, Folder folder) {
        String souceName;
        try {
            String contentType;
            String bucketName = folder.getBucketName();
            String prefix = "";

            if (StringUtils.isNotEmpty(folder.getPrefix()) && !folder.getPrefix().equals("/")) {
                prefix = folder.getPrefix();
            }
            for (String filePath : fileList) {
                // 检查是否为"虚拟文件夹"（即以"/"结尾的对象）
                if (!filePath.endsWith("/")) {
                    souceName = dirName + File.separatorChar + filePath;
                    //获取文件类型
                    contentType = getContentType(souceName);
                    InputStream fileStream = new FileInputStream(new File(souceName));
                    minioUtils.putObjectByStream(bucketName, prefix + dec + "/" + filePath, fileStream, contentType);
                    fileStream.close();
                }
            }
        } catch (Exception ex) {
            logger.error("上传异常：{}", ex.toString());
        }

    }

    /**
     * 获取文件类型，保证每个文件都有类型
     *
     * @param fileUrl
     * @return
     */
    public String getContentType(String fileUrl) {
        String contentType = "";
        try {
            contentType = Files.probeContentType(Paths.get(fileUrl));
            if (StringUtils.isNull(contentType)) {
                contentType = "application/octet-stream";
            }
        } catch (Exception e) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    /**
     * 下载zip包处理
     *
     * @param name
     * @param bucketName
     * @param prefix
     */
    @Override
    public void minioDownloadZip(String name, String bucketName, String prefix) {
        HttpServletResponse response = ServletUtils.getResponse();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        File zipFile;
        try {
            // 获取文件列表
            Iterable<Result<Item>> objects = minioUtils.getListObjects(bucketName, prefix);
            zipFile = compressedFileToZip(name, bucketName, objects);
            FileInputStream ins = new FileInputStream(zipFile);
            WritableByteChannel writableByteChannel = Channels.newChannel(os);
            FileChannel fileChannel = ins.getChannel();
            fileChannel.transferTo(0, fileChannel.size(), writableByteChannel);
            fileChannel.close();
            response.setCharacterEncoding("UTF-8");
            name = URLEncoder.encode(name, "UTF-8") + ".zip";
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("iso8859-1")));
            response.setContentLength(os.size());
            response.setHeader("filename", name);
            response.addHeader("Content-Length", "" + os.size());
            OutputStream outputstream = response.getOutputStream();
            os.writeTo(outputstream);
            os.flush();
            os.close();
            outputstream.flush();
            outputstream.close();
            writableByteChannel.close();
            if (zipFile.exists()) {
                //删除临时文件
                zipFile.delete();
            }
        } catch (IOException e) {
            logger.error("下载文件异常：{}", e.toString());
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                logger.error("文件流关闭异常：{}", e.toString());
            }
        }
    }

    /**
     * 批量删除下载前缀列表
     *
     * @param folder
     */
    @Override
    public void deletePrefixList(Folders folder) {
        String bucketName = folder.getBucketName();
        List<UrlList> urlLists = folder.getList();
        Folder folderNew;
        //遍历删除
        if (urlLists.size() > 0) {
            for (int i = 0; i < urlLists.size(); i++) {
                String objectName = urlLists.get(i).getPrefix();
                boolean recursive = urlLists.get(i).isRecursive();
                if (recursive) {
                    // 如果是，则递归删除该"文件夹"下的对象
                    folderNew = new Folder();
                    folderNew.setBucketName(bucketName);
                    folderNew.setPrefix(objectName);
                    minioUtils.deleteFolder(folderNew);
                } else {
                    minioUtils.deleteFile(bucketName, objectName);
                }
            }
        }
    }

    /**
     * 查询指定路径文件列表
     *
     * @param folder
     * @return
     */
    @Override
    public List<HashMap<String, String>> queryBucket(Folder folder) {
        Iterable<Result<Item>> objects;
        try {
            String bucketName = folder.getBucketName();
            String url = folder.getPrefix();
            if (StringUtils.isEmpty(bucketName) || !minioUtils.bucketExists(bucketName)) {
                return Collections.emptyList();
            }
            // 获取文件列表
            objects = minioUtils.getListObjectsNoRecursive(bucketName, url);
            List<HashMap<String, String>> list = new ArrayList<>();
            HashMap<String, String> hashMap;
            Item item = null;
            String lastTime;
            // 获取对象的完整元数据以获取 Content-Type
            //StatObjectResponse metaData; 需要调用sdk 缓慢
            for (Result<Item> result : objects) {
                hashMap = new HashMap<>();
                item = result.get();
                try {
                    if (item.objectName().equals(folder.getPrefix())) {
                        continue;
                    }
                    hashMap.put("name", item.objectName());
                    if (!item.objectName().endsWith("/")) {

                        hashMap.put("type", getFileType(item.objectName().toLowerCase()));
                    } else {
                        hashMap.put("type", "");
                    }

                    if (StringUtils.isNotNull(item) && item.size() == 0) {
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
            logger.error("查询桶异常：{}", ex.toString());
            return Collections.emptyList();
        }

    }


    /**
     * 查询表文件
     *
     * @param id
     * @return
     */
    @Override
    public com.angel.minio.domain.File findById(String id) {
        return fileRepositoryMapper.findById(id);
    }


    /**
     * 文件上传
     *
     * @param file
     * @param inputStream
     * @return
     */
    @Transactional
    @Override
    public com.angel.minio.domain.File uploadFile(com.angel.minio.domain.File file, InputStream inputStream) {
        String objectName = "";
        try {
            fileRepositoryMapper.saveAndUpdate(file);
            String fileType = file.getFileName().substring(file.getFileName().lastIndexOf("."));
            String fileName = file.getId() + fileType;
            objectName = file.getLocation() + "/" + fileName;
            String contentType = file.getMimeType();
            minioUtils.putObjectByStream(bucketName, objectName, inputStream, contentType);
        } catch (Exception ex) {
            logger.error("上传文件失败，路径：{}", objectName);
        }
        return file;
    }

    /**
     * 获取图片
     *
     * @param objectName
     * @param width
     * @param height
     * @return
     */
    @Override
    public byte[] getImageByte(String objectName, int width, int height, String fileType) {

        try {
            byte[] content = new byte[0];
            if (width != 0 && height != 0) {
                InputStream inputStream = minioUtils.getObject(bucketName, objectName);
                BufferedImage image = ImageIO.read(inputStream);
                //设置缩略图
                BufferedImage imageNew = Thumbnails.of(image).forceSize(width, height).outputQuality(1f).asBufferedImage();
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                ImageOutputStream imageOut;
                imageOut = ImageIO.createImageOutputStream(bs);
                ImageIO.write(imageNew, fileType.replace(".", ""), imageOut);
                content = bs.toByteArray();
            } else {
                // 创建查询参数映射
                Map<String, String> queryParams = new HashMap<>();
                // 注意：这里需要根据MinIO服务支持的查询参数进行设置
                // 例如，如果你的MinIO服务器支持image processing，并且已经安装了相关的中间件
                queryParams.put("image", "resize"); // 设置图片处理参数
                queryParams.put("w", String.valueOf(width)); // 设置宽度
                queryParams.put("h", String.valueOf(height)); // 设置高度
                // 获取图片对象并处理
                content = minioUtils.getInputByteObject(bucketName, objectName, queryParams);
            }
            return content;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 删除文件
     *
     * @param ids
     */
    @Override
    @Transactional
    public void delFile(String[] ids) {
        for (String id : ids) {
            com.angel.minio.domain.File resultFile = fileRepositoryMapper.findById(id);
            if (StringUtils.isNotNull(resultFile)) {
                String fileType = resultFile.getFileName().substring(resultFile.getFileName().lastIndexOf("."));
                String fileName = resultFile.getId() + fileType;
                String objectName = resultFile.getLocation() + "/" + fileName;
                minioUtils.deleteFile(bucketName, objectName);
                fileRepositoryMapper.deleteById(id);
            }
        }
    }

    /**
     * 按编号列表删除
     *
     * @param ids
     * @return
     */
    @Override
    public List<Map<String, String>> findFileIdAndName(List<String> ids) {
        return fileRepositoryMapper.findFileIdAndName(ids);
    }

    /**
     * 构建临时zip文件
     **/
    public File compressedFileToZip(String name, String bucketName, Iterable<Result<Item>> fileList) {
        String zipName = name.concat(".zip");
        String sysPath = uploadPath + File.separatorChar;
        //临时zip路径
        String fileZipPath = sysPath.concat(zipName);
        OutputStream os = null;
        ZipOutputStream zos = null;
        File file = new File(fileZipPath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            os = new FileOutputStream(file);
            zos = new ZipOutputStream(os);
            for (Result<Item> result : fileList) {
                Item item = result.get();
                if (!item.objectName().endsWith("/")) {

                    try (InputStream stream = minioUtils.getObject(bucketName, item.objectName())) {
                        zos.putNextEntry(new ZipEntry(item.objectName()));
                        //minio 获取流
                        FileInputStream insf = convertToFileInputStream(stream);
                        WritableByteChannel writableByteChannel = Channels.newChannel(zos);
                        FileChannel fileChannel = insf.getChannel();
                        fileChannel.transferTo(0, fileChannel.size(), writableByteChannel);
                        zos.closeEntry();
                        fileChannel.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static FileInputStream convertToFileInputStream(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("temp", ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return new FileInputStream(tempFile);
    }

    /**
     * 按文件名称后缀返回类型
     *
     * @param filePath
     * @return
     */
    public String getFileType(String filePath) {
        String fileType = "";
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            fileType = "image/jpeg";
        } else if (filePath.endsWith(".png")) {
            fileType = "image/png";
        } else if (filePath.endsWith(".gif")) {
            fileType = "image/gif";
        } else if (filePath.endsWith(".bmp")) {
            fileType = "image/bmp";
        } else if (filePath.endsWith(".svg")) {
            fileType = "image/svg+xml";
        } else if (filePath.endsWith(".txt")) {
            fileType = "text/plain";
        } else if (filePath.endsWith(".mp3")) {
            fileType = "audio/mpeg";
        } else if (filePath.endsWith(".mp4")) {
            fileType = "video/mp4";
        } else {
            fileType = "application/octet-stream";
        }
        return fileType;
    }


}
