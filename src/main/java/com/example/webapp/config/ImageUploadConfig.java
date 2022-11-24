package com.example.webapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageUploadConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        配置虚拟路径 不然上传图片后需要重启项目才能加到target里 显示不了图片
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:"+System.getProperty("user.dir")+"/src/main/resources/images/");
    }
}
