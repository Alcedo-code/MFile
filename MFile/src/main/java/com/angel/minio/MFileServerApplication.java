package com.angel.minio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;



/**
 * @author yangb
 */
@SpringBootApplication
@MapperScan("com.angel.minio.mapper")
public class MFileServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MFileServerApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MFileServerApplication.class);
    }

}
