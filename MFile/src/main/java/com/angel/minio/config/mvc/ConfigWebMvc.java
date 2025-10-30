package com.angel.minio.config.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author yangbo
 * @Date 2023/11/20 14:24
 * @Version 1.0
 */
@Configuration
public class ConfigWebMvc implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 系统默认地址：index.html
        registry.addViewController("/").setViewName("redirect:index.html");
        // 将默认地址改为doc/index.html
        registry.addViewController("/d").setViewName("redirect:doc/index.html");
    }
}
