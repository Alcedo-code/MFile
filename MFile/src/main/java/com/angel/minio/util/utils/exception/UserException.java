package com.angel.minio.util.utils.exception;

/**
 * @Author yangbo
 * @Date 2023/6/25 15:21
 * @Version 1.0
 */
public class UserException extends BaseException
{

    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("TVgUser", code, args, null);
    }

}
