package com.qk.config;

import com.qk.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")                              // 拦截所有请求
                .excludePathPatterns(
                        "/login",                                   // 放行登录
                        "/api/login",                               // 放行登录（带前缀）
                        "/upload",                                  // 放行上传
                        "/api/upload",                              // 放行上传（带前缀）
                        "/images/**",                               // 放行图片资源
                        "/css/**",                                  // 放行静态资源
                        "/js/**",                                   // 放行静态资源
                        "/favicon.ico",                             // 放行网站图标
                        "/swagger-ui/**",                           // 放行 Swagger（如果有）
                        "/v3/api-docs/**",                          // 放行 Swagger（如果有）
                        "/doc.html"                                 // 放行 Knife4j（如果有）
                );
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取项目根目录下的 images 文件夹
        String projectRoot = System.getProperty("user.dir");
        String uploadPath = Paths.get(projectRoot, "images").toString();

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}