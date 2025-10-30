package com.angel.minio.util.utils;

/**
 * 返回状态码
 *
 */
public class HttpStatus
{
    private HttpStatus(){}
    /**
     * 操作成功
     */
    public static final int SUCCESS = 0;

    /**
     * 对象创建成功
     */
    public static final int CREATED = 1;

    /**
     * 操作已经执行成功，但是没有返回数据
     */
    public static final int NO_CONTENT = 1;
    /**
     * 不允许的http方法
     */
    public static final int BAD_METHOD = 1;

    /**
     * 资源冲突，或者资源被锁
     */
    public static final int CONFLICT = 1;

    /**
     * 系统内部错误
     */
    public static final int ERROR = 1;
}
