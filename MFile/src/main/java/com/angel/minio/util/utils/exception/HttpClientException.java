package com.angel.minio.util.utils.exception;

/**
 * @Author yangb
 * @Date 2022/6/28 9:55
 * @Version 1.0
 */
public class HttpClientException extends GlobalException {
    public HttpClientException(String message,Exception e) {
        super(message,e);
    }

    public HttpClientException(String message) {
        super(message);
    }
}
