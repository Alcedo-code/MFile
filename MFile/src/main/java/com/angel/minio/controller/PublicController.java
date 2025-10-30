package com.angel.minio.controller;


import com.angel.minio.config.minio.MinioProperty;
import com.angel.minio.util.utils.StringUtils;
import com.angel.minio.util.web.controller.BaseController;
import com.angel.minio.util.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 通用操作功能接口
 *
 * @Author yangb
 * @Date 2022/10/13 14:55
 * @Version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/public/")
public class PublicController extends BaseController {

    @Autowired
    MinioProperty minioProperty;

    /**
     * 获取预览地址前缀
     *
     * @return
     */
    @GetMapping("/getUrl")
    public AjaxResult getUrl() {
        String httpUrl = minioProperty.getUrl();
        if (StringUtils.isNotEmpty(httpUrl)) {
            return AjaxResult.success("获取成功", httpUrl);
        } else {
            return AjaxResult.error("获取地址错误");
        }
    }


}
