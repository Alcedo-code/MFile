package com.angel.minio.controller;

import com.angel.minio.domain.Buckets;
import com.angel.minio.domain.Folder;
import com.angel.minio.domain.Folders;
import com.angel.minio.service.IFilesService;
import com.angel.minio.service.ITSystemApplication;
import com.angel.minio.config.minio.MinioProperty;
import com.angel.minio.util.utils.StringUtils;
import com.angel.minio.util.web.controller.BaseController;
import com.angel.minio.util.web.domain.AjaxResult;
import com.angel.minio.util.web.domain.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * MFile上传到minio操作接口
 *
 * @Author yangb
 * @Date 2022/11/27 15:55
 * @Version 1.0
 */
@CrossOrigin
@Controller
@RequestMapping("/minio")
public class MFileController extends BaseController {

    @Autowired
    MinioProperty minioProperty;

    @Autowired
    IFilesService filesService;

    @Autowired
    ITSystemApplication systemApplication;

    /**
     * 查询所有系统信息
     *
     * @return
     */
    @PostMapping("/queryList")
    @ResponseBody
    public AjaxResult queryList() {
        List<Buckets> list;
        try {
            list = systemApplication.queryList();
        } catch (Exception ex) {
            logger.error("查询桶信息异常：{}", ex.toString());
            return AjaxResult.error("查询异常");
        }
        return AjaxResult.success("查询成功", list);
    }

    /**
     * 创建应用系统
     *
     * @param bucketName
     * @return
     */
    @PostMapping("/createBucket/{bucketName}")
    @ResponseBody
    public AjaxResult createBucket(@PathVariable("bucketName") String bucketName) {
        try {
            if (StringUtils.isEmpty(bucketName)) {
                return AjaxResult.error("名称不能为空");
            }
            systemApplication.insertAndUpdate(bucketName);
            return AjaxResult.success("创建成功");
        } catch (Exception ex) {
            logger.error("创建应用异常：{}", ex.toString());
            return AjaxResult.error("创建失败！");
        }
    }

    /**
     * 删除应用系统
     *
     * @param bucketName
     * @return
     */
    @PostMapping("/removeBucket/{bucketName}")
    @ResponseBody
    public AjaxResult removeBucket(@PathVariable("bucketName") String bucketName) {
        try {
            systemApplication.delSystemApplication(bucketName);
            return AjaxResult.success("删除成功");
        } catch (Exception ex) {
            logger.error("删除应用异常：{}", ex.toString());
            return AjaxResult.error("删除失败！");
        }
    }

    /**
     * 在系统中创建文件夹
     *
     * @param folder
     * @return
     */
    @PostMapping("/createFolder")
    @ResponseBody
    public AjaxResult createFolder(@RequestBody Folder folder) {
        try {
            filesService.createFolder(folder);
            return AjaxResult.success("创建成功");
        } catch (Exception ex) {
            return AjaxResult.error("创建失败！");
        }
    }

    /**
     * 在系统中删除文件夹
     *
     * @param folder
     * @return
     */
    @PostMapping("/removeFolder")
    @ResponseBody
    public AjaxResult removeFolder(@RequestBody Folder folder) {
        try {
            filesService.removeFolder(folder);
            return AjaxResult.success("删除成功");
        } catch (Exception ex) {
            return AjaxResult.error("创建失败！");
        }
    }


    /**
     * 查询系统下的路径内容
     *
     * @param folder
     * @return
     */
    @PostMapping("/queryBucket")
    @ResponseBody
    public AjaxResult queryBucket(@RequestBody Folder folder) {
        try {
            List<HashMap<String, String>> hashMapList = filesService.queryBucket(folder);
            return AjaxResult.success("查询成功", hashMapList);
        } catch (Exception ex) {
            return AjaxResult.error("删除失败！");
        }
    }

    /**
     * 文件上传
     *
     * @param file
     * @param folder:{"bucketName":"",url:"地址路径"}
     * @return
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public AjaxResult uploadFile(@RequestBody MultipartFile file, Folder folder) {
        if (StringUtils.isEmpty(file.getContentType())) {
            return AjaxResult.error("无法获取文件类型");
        }
        ResultEntity<Map<String, Object>> result = filesService.uploadFile(file, folder);
        if (result.getCode() == 0) {
            return AjaxResult.success("上传成功");
        } else {
            return AjaxResult.error("上传错误！！！");
        }
    }


    /**
     * 上传zip压缩包,指定某个桶目录上传
     *
     * @param file
     * @param folder
     * @return
     */
    @PostMapping("/uploadZip")
    @ResponseBody
    public AjaxResult uploadZip(@RequestBody MultipartFile file, Folder folder) {
        if (filesService.uploadZip(file, folder) > 0) {
            return AjaxResult.success("上传成功");
        } else {
            return AjaxResult.error("上传失败");
        }
    }

    /**
     * 文件夹下载，按zip压缩包
     *
     * @param bucketName
     * @param prefix
     * @return
     */
    @GetMapping("/downloadZip")
    @ResponseBody
    public void downloadZip(String bucketName, String prefix) {
        try {
            String uuid = StringUtils.getUUid();
            filesService.minioDownloadZip(uuid, bucketName, prefix);
        } catch (Exception e) {
            logger.error("下载异常：{}", e.toString());
        }
    }

    /**
     * 文件预览
     *
     * @param folder
     * @return
     */
    @PostMapping("/getRedFile")
    @ResponseBody
    public AjaxResult getRedFile(@RequestBody Folder folder) {
        String outUrl = filesService.getRedFile(folder);
        return AjaxResult.success(outUrl);
    }

    /**
     * 文件下载
     *
     * @param bucketName 应用桶名称
     * @param prefix     文件路径 例：1111/3.jpg
     * @return
     */
    @GetMapping("/downloadFile")
    @ResponseBody
    public String downloadFile(String bucketName, String prefix) {
        return filesService.downloadFile(bucketName, prefix) != null ? "下载成功" : "下载失败";
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param fileName
     * @return
     */
    @PostMapping("/deleteFile")
    @ResponseBody
    public AjaxResult deleteFile(String bucketName, String fileName) {
        if (filesService.deleteFile(bucketName, fileName)) {
            return AjaxResult.success("删除成功");
        } else {
            return AjaxResult.error("删除失败");
        }
    }

    /**
     * 批量删除文件和文件夹
     *
     * @param folder
     * @return
     */
    @PostMapping("/deleteAll")
    @ResponseBody
    public AjaxResult deleteAll(@RequestBody Folders folder) {
        try {
            filesService.deletePrefixList(folder);
            return AjaxResult.success("删除成功");
        } catch (Exception ex) {
            return AjaxResult.error("创建失败！");
        }
    }

}


