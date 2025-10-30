package com.angel.minio.util.utils.exception;

/**
 * @Author yangb
 * @Date 2022/6/28 9:53
 * @Version 1.0
 */
public class GlobalException extends Exception {

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, Exception e) {
        super(message, e);
    }
}
