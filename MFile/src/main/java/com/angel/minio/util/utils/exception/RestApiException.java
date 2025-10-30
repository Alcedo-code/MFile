package com.angel.minio.util.utils.exception;

/**
 * 远程调用异常
 *
 * @author Alcedo
 */
public class RestApiException extends RuntimeException {

    public RestApiException() {
        super();
    }

    /**
     * 用详细信息指定一个异常
     */
    public RestApiException(String message) {
        super(message);
    }

    /**
     * 用指定的详细信息和原因构造一个新的异常
     */

    public RestApiException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 用指定原因构造一个新的异常
     *
     * @param cause
     */
    public RestApiException(Throwable cause) {
        super(cause);
    }


}
