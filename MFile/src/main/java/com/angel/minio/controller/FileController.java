package com.angel.minio.controller;

import com.angel.minio.domain.File;
import com.angel.minio.domain.FileLocation;
import com.angel.minio.service.IFilesService;
import com.angel.minio.domain.VoInfo;
import com.angel.minio.util.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * MFile直接上传文件到文件接口
 * @Author YangBo
 * @Date 2024/3/16 12:53
 * @Version 1.0
 */
@CrossOrigin
@Controller
@RequestMapping("/file")
public class FileController {


    @Autowired
    IFilesService filesService;

    /**
     * @ignore
     * @return
     */
    @GetMapping("/info")
    public String info(){
        return "start success";
    }

    /**
     * 按编号获取图片
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public ResponseEntity getFile(@PathVariable("id") String id) {

        File file = filesService.findById(id);
        if (file == null) {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
        // Generate the http headers with the file properties
        HttpHeaders headers = new HttpHeaders();
        String primaryType, subType;
        try {
            headers.add("content-disposition", "attachment; filename* = UTF-8''" + URLEncoder.encode(file.getFileName(), "UTF-8"));
            primaryType = file.getMimeType().split("/")[0];
            subType = file.getMimeType().split("/")[1];
        } catch (IndexOutOfBoundsException | NullPointerException ex) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        headers.setContentType(new MediaType(primaryType, subType));
        String fileType = file.getFileName().substring(file.getFileName().lastIndexOf("."));
        String fileName = file.getId() + fileType;
        String objectName = file.getLocation() + "/" + fileName;
        byte[] content = new byte[0];
        try {
                content =filesService.getImageByte(objectName,0,0,"");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    /**
     * 按编号获取指定大小图片
     * @param id
     * @param width
     * @param height
     * @return
     */
    @GetMapping("/get/{id}/{width}/{height}")
    public ResponseEntity getFile(@PathVariable("id") String id,@PathVariable("width")Integer width,@PathVariable("height")Integer height) {
        File file = filesService.findById(id);

        if (file == null || !file.getMimeType().toLowerCase().startsWith("image") || width == null || height == null) {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }

        // Generate the http headers with the file properties
        HttpHeaders headers = new HttpHeaders();
        String primaryType, subType;
        try {
            headers.add("content-disposition", "attachment; filename* = UTF-8''" + URLEncoder.encode(file.getFileName(), "UTF-8"));
            primaryType = file.getMimeType().split("/")[0];
            subType = file.getMimeType().split("/")[1];
        } catch (IndexOutOfBoundsException | NullPointerException ex) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        headers.setContentType(new MediaType(primaryType, subType));
        String fileType = file.getFileName().substring(file.getFileName().lastIndexOf("."));
        String fileName = file.getId() + fileType;
        String objectName = file.getLocation() + "/"+ fileName;

        byte[] content = new byte[0];
        try {
            //截至以上代码都沿用之前的逻辑，方便兼容之前接口信息
            content =filesService.getImageByte(objectName,width,height,fileType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }


    /**
     * 文件上传
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity uploadFile(MultipartHttpServletRequest request) {
        File newFile = null;
        try {
            Iterator<String> itr = request.getFileNames();
            while (itr.hasNext()) {
                String uploadedFile = itr.next();
                MultipartFile file = request.getFile(uploadedFile);
                String mimeType = file.getContentType();
                String filename = file.getOriginalFilename();
                if (filename.length() > 100) {
                    return new ResponseEntity<>("{\"errorMessage\":\"文件名字符数不能超过100\"}", HttpStatus.FORBIDDEN);
                }
                //大连总线都是image/*
                if(mimeType.equalsIgnoreCase("image/*")){
                    String fileType = filename.substring(filename.lastIndexOf("."));
                    if(fileType.equalsIgnoreCase("png")){
                        mimeType = MediaType.IMAGE_PNG_VALUE;
                    }else if(fileType.equalsIgnoreCase("gif")){
                        mimeType = MediaType.IMAGE_GIF_VALUE;
                    }else if(fileType.equalsIgnoreCase("bmp")){
                        mimeType = "image/bmp";
                    }else{
                        mimeType = MediaType.IMAGE_JPEG_VALUE;
                    }
                }
                String location = FileLocation.FILE_DEFAULT;
                if (mimeType.startsWith("audio")){
                    location = FileLocation.FILE_AUDIO;
                }else if (mimeType.startsWith("image")){
                    location = FileLocation.FILE_IMAGE;
                }else if (mimeType.startsWith("video")){
                    location = FileLocation.FILE_VIDEO;
                }
                InputStream inputStream = file.getInputStream();
                newFile = new File();
                newFile.setId(StringUtils.getUUid());
                newFile.setFileName(filename);
                newFile.setMimeType(mimeType);
                newFile.setLocation(location);
                newFile = filesService.uploadFile(newFile, inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{\"id\":\"" + newFile.getId() + "\",\"name\":\"" + newFile.getFileName() + "\"}", HttpStatus.OK);
    }

    /**
     * 按文件编号查询返回文件信息
     * @param ids
     * @return
     */
    @GetMapping("/getFileIdAndName")
    @ResponseBody
    public List<Map<String, String>> getFileIdAndName(@RequestParam List<String> ids) {
        return filesService.findFileIdAndName(ids);
    }

    /**
     * 根据文件编号进行删除
     * @param ids 多个逗号分隔
     * @dis
     * @return
     */
    @GetMapping("/del")
    @ResponseBody
    public VoInfo delFile(String[] ids) {
        VoInfo info = new VoInfo();
        info.setResult(true);
        filesService.delFile(ids);
        return info;
    }

}
