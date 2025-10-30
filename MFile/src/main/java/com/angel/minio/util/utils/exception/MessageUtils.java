package com.angel.minio.util.utils.exception;

import com.angel.minio.util.utils.SpringUtils;
import org.springframework.context.MessageSource;

/**
 * @Author yangbo
 * @Date 2023/6/25 15:25
 * @Version 1.0
 */
public class MessageUtils
{

    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return
     */
    public static String message(String code, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, null);
    }

}
