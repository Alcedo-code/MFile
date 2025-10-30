package com.angel.minio.util.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件上传下载处理工具类
 * @Author yangbo
 * @Date 2023/6/30 13:43
 * @Version 1.0
 */

public class FileUploadUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUploadUtils.class);

    /**
     * 文件上传
     * @param file 文件
     * @param uploadPath 路径
     * @return
     */
    public String upload(MultipartFile file,String uploadPath) {
        if (Objects.isNull(file) || file.isEmpty()) {
            logger.info("文件为空");
            return "上传失败";
        }else {
            String originallyName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String suffix = originallyName.substring(originallyName.lastIndexOf("."));
            Date now = new Date();
            String storageName = uuid.replace("-", "") + now.getTime() + suffix;

            try {
                File path = new File(uploadPath);
                if (!path.exists()) {
                    path.mkdirs();
                }
                file.transferTo(new File(uploadPath + "/" + storageName));
                return storageName;
            } catch (Exception e) {
                e.printStackTrace();
                return "上传失败";
            }
        }
    }

    /**
     * 本地文件下载
     * @param response
     * @param fileName
     * @param uploadPath
     * @throws FileNotFoundException
     */
    public void downloadLocal(HttpServletResponse response, String fileName, String uploadPath) throws FileNotFoundException {
        InputStream inStream = new FileInputStream(uploadPath + "/" + fileName);
        String fname = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.reset();
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment;filename=".concat(fname));
        byte[] b = new byte[100];
        try {
            System.out.print("fileName===" + fname);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     * @param filePath
     */
    public static void delFile(String filePath) {
        try {
            File file = new File(filePath);
            if(file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.out.println("删除文件出错");
            e.printStackTrace();
        }
    }

}
