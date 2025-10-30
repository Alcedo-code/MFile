package com.angel.minio.domain;

/**
 * @ClassName FileType
 * @Description TODO
 * @Date 2020/12/20 14:51
 */
public class FileLocation {
    public static String FILE_IMAGE = "image";
    public static String FILE_VIDEO = "video";
    public static String FILE_DEFAULT = "text";
    public static String FILE_AUDIO = "audio";
    public static String[] DIR = {FileLocation.FILE_IMAGE,FileLocation.FILE_VIDEO,FileLocation.FILE_DEFAULT,FileLocation.FILE_AUDIO};
}
