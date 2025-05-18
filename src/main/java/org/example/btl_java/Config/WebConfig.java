package org.example.btl_java.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ thư mục images
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/D:/HK6/BTLJava/BTL_Java/images/");
    }
}