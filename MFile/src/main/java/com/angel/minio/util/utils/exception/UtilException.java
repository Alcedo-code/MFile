package com.angel.minio.util.utils.exception;

/**
 * 工具类异常
 *
 * @author yangb
 */
public class UtilException extends RuntimeException
{
    private static final long serialVersionUID = 8247610319171014189L;

    public UtilException(Throwable e)
    {
        super(e.getMessage(), e);
    }

    public UtilException(String message)
    {
        super(message);
    }

    public UtilException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
