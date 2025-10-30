package com.angel.minio.util.utils.exception;

/**
 * @Author yangbo
 * @Date 2023/6/25 15:19
 * @Version 1.0
 */
public class UserPasswordNotMatchException extends UserException
{

    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException()
    {
        super("user.password.not.match", null);
    }
}

